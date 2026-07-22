package model;

public class Configuracion {

    private int id;
    private String nombreEmpresa;
    private String ruc;
    private String direccion;
    private String telefono;

    public Configuracion() {
    }

    public Configuracion(int id, String nombreEmpresa, String ruc, String direccion, String telefono) {
        this.id = id;
        this.nombreEmpresa = nombreEmpresa;
        this.ruc = ruc;
        this.direccion = direccion;
        this.telefono = telefono;
    }

    public boolean validarDatos() {
        return nombreEmpresa != null && !nombreEmpresa.trim().isEmpty();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombreEmpresa() { return nombreEmpresa; }
    public void setNombreEmpresa(String nombreEmpresa) { this.nombreEmpresa = nombreEmpresa; }

    public String getRuc() { return ruc; }
    public void setRuc(String ruc) { this.ruc = ruc; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}