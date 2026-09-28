package dao;

import conexion.ConexionBD;
import modelo.Entrega;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EntregaDAO {

    // Guarda la relación entre pedido y repartidor
    public boolean guardar(Entrega entrega) {
        String sql = "INSERT INTO entrega (id_pedido, id_repartidor, fecha, hora) VALUES (?, ?, ?, ?)";
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = ConexionBD.conectar();
            ps = con.prepareStatement(sql);
            ps.setInt(1, entrega.getIdPedido());
            ps.setInt(2, entrega.getIdRepartidor());
            ps.setDate(3, entrega.getFecha());
            ps.setTime(4, entrega.getHora());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al guardar entrega: " + e.getMessage());
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
}