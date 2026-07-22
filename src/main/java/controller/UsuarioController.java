package controller;

import dao.UsuarioDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Usuario;

public class UsuarioController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtCedula;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private ComboBox<String> cmbRol;
    @FXML private Label lblMensaje;

    @FXML private Button btnGuardar;
    @FXML private Button btnActualizar;
    @FXML private Button btnEliminar;
    @FXML private Button btnLimpiar;

    @FXML private TableView<Usuario> tablaUsuarios;
    @FXML private TableColumn<Usuario, Integer> colId;
    @FXML private TableColumn<Usuario, String> colNombre;
    @FXML private TableColumn<Usuario, String> colUsername;
    @FXML private TableColumn<Usuario, String> colRol;

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final ObservableList<Usuario> listaUsuarios = FXCollections.observableArrayList();

    private Usuario usuarioSeleccionado = null;

    @FXML
    private void initialize() {
        cmbRol.setItems(FXCollections.observableArrayList("Administrador", "Cajero", "Reportes"));

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));

        tablaUsuarios.setItems(listaUsuarios);

        tablaUsuarios.getSelectionModel().selectedItemProperty().addListener(
                (obs, anterior, seleccionado) -> cargarEnFormulario(seleccionado)
        );

        cargarUsuarios();
    }

    private void cargarUsuarios() {
        listaUsuarios.setAll(usuarioDAO.listar());
    }

    private void cargarEnFormulario(Usuario u) {
        usuarioSeleccionado = u;
        if (u == null) {
            return;
        }
        txtNombre.setText(u.getNombre());
        txtCedula.setText(u.getCedula());
        txtTelefono.setText(u.getTelefono());
        txtUsername.setText(u.getUsername());
        txtPassword.clear();
        cmbRol.setValue(u.getRol());
    }

    @FXML
    private void onGuardar(ActionEvent event) {
        Usuario u = leerFormulario(true);
        if (u == null) {
            return;
        }

        if (usuarioDAO.existeUsername(u.getUsername(), 0)) {
            mostrarMensaje("Ya existe un usuario con ese nombre de usuario.");
            return;
        }

        if (usuarioDAO.guardar(u)) {
            mostrarMensaje("Usuario guardado correctamente.");
            cargarUsuarios();
            onLimpiar(null);
        } else {
            mostrarMensaje("No se pudo guardar el usuario.");
        }
    }

    @FXML
    private void onActualizar(ActionEvent event) {
        if (usuarioSeleccionado == null) {
            mostrarMensaje("Selecciona un usuario de la tabla para actualizar.");
            return;
        }

        boolean cambioPassword = txtPassword.getText() != null && !txtPassword.getText().isEmpty();
        Usuario u = leerFormulario(cambioPassword);
        if (u == null) {
            return;
        }
        u.setId(usuarioSeleccionado.getId());
        if (!cambioPassword) {
            u.setPassword(usuarioSeleccionado.getPassword());
        }

        if (usuarioDAO.existeUsername(u.getUsername(), u.getId())) {
            mostrarMensaje("Ya existe otro usuario con ese nombre de usuario.");
            return;
        }

        if (usuarioDAO.actualizar(u)) {
            mostrarMensaje("Usuario actualizado correctamente.");
            cargarUsuarios();
            onLimpiar(null);
        } else {
            mostrarMensaje("No se pudo actualizar el usuario.");
        }
    }

    @FXML
    private void onEliminar(ActionEvent event) {
        if (usuarioSeleccionado == null) {
            mostrarMensaje("Selecciona un usuario de la tabla para eliminar.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION,
                "¿Seguro que deseas eliminar a \"" + usuarioSeleccionado.getNombre() + "\"?",
                ButtonType.YES, ButtonType.NO);
        confirmacion.setHeaderText(null);
        confirmacion.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.YES) {
                if (usuarioDAO.eliminar(usuarioSeleccionado.getId())) {
                    mostrarMensaje("Usuario eliminado.");
                    cargarUsuarios();
                    onLimpiar(null);
                } else {
                    mostrarMensaje("No se pudo eliminar el usuario.");
                }
            }
        });
    }

    @FXML
    private void onLimpiar(ActionEvent event) {
        usuarioSeleccionado = null;
        tablaUsuarios.getSelectionModel().clearSelection();
        txtNombre.clear();
        txtCedula.clear();
        txtTelefono.clear();
        txtUsername.clear();
        txtPassword.clear();
        cmbRol.setValue(null);
        lblMensaje.setText("");
    }

    private Usuario leerFormulario(boolean validarPassword) {
        String nombre = txtNombre.getText() == null ? "" : txtNombre.getText().trim();
        String cedula = txtCedula.getText() == null ? "" : txtCedula.getText().trim();
        String telefono = txtTelefono.getText() == null ? "" : txtTelefono.getText().trim();
        String username = txtUsername.getText() == null ? "" : txtUsername.getText().trim();
        String password = txtPassword.getText() == null ? "" : txtPassword.getText();
        String rol = cmbRol.getValue();

        if (nombre.length() < 3 || nombre.length() > 100) {
            mostrarMensaje("El nombre debe tener entre 3 y 100 caracteres.");
            return null;
        }

        if (!cedulaValida(cedula)) {
            mostrarMensaje("La cédula ecuatoriana no es válida.");
            return null;
        }

        if (!telefono.matches("^09\\d{8}$")) {
            mostrarMensaje("El teléfono debe tener 10 dígitos y empezar con 09.");
            return null;
        }

        if (!username.matches("^[A-Za-z0-9_.]{4,20}$")) {
            mostrarMensaje("El usuario debe tener 4 a 20 caracteres: letras, números, punto o guion bajo.");
            return null;
        }

        if (rol == null || rol.isBlank()) {
            mostrarMensaje("Selecciona un rol.");
            return null;
        }

        if (validarPassword && password.length() < 6) {
            mostrarMensaje("La contraseña debe tener al menos 6 caracteres.");
            return null;
        }

        return new Usuario(0, nombre, cedula, telefono, username, password, rol);
    }

    private boolean cedulaValida(String cedula) {
        if (!cedula.matches("\\d{10}")) {
            return false;
        }

        int provincia = Integer.parseInt(cedula.substring(0, 2));
        if (provincia < 1 || provincia > 24) {
            return false;
        }

        int suma = 0;
        for (int i = 0; i < 9; i++) {
            int digito = Character.getNumericValue(cedula.charAt(i));

            if (i % 2 == 0) {
                digito *= 2;
                if (digito > 9) {
                    digito -= 9;
                }
            }
            suma += digito;
        }

        int verificador = (10 - (suma % 10)) % 10;
        return verificador == Character.getNumericValue(cedula.charAt(9));
    }

    private void mostrarMensaje(String texto) {
        lblMensaje.setText(texto);
    }
}