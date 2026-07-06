package model;

public class Usuario extends Persona {

    private String username;
    private String password;
    private String rol;

    public Usuario() {
        super();
    }

    public Usuario(int id, String nombre, String cedula, String telefono,
                   String username, String password, String rol) {
        super(id, nombre, cedula, telefono);
        this.username = username;
        this.password = password;
        this.rol = rol;
    }

    @Override
    public boolean validarDatos() {
        return username != null && !username.trim().isEmpty()
                && password != null && password.length() >= 4
                && rol != null && !rol.trim().isEmpty();
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    @Override
    public String toString() {
        return "Usuario{" + super.toString() + ", rol='" + rol + "'}";
    }
}