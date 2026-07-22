package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.Usuario;

import java.io.IOException;

public class DashboardController {

    @FXML private BorderPane rootPane;
    @FXML private StackPane contentArea;
    @FXML private Label lblBienvenida;
    @FXML private Label lblRol;
    @FXML private Button btnCerrarSesion;
    @FXML private Button btnProductos;
    @FXML private Button btnReportes;
    @FXML private Button btnUsuarios;
    @FXML private Button btnConfiguracion;

    private Usuario usuario;

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        if (lblBienvenida != null) {
            lblBienvenida.setText("Bienvenido, " + usuario.getNombre());
        }
        if (lblRol != null) {
            lblRol.setText("Rol: " + usuario.getRol());
        }
        aplicarPermisosPorRol();
        cargarPantallaInicial();
    }

    private void aplicarPermisosPorRol() {
        String rol = usuario.getRol();

        rootPane.getStyleClass().removeAll("theme-admin", "theme-cajero", "theme-reportes");

        switch (rol) {
            case "Administrador":
                rootPane.getStyleClass().add("theme-admin");
                btnProductos.setDisable(false);
                btnReportes.setDisable(false);
                btnUsuarios.setDisable(false);
                btnConfiguracion.setDisable(false);
                break;
            case "Cajero":
                rootPane.getStyleClass().add("theme-cajero");
                btnProductos.setDisable(false);
                btnReportes.setDisable(true);
                btnUsuarios.setDisable(true);
                btnConfiguracion.setDisable(true);
                break;
            case "Reportes":
                rootPane.getStyleClass().add("theme-reportes");
                btnProductos.setDisable(true);
                btnReportes.setDisable(false);
                btnUsuarios.setDisable(true);
                btnConfiguracion.setDisable(true);
                break;
            default:
                btnProductos.setDisable(true);
                btnReportes.setDisable(true);
                btnUsuarios.setDisable(true);
                btnConfiguracion.setDisable(true);
        }
    }

    private void cargarPantallaInicial() {
        if ("Reportes".equals(usuario.getRol())) {
            cargarContenido("/view/reporte.fxml");
        } else {
            cargarContenido("/view/producto.fxml");
        }
    }

    @FXML
    private void onGestionarProductos(ActionEvent event) {
        cargarContenido("/view/producto.fxml");
    }

    @FXML
    private void onReportes(ActionEvent event) {
        cargarContenido("/view/reporte.fxml");
    }

    @FXML
    private void onUsuarios(ActionEvent event) {
        cargarContenido("/view/usuario.fxml");
    }

    @FXML
    private void onConfiguracion(ActionEvent event) {
        cargarContenido("/view/configuracion.fxml");
    }

    private void cargarContenido(String rutaFxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFxml));
            Parent contenido = loader.load();
            contentArea.getChildren().setAll(contenido);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onCerrarSesion(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/stilos.css").toExternalForm());

            Stage stage = (Stage) btnCerrarSesion.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Restobar - Iniciar sesión");
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}