package vista;

import dao.PedidoDAO;
import dao.RepartidorDAO;
import modelo.Pedido;
import modelo.Repartidor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentanaPrincipal extends JFrame {

    private final PedidoDAO pedidoDAO = new PedidoDAO();
    private final RepartidorDAO repartidorDAO = new RepartidorDAO();

    // Componentes del formulario Repartidor
    private JTextField txtNombreRepartidor;
    private JButton btnGuardarRepartidor;

    // Componentes del formulario Pedido
    private JTextField txtDireccion;
    private JComboBox<String> cbTipo;
    private JComboBox<String> cbEstado;
    private JButton btnGuardarPedido;

    // Componentes para la visualización en base de datos
    private JTable tablaPedidos;
    private DefaultTableModel modeloTabla;
    private JButton btnActualizarTabla;

    public VentanaPrincipal() {
        setTitle("SpeedFast - Gestión de Pedidos y Repartidores");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Panel Superior: Contenedor de Formularios
        JPanel panelFormularios = new JPanel(new GridLayout(1, 2, 10, 10));
        panelFormularios.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 1. Formulario Repartidor
        JPanel panelRepartidor = new JPanel(new GridLayout(3, 2, 8, 8));
        panelRepartidor.setBorder(BorderFactory.createTitledBorder("Registrar Repartidor"));
        panelRepartidor.add(new JLabel("Nombre:"));
        txtNombreRepartidor = new JTextField();
        panelRepartidor.add(txtNombreRepartidor);
        btnGuardarRepartidor = new JButton("Guardar Repartidor");
        panelRepartidor.add(new JLabel(""));
        panelRepartidor.add(btnGuardarRepartidor);

        // 2. Formulario Pedido
        JPanel panelPedido = new JPanel(new GridLayout(4, 2, 8, 8));
        panelPedido.setBorder(BorderFactory.createTitledBorder("Registrar Pedido"));
        panelPedido.add(new JLabel("Dirección:"));
        txtDireccion = new JTextField();
        panelPedido.add(txtDireccion);

        panelPedido.add(new JLabel("Tipo:"));
        cbTipo = new JComboBox<>(new String[]{"COMIDA", "ENCOMIENDA", "EXPRESS"});
        panelPedido.add(cbTipo);

        panelPedido.add(new JLabel("Estado:"));
        cbEstado = new JComboBox<>(new String[]{"PENDIENTE", "EN_REPARTO", "ENTREGADO"});
        panelPedido.add(cbEstado);

        btnGuardarPedido = new JButton("Guardar Pedido");
        panelPedido.add(new JLabel(""));
        panelPedido.add(btnGuardarPedido);

        panelFormularios.add(panelRepartidor);
        panelFormularios.add(panelPedido);
        add(panelFormularios, BorderLayout.NORTH);

        // Panel Centro: Visualización en JTable
        JPanel panelCentro = new JPanel(new BorderLayout(5, 5));
        panelCentro.setBorder(BorderFactory.createTitledBorder("Listado de Pedidos en Base de Datos"));

        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Dirección", "Tipo", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Solo lectura
            }
        };
        tablaPedidos = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaPedidos);
        panelCentro.add(scrollPane, BorderLayout.CENTER);

        btnActualizarTabla = new JButton("Recargar Pedidos");
        panelCentro.add(btnActualizarTabla, BorderLayout.SOUTH);
        add(panelCentro, BorderLayout.CENTER);

        // Asignación de acciones a los botones
        configurarEventos();

        // Consulta inicial de datos en la base de datos
        cargarTablaPedidos();
    }

    private void configurarEventos() {
        // Registro directo en MySQL: Repartidor
        btnGuardarRepartidor.addActionListener(e -> {
            String nombre = txtNombreRepartidor.getText().trim();
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe ingresar el nombre del repartidor.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Repartidor r = new Repartidor(nombre);
            if (repartidorDAO.guardar(r)) {
                JOptionPane.showMessageDialog(this, "Repartidor registrado en la base de datos con éxito.");
                txtNombreRepartidor.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar el repartidor en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Registro directo en MySQL: Pedido
        btnGuardarPedido.addActionListener(e -> {
            String direccion = txtDireccion.getText().trim();
            String tipo = (String) cbTipo.getSelectedItem();
            String estado = (String) cbEstado.getSelectedItem();

            if (direccion.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe ingresar la dirección del pedido.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Pedido p = new Pedido(direccion, tipo, estado);
            if (pedidoDAO.guardar(p)) {
                JOptionPane.showMessageDialog(this, "Pedido guardado correctamente en la base de datos.");
                txtDireccion.setText("");
                cargarTablaPedidos(); // Actualiza la JTable en tiempo real
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar el pedido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Botón para refrescar la tabla manualmente
        btnActualizarTabla.addActionListener(e -> cargarTablaPedidos());
    }

    // Consulta en MySQL y carga los datos en la JTable
    private void cargarTablaPedidos() {
        modeloTabla.setRowCount(0);
        List<Pedido> pedidos = pedidoDAO.listarTodos();
        for (Pedido p : pedidos) {
            modeloTabla.addRow(new Object[]{
                    p.getId(),
                    p.getDireccion(),
                    p.getTipo(),
                    p.getEstado()
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VentanaPrincipal().setVisible(true);
        });
    }
}