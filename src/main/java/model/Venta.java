package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Venta {

    private int id;
    private int idUsuario;
    private String nombreUsuario;
    private LocalDateTime fecha;
    private double total;
    private String estado;
    private List<DetalleVenta> detalles = new ArrayList<>();

    public Venta() {
    }

    public Venta(int idUsuario, String nombreUsuario) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.estado = "Pagado";
    }

    public boolean validarDatos() {
        return idUsuario > 0 && detalles != null && !detalles.isEmpty() && total > 0;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public List<DetalleVenta> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleVenta> detalles) { this.detalles = detalles; }

    public void agregarDetalle(DetalleVenta d) {
        detalles.add(d);
        recalcularTotal();
    }

    public void recalcularTotal() {
        this.total = detalles.stream().mapToDouble(DetalleVenta::getSubtotal).sum();
    }

    @Override
    public String toString() {
        return "Venta{id=" + id + ", total=" + total + ", items=" + detalles.size() + "}";
    }
}