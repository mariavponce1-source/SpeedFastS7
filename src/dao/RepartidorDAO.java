package dao;

import conexion.ConexionBD;
import modelo.Repartidor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RepartidorDAO {

    public boolean guardar(Repartidor repartidor) {
        String sql = "INSERT INTO repartidor (nombre) VALUES (?)";
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = ConexionBD.conectar();
            ps = con.prepareStatement(sql);
            ps.setString(1, repartidor.getNombre());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al guardar repartidor: " + e.getMessage());
            return false;
        } finally {
            try {
                if (ps != null) ps.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar PreparedStatement: " + e.getMessage());
            }
            ConexionBD.cerrarConexion(con);
        }
    }

    // Método listarTodos() que devuelve List<Repartidor> con ResultSet
    public List<Repartidor> listarTodos() {
        List<Repartidor> lista = new ArrayList<>();
        String sql = "SELECT id, nombre FROM repartidor";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConexionBD.conectar();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Repartidor r = new Repartidor(
                        rs.getInt("id"),
                        rs.getString("nombre")
                );
                lista.add(r);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar repartidores: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos: " + e.getMessage());
            }
            ConexionBD.cerrarConexion(con);
        }
        return lista;
    }
}