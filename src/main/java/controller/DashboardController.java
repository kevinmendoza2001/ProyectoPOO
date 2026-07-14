package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import model.Usuario;

public class DashboardController {

    @FXML
    private Label lblBienvenida;

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
}