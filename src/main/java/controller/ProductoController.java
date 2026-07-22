package controller;

import dao.ProductoDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Producto;

public class ProductoController {

    @FXML private TextField txtNombre;
    @FXML private ComboBox<String> cmbCategoria;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtStock;
    @FXML private CheckBox chkDisponible;
    @FXML private Label lblMensaje;

    @FXML private Button btnGuardar;
    @FXML private Button btnActualizar;
    @FXML private Button btnEliminar;
    @FXML private Button btnLimpiar;

    @FXML private TableView<Producto> tablaProductos;
    @FXML private TableColumn<Producto, Integer> colId;
    @FXML private TableColumn<Producto, String> colNombre;
    @FXML private TableColumn<Producto, String> colCategoria;
    @FXML private TableColumn<Producto, Double> colPrecio;
    @FXML private TableColumn<Producto, Integer> colStock;
    @FXML private TableColumn<Producto, Boolean> colDisponible;

    private final ProductoDAO productoDAO = new ProductoDAO();
    private final ObservableList<Producto> listaProductos = FXCollections.observableArrayList();

    private Producto productoSeleccionado = null;

    @FXML
    private void initialize() {
        cmbCategoria.setItems(FXCollections.observableArrayList(
                "Entrada", "Plato fuerte", "Bebida", "Postre"
        ));

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colDisponible.setCellValueFactory(new PropertyValueFactory<>("disponible"));

        tablaProductos.setItems(listaProductos);

        tablaProductos.getSelectionModel().selectedItemProperty().addListener(
                (obs, anterior, seleccionado) -> cargarEnFormulario(seleccionado)
        );

        cargarProductos();
    }

    private void cargarProductos() {
        listaProductos.setAll(productoDAO.listar());
    }

    private void cargarEnFormulario(Producto p) {
        productoSeleccionado = p;
        if (p == null) {
            return;
        }
        txtNombre.setText(p.getNombre());
        cmbCategoria.setValue(p.getCategoria());
        txtPrecio.setText(String.valueOf(p.getPrecio()));
        txtStock.setText(String.valueOf(p.getStock()));
        chkDisponible.setSelected(p.isDisponible());
    }

    @FXML
    private void onGuardar(ActionEvent event) {
        Producto p = leerFormulario();
        if (p == null) {
            return;
        }

        if (productoDAO.existeNombre(p.getNombre(), 0)) {
            mostrarMensaje("Ya existe un producto con ese nombre.");
            return;
        }

        if (productoDAO.guardar(p)) {
            mostrarMensaje("Producto guardado correctamente.");
            cargarProductos();
            onLimpiar(null);
        } else {
            mostrarMensaje("No se pudo guardar el producto.");
        }
    }

    @FXML
    private void onActualizar(ActionEvent event) {
        if (productoSeleccionado == null) {
            mostrarMensaje("Selecciona un producto de la tabla para actualizar.");
            return;
        }

        Producto p = leerFormulario();
        if (p == null) {
            return;
        }
        p.setId(productoSeleccionado.getId());

        if (productoDAO.existeNombre(p.getNombre(), p.getId())) {
            mostrarMensaje("Ya existe otro producto con ese nombre.");
            return;
        }

        if (productoDAO.actualizar(p)) {
            mostrarMensaje("Producto actualizado correctamente.");
            cargarProductos();
            onLimpiar(null);
        } else {
            mostrarMensaje("No se pudo actualizar el producto.");
        }
    }

    @FXML
    private void onEliminar(ActionEvent event) {
        if (productoSeleccionado == null) {
            mostrarMensaje("Selecciona un producto de la tabla para eliminar.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION,
                "¿Seguro que deseas eliminar \"" + productoSeleccionado.getNombre() + "\"?",
                ButtonType.YES, ButtonType.NO);
        confirmacion.setHeaderText(null);
        confirmacion.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.YES) {
                if (productoDAO.eliminar(productoSeleccionado.getId())) {
                    mostrarMensaje("Producto eliminado.");
                    cargarProductos();
                    onLimpiar(null);
                } else {
                    mostrarMensaje("No se pudo eliminar el producto.");
                }
            }
        });
    }

    @FXML
    private void onLimpiar(ActionEvent event) {
        productoSeleccionado = null;
        tablaProductos.getSelectionModel().clearSelection();
        txtNombre.clear();
        cmbCategoria.setValue(null);
        txtPrecio.clear();
        txtStock.clear();
        chkDisponible.setSelected(true);
        lblMensaje.setText("");
    }

    private Producto leerFormulario() {
        String nombre = txtNombre.getText() == null ? "" : txtNombre.getText().trim();
        String categoria = cmbCategoria.getValue() == null ? "" : cmbCategoria.getValue().trim();
        String precioTexto = txtPrecio.getText() == null ? "" : txtPrecio.getText().trim();
        String stockTexto = txtStock.getText() == null ? "" : txtStock.getText().trim();

        if (nombre.length() < 3 || nombre.length() > 100) {
            mostrarMensaje("El nombre debe tener entre 3 y 100 caracteres.");
            return null;
        }

        if (categoria.isEmpty()) {
            mostrarMensaje("Selecciona o escribe una categoría.");
            return null;
        }

        double precio;
        int stock;

        try {
            precio = Double.parseDouble(precioTexto);
            stock = Integer.parseInt(stockTexto);
        } catch (NumberFormatException e) {
            mostrarMensaje("Precio y stock deben ser números válidos.");
            return null;
        }

        if (!Double.isFinite(precio) || precio <= 0 || precio > 999999.99) {
            mostrarMensaje("El precio debe ser mayor a 0.");
            return null;
        }

        if (stock < 0) {
            mostrarMensaje("El stock no puede ser negativo.");
            return null;
        }

        return new Producto(
                0, nombre, categoria, precio, stock, chkDisponible.isSelected()
        );
    }

    private void mostrarMensaje(String texto) {
        lblMensaje.setText(texto);
    }
}