package dao;

import db.Conexion;
import model.Configuracion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConfiguracionDAO {

    public Configuracion obtener() {
        String sql = "SELECT id, nombre_empresa, ruc, direccion, telefono FROM configuracion WHERE id = 1";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return new Configuracion(
                        rs.getInt("id"),
                        rs.getString("nombre_empresa"),
                        rs.getString("ruc"),
                        rs.getString("direccion"),
                        rs.getString("telefono")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener configuración: " + e.getMessage());
        }
        return new Configuracion(1, "", "", "", "");
    }

    public boolean guardar(Configuracion c) {
        String sql = "INSERT INTO configuracion (id, nombre_empresa, ruc, direccion, telefono) " +
                "VALUES (1, ?, ?, ?, ?) " +
                "ON CONFLICT (id) DO UPDATE SET " +
                "nombre_empresa = EXCLUDED.nombre_empresa, " +
                "ruc = EXCLUDED.ruc, " +
                "direccion = EXCLUDED.direccion, " +
                "telefono = EXCLUDED.telefono";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, c.getNombreEmpresa());
            ps.setString(2, c.getRuc());
            ps.setString(3, c.getDireccion());
            ps.setString(4, c.getTelefono());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al guardar configuración: " + e.getMessage());
            return false;
        }
    }
}