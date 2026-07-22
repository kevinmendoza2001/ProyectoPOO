package controller;

import dao.ProductoDAO;
import dao.VentaDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import model.DetalleVenta;
import model.Producto;
import model.Usuario;
import model.Venta;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class VentaController {

    // --- Registrar venta ---
    @FXML private ComboBox<Producto> cmbProducto;
    @FXML private TextField txtCantidad;
    @FXML private Label lblMensaje;
    @FXML private Button btnAgregar;
    @FXML private Button btnRegistrarVenta;
    @FXML private Button btnLimpiarCarrito;
    @FXML private Label lblTotalVenta;

    @FXML private TableView<DetalleVenta> tablaCarrito;
    @FXML private TableColumn<DetalleVenta, String> colProductoCarrito;
    @FXML private TableColumn<DetalleVenta, Integer> colCantidadCarrito;
    @FXML private TableColumn<DetalleVenta, Double> colPrecioCarrito;
    @FXML private TableColumn<DetalleVenta, Double> colSubtotalCarrito;

    // --- Consultar stock (solo lectura) ---
    @FXML private TableView<Producto> tablaStock;
    @FXML private TableColumn<Producto, Integer> colStockId;
    @FXML private TableColumn<Producto, String> colStockNombre;
    @FXML private TableColumn<Producto, String> colStockCategoria;
    @FXML private TableColumn<Producto, Double> colStockPrecio;
    @FXML private TableColumn<Producto, Integer> colStockCantidad;

    private final ProductoDAO productoDAO = new ProductoDAO();
    private final VentaDAO ventaDAO = new VentaDAO();

    private final ObservableList<DetalleVenta> carrito = FXCollections.observableArrayList();
    private Venta ventaActual;
    private Usuario usuarioActual;

    public void setUsuario(Usuario usuario) {
        this.usuarioActual = usuario;
    }

    @FXML
    private void initialize() {
        colProductoCarrito.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));
        colCantidadCarrito.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colPrecioCarrito.setCellValueFactory(new PropertyValueFactory<>("precioUnitario"));
        colSubtotalCarrito.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
        tablaCarrito.setItems(carrito);

        colStockId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colStockNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colStockCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colStockPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colStockCantidad.setCellValueFactory(new PropertyValueFactory<>("stock"));

        cmbProducto.setConverter(new StringConverter<Producto>() {
            @Override
            public String toString(Producto p) {
                if (p == null) return "";
                return p.getNombre() + " — $" + String.format("%.2f", p.getPrecio()) + " (stock: " + p.getStock() + ")";
            }

            @Override
            public Producto fromString(String s) {
                return null;
            }
        });

        cargarProductosDisponibles();
        cargarStock();
        actualizarTotal();
    }

    private void cargarProductosDisponibles() {
        ObservableList<Producto> disponibles = FXCollections.observableArrayList(
                productoDAO.listar().stream()
                        .filter(Producto::isDisponible)
                        .filter(p -> p.getStock() > 0)
                        .toList()
        );

        cmbProducto.setItems(disponibles);
    }

    private void cargarStock() {
        tablaStock.setItems(FXCollections.observableArrayList(productoDAO.listar()));
    }

    @FXML
    private void onAgregar(ActionEvent event) {
        Producto seleccionado = cmbProducto.getValue();
        String cantidadTexto = txtCantidad.getText() != null ? txtCantidad.getText().trim() : "";

        if (seleccionado == null) {
            mostrarMensaje("Selecciona un producto.");
            return;
        }

        int cantidad;
        try {
            cantidad = Integer.parseInt(cantidadTexto);
        } catch (NumberFormatException e) {
            mostrarMensaje("La cantidad debe ser un número.");
            return;
        }

        if (cantidad <= 0) {
            mostrarMensaje("La cantidad debe ser mayor a cero.");
            return;
        }

        int cantidadEnCarrito = carrito.stream()
                .filter(d -> d.getIdProducto() == seleccionado.getId())
                .mapToInt(DetalleVenta::getCantidad)
                .sum();

        if (cantidad + cantidadEnCarrito > seleccionado.getStock()) {
            mostrarMensaje("No hay suficiente stock de \"" + seleccionado.getNombre()
                    + "\". Disponible: " + seleccionado.getStock() + ".");
            return;
        }

        DetalleVenta detalle = new DetalleVenta(seleccionado.getId(), seleccionado.getNombre(), cantidad, seleccionado.getPrecio());
        carrito.add(detalle);
        actualizarTotal();
        txtCantidad.clear();
        mostrarMensaje("");
    }

    @FXML
    private void onLimpiarCarrito(ActionEvent event) {
        carrito.clear();
        actualizarTotal();
        mostrarMensaje("");
    }

    private void actualizarTotal() {
        double total = carrito.stream().mapToDouble(DetalleVenta::getSubtotal).sum();
        lblTotalVenta.setText(String.format("$%.2f", total));
    }

    @FXML
    private void onRegistrarVenta(ActionEvent event) {
        if (carrito.isEmpty()) {
            mostrarMensaje("Agrega al menos un producto antes de registrar la venta.");
            return;
        }

        Venta venta = new Venta(usuarioActual.getId(), usuarioActual.getNombre());
        venta.getDetalles().addAll(carrito);
        venta.recalcularTotal();

        if (!venta.validarDatos()) {
            mostrarMensaje("Datos de la venta inválidos.");
            return;
        }

        if (ventaDAO.registrarVenta(venta)) {
            ventaActual = venta;
            Alert alert = new Alert(Alert.AlertType.INFORMATION,
                    "Venta registrada correctamente.\nTotal: " + String.format("$%.2f", venta.getTotal()));
            alert.setHeaderText(null);
            alert.showAndWait();

            carrito.clear();
            actualizarTotal();
            cargarProductosDisponibles();
            cargarStock();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "No se pudo registrar la venta. Verifica el stock disponible e inténtalo de nuevo.");
            alert.setHeaderText(null);
            alert.showAndWait();
        }
    }

    @FXML
    private void onImprimirComprobante(ActionEvent event) {
        if (ventaActual == null) {
            mostrarMensaje("Registra una venta antes de imprimir el comprobante.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar comprobante de venta");
        fileChooser.setInitialFileName("comprobante_venta_" + ventaActual.getId() + ".txt");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivo de texto", "*.txt"));

        File archivo = fileChooser.showSaveDialog(btnRegistrarVenta.getScene().getWindow());
        if (archivo == null) {
            return;
        }

        try (FileWriter writer = new FileWriter(archivo)) {
            writer.write("======= RESTOBAR — COMPROBANTE DE VENTA =======\n");
            writer.write("Venta N°: " + ventaActual.getId() + "\n");
            writer.write("Atendido por: " + usuarioActual.getNombre() + "\n");
            writer.write("Fecha: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + "\n");
            writer.write("-------------------------------------------------\n");
            for (DetalleVenta d : ventaActual.getDetalles()) {
                writer.write(String.format("%-20s x%-3d  $%8.2f%n", d.getNombreProducto(), d.getCantidad(), d.getSubtotal()));
            }
            writer.write("-------------------------------------------------\n");
            writer.write(String.format("TOTAL: $%.2f%n", ventaActual.getTotal()));
            writer.write("=================================================\n");

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Comprobante guardado correctamente.");
            alert.setHeaderText(null);
            alert.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "No se pudo generar el comprobante.");
            alert.setHeaderText(null);
            alert.showAndWait();
        }
    }

    private void mostrarMensaje(String texto) {
        lblMensaje.setText(texto);
    }
}