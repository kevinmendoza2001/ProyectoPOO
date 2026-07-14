package controller;

import dao.UsuarioDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Usuario;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnIngresar;

    @FXML
    private Label lblMensaje;

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @FXML
    private void initialize() {
        txtPassword.setOnAction(this::onIngresar);
    }

    @FXML
    private void onIngresar(ActionEvent event) {
        String username = txtUsuario.getText() != null ? txtUsuario.getText().trim() : "";
        String password = txtPassword.getText() != null ? txtPassword.getText() : "";

        if (username.isEmpty() || password.isEmpty()) {
            mostrarMensaje("Por favor ingrese usuario y contraseña.");
            return;
        }

        Usuario usuario = usuarioDAO.autenticar(username, password);

        if (usuario == null) {
            mostrarMensaje("Usuario o contraseña incorrectos.");
            return;
        }

        abrirDashboard(usuario);
    }

    private void mostrarMensaje(String texto) {
        if (lblMensaje != null) {
            lblMensaje.setText(texto);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, texto);
            alert.showAndWait();
        }
    }

    private void abrirDashboard(Usuario usuario) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/dashboard.fxml"));
            Parent root = loader.load();

            DashboardController controller = loader.getController();
            controller.setUsuario(usuario);

            Stage stage = (Stage) btnIngresar.getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/stilos.css").toExternalForm());

            stage.setScene(scene);
            stage.setTitle("Restobar - Panel principal (" + usuario.getRol() + ")");
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarMensaje("No se pudo cargar el panel principal.");
        }
    }
}