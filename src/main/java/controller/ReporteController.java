package controller;

import dao.ProductoDAO;
import dao.UsuarioDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import model.Producto;
import model.Usuario;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ReporteController {

    @FXML private Label lblTotalProductos;
    @FXML private Label lblTotalUsuarios;
    @FXML private Label lblValorInventario;
    @FXML private Label lblStockBajo;
    @FXML private Button btnDescargar;

    private final ProductoDAO productoDAO = new ProductoDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @FXML
    private void initialize() {
        cargarEstadisticas();
    }

    private void cargarEstadisticas() {
        List<Producto> productos = productoDAO.listar();
        List<Usuario> usuarios = usuarioDAO.listar();

        double valorInventario = productos.stream()
                .mapToDouble(p -> p.getPrecio() * p.getStock())
                .sum();

        long stockBajo = productos.stream().filter(p -> p.getStock() < 5).count();

        lblTotalProductos.setText(String.valueOf(productos.size()));
        lblTotalUsuarios.setText(String.valueOf(usuarios.size()));
        lblValorInventario.setText(String.format("$%.2f", valorInventario));
        lblStockBajo.setText(String.valueOf(stockBajo));
    }

    @FXML
    private void onDescargar(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar reporte de inventario");
        fileChooser.setInitialFileName("reporte_inventario.csv");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Archivo CSV", "*.csv"));

        File archivo = fileChooser.showSaveDialog(btnDescargar.getScene().getWindow());
        if (archivo == null) {
            return;
        }

        try (FileWriter writer = new FileWriter(archivo)) {
            writer.write("ID,Nombre,Categoria,Precio,Stock,Disponible\n");
            for (Producto p : productoDAO.listar()) {
                writer.write(p.getId() + "," + p.getNombre() + "," + p.getCategoria() + ","
                        + p.getPrecio() + "," + p.getStock() + "," + p.isDisponible() + "\n");
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Reporte descargado correctamente.");
            alert.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "No se pudo generar el reporte.");
            alert.showAndWait();
        }
    }
}