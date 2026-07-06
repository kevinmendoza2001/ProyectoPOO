package model;

public class ClaseTematica {

    private int id;
    private String nombre;
    private String categoria; // Entrada, Plato fuerte, Bebida, Postre
    private double precio;
    private int stock;
    private boolean disponible;

    public ClaseTematica() {
    }

    public ClaseTematica(int id, String nombre, String categoria, double precio, int stock, boolean disponible) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.stock = stock;
        this.disponible = disponible;
    }

    public boolean validarDatos() {
        return nombre != null && !nombre.trim().isEmpty() && precio > 0 && stock >= 0;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    @Override
    public String toString() {
        return "ClaseTematica{id=" + id + ", nombre='" + nombre + "', precio=" + precio + "}";
    }
}