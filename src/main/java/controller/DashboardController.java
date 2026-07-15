package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.Usuario;

import java.io.IOException;

public class DashboardController {

    @FXML private Label lblBienvenida;
    @FXML private Button btnCerrarSesion;
    @FXML private Button btnProductos;
    @FXML private Button btnReportes;
    @FXML private Button btnUsuarios;
    @FXML private Button btnConfiguracion;

    private Usuario usuario;

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        if (lblBienvenida != null) {
            lblBienvenida.setText("Bienvenido, " + usuario.getNombre() + " (" + usuario.getRol() + ")");
        }
    }

    public Usuario getUsuario() {
        return usuario;
    }

    @FXML
    private void onGestionarProductos(ActionEvent event) {
        cambiarPantalla(event, "/view/producto.fxml", "Restobar - Gestión de Productos");
    }

    @FXML
    private void onReportes(ActionEvent event) {
        mostrarProximamente();
    }

    @FXML
    private void onUsuarios(ActionEvent event) {
        mostrarProximamente();
    }

    @FXML
    private void onConfiguracion(ActionEvent event) {
        mostrarProximamente();
    }

    @FXML
    private void onCerrarSesion(ActionEvent event) {
        cambiarPantalla(event, "/view/login.fxml", "Restobar - Iniciar sesión");
    }

    private void mostrarProximamente() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Esta sección estará disponible próximamente.");
        alert.showAndWait();
    }

    private void cambiarPantalla(ActionEvent event, String rutaFxml, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFxml));
            Parent root = loader.load();

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/stilos.css").toExternalForm());

            stage.setScene(scene);
            stage.setTitle(titulo);
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "No se pudo cargar la pantalla solicitada.");
            alert.showAndWait();
        }
    }
}