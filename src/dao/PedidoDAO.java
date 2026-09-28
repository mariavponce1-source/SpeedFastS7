package dao;

import conexion.ConexionBD;
import modelo.Pedido;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    // Guarda el pedido usando PreparedStatement
    public boolean guardar(Pedido pedido) {
        String sql = "INSERT INTO pedido (direccion, tipo, estado) VALUES (?, ?, ?)";
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = ConexionBD.conectar();
            ps = con.prepareStatement(sql);
            ps.setString(1, pedido.getDireccion());
            ps.setString(2, pedido.getTipo());
            ps.setString(3, pedido.getEstado());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al guardar pedido: " + e.getMessage());
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

    // Consulta todos los pedidos para la tabla gráfica
    public List<Pedido> listarTodos() {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT id, direccion, tipo, estado FROM pedido";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConexionBD.conectar();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Pedido p = new Pedido(
                        rs.getInt("id"),
                        rs.getString("direccion"),
                        rs.getString("tipo"),
                        rs.getString("estado")
                );
                lista.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar pedidos: " + e.getMessage());
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