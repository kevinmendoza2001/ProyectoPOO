package controller;

import dao.ConfiguracionDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.Configuracion;

public class ConfiguracionController {

    @FXML private TextField txtNombreEmpresa;
    @FXML private TextField txtRuc;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtTelefono;
    @FXML private Label lblMensaje;
    @FXML private Button btnGuardar;

    private final ConfiguracionDAO configuracionDAO = new ConfiguracionDAO();

    @FXML
    private void initialize() {
        Configuracion c = configuracionDAO.obtener();
        txtNombreEmpresa.setText(c.getNombreEmpresa());
        txtRuc.setText(c.getRuc());
        txtDireccion.setText(c.getDireccion());
        txtTelefono.setText(c.getTelefono());
    }

    @FXML
    private void onGuardar(ActionEvent event) {
        String nombreEmpresa = txtNombreEmpresa.getText() != null ? txtNombreEmpresa.getText().trim() : "";

        if (nombreEmpresa.isEmpty()) {
            lblMensaje.setText("El nombre de la empresa es obligatorio.");
            return;
        }

        Configuracion c = new Configuracion(1, nombreEmpresa,
                txtRuc.getText(), txtDireccion.getText(), txtTelefono.getText());

        if (configuracionDAO.guardar(c)) {
            lblMensaje.setText("Configuración guardada correctamente.");
        } else {
            lblMensaje.setText("No se pudo guardar la configuración.");
        }
    }
}