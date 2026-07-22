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
        String nombreEmpresa = txtNombreEmpresa.getText() == null
                ? "" : txtNombreEmpresa.getText().trim();
        String ruc = txtRuc.getText() == null
                ? "" : txtRuc.getText().trim();
        String direccion = txtDireccion.getText() == null
                ? "" : txtDireccion.getText().trim();
        String telefono = txtTelefono.getText() == null
                ? "" : txtTelefono.getText().trim();

        if (nombreEmpresa.length() < 3 || nombreEmpresa.length() > 100) {
            lblMensaje.setText("El nombre de la empresa debe tener entre 3 y 100 caracteres.");
            return;
        }

        if (!ruc.matches("\\d{13}")) {
            lblMensaje.setText("El RUC debe contener exactamente 13 dígitos.");
            return;
        }

        if (direccion.length() < 5 || direccion.length() > 150) {
            lblMensaje.setText("Ingresa una dirección válida.");
            return;
        }

        if (!telefonoValido(telefono)) {
            lblMensaje.setText("El teléfono debe tener 10 dígitos y empezar con 0.");
            return;
        }

        Configuracion c = new Configuracion(
                1, nombreEmpresa, ruc, direccion, telefono
        );

        if (configuracionDAO.guardar(c)) {
            lblMensaje.setText("Configuración guardada correctamente.");
        } else {
            lblMensaje.setText("No se pudo guardar la configuración.");
        }
    }

    private boolean telefonoValido(String telefono) {
        return telefono.matches("^0\\d{9}$");
    }
}