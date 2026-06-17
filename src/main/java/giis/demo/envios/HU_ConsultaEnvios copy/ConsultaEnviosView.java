package giis.demo.envios.HU_ConsultaEnvios;

import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import java.awt.*;

public class ConsultaEnviosView extends JPanel {
    private JTable tabMisEnvios;
    private JButton btnConsultarDetalle;
    private JTextArea txtDetalleEnvioCliente;
    private JTable tabHistorialCliente;

    public ConsultaEnviosView() {
        setLayout(new MigLayout("wrap 1, fill", "[grow]", "[40%][grow]"));

        JPanel topPanel = new JPanel(new MigLayout("fill", "[grow][]", "[grow]"));
        topPanel.setBorder(BorderFactory.createTitledBorder("Lista de Envíos Realizados"));

        tabMisEnvios = new JTable();
        tabMisEnvios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollTable = new JScrollPane(tabMisEnvios);
        topPanel.add(scrollTable, "grow, push");

        btnConsultarDetalle = new JButton("Consultar Detalles e Historial");
        btnConsultarDetalle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        topPanel.add(btnConsultarDetalle, "top, wrap");
        add(topPanel, "grow");

        JPanel bottomPanel = new JPanel(new MigLayout("fill", "[30%][70%]", "[grow]"));
        bottomPanel.setBorder(BorderFactory.createTitledBorder("Detalle del Envío y Seguimiento de Movimientos"));

        txtDetalleEnvioCliente = new JTextArea();
        txtDetalleEnvioCliente.setEditable(false);
        txtDetalleEnvioCliente.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        bottomPanel.add(new JScrollPane(txtDetalleEnvioCliente), "grow");

        tabHistorialCliente = new JTable();
        bottomPanel.add(new JScrollPane(tabHistorialCliente), "grow");

        add(bottomPanel, "grow");
    }

    public JTable getTabMisEnvios() { return tabMisEnvios; }
    public JButton getBtnConsultarDetalle() { return btnConsultarDetalle; }
    public JTextArea getTxtDetalleEnvioCliente() { return txtDetalleEnvioCliente; }
    public JTable getTabHistorialCliente() { return tabHistorialCliente; }
}
