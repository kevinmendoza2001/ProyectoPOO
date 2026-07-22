package dao;

import db.Conexion;
import model.DetalleVenta;
import model.Venta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VentaDAO {

    public boolean registrarVenta(Venta venta) {
        String sqlPedido = "INSERT INTO pedidos (id_usuario, total, estado) VALUES (?, ?, ?::estado_pedido) RETURNING id";
        String sqlDetalle = "INSERT INTO detalle_pedido (id_pedido, id_producto, cantidad, subtotal) VALUES (?, ?, ?, ?)";
        String sqlStock = "UPDATE productos SET stock = stock - ? WHERE id = ? AND stock >= ?";

        Connection con = null;
        try {
            con = Conexion.conectar();
            con.setAutoCommit(false);

            int idPedido;
            try (PreparedStatement ps = con.prepareStatement(sqlPedido)) {
                ps.setInt(1, venta.getIdUsuario());
                ps.setDouble(2, venta.getTotal());
                ps.setString(3, "Pagado");
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        con.rollback();
                        return false;
                    }
                    idPedido = rs.getInt("id");
                }
            }

            for (DetalleVenta d : venta.getDetalles()) {
                try (PreparedStatement psStock = con.prepareStatement(sqlStock)) {
                    psStock.setInt(1, d.getCantidad());
                    psStock.setInt(2, d.getIdProducto());
                    psStock.setInt(3, d.getCantidad());
                    int filas = psStock.executeUpdate();
                    if (filas == 0) {
                        // No había stock suficiente en ese momento
                        con.rollback();
                        return false;
                    }
                }
                try (PreparedStatement psDet = con.prepareStatement(sqlDetalle)) {
                    psDet.setInt(1, idPedido);
                    psDet.setInt(2, d.getIdProducto());
                    psDet.setInt(3, d.getCantidad());
                    psDet.setDouble(4, d.getSubtotal());
                    psDet.executeUpdate();
                }
            }

            con.commit();
            venta.setId(idPedido);
            return true;

        } catch (SQLException e) {
            System.out.println("Error al registrar venta: " + e.getMessage());
            try {
                if (con != null) con.rollback();
            } catch (SQLException ex) {
                System.out.println("Error al hacer rollback: " + ex.getMessage());
            }
            return false;
        } finally {
            try {
                if (con != null) con.setAutoCommit(true);
            } catch (SQLException ignored) {
            }
        }
    }
}