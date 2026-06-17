package giis.demo.envios.HU_LlegadaAlmacen;

import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import java.awt.*;

public class LlegadaAlmacenView extends JPanel {
    private JTextField txtLlegadaBusqueda;
    private JButton btnLlegadaBuscar;
    private JPanel panelDetalleLlegada;
    private JTextArea txtInfoLlegada;
    private JComboBox<String> comboLlegadaEstadoPkg;
    private JTextField txtLlegadaAlmacenActual;
    private JButton btnConfirmarLlegada;

    public LlegadaAlmacenView() {
        setLayout(new MigLayout("wrap 1, fillx", "[grow]"));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Buscar Envío"));
        searchPanel.add(new JLabel("Número de Seguimiento:"));
        txtLlegadaBusqueda = new JTextField(20);
        searchPanel.add(txtLlegadaBusqueda);
        btnLlegadaBuscar = new JButton("Buscar Envío");
        searchPanel.add(btnLlegadaBuscar);
        add(searchPanel, "growx");

        panelDetalleLlegada = new JPanel(new MigLayout("wrap 2, fillx", "[45%][55%]"));
        panelDetalleLlegada.setBorder(BorderFactory.createTitledBorder("Registro de Recepción en Almacén"));
        panelDetalleLlegada.setVisible(false);

        txtInfoLlegada = new JTextArea(8, 30);
        txtInfoLlegada.setEditable(false);
        txtInfoLlegada.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panelDetalleLlegada.add(new JScrollPane(txtInfoLlegada), "grow, span 2");

        panelDetalleLlegada.add(new JLabel("Ubicación Almacén Actual:"));
        txtLlegadaAlmacenActual = new JTextField("Madrid Central Almacén");
        panelDetalleLlegada.add(txtLlegadaAlmacenActual, "growx");

        panelDetalleLlegada.add(new JLabel("Estado Físico del Paquete:"));
        comboLlegadaEstadoPkg = new JComboBox<>(new String[] { "Correcto", "Dañado", "Abierto", "Mojado", "Otro" });
        panelDetalleLlegada.add(comboLlegadaEstadoPkg, "growx");

        btnConfirmarLlegada = new JButton("Confirmar Llegada / Registrar");
        btnConfirmarLlegada.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnConfirmarLlegada.setBackground(new Color(20, 100, 200));
        btnConfirmarLlegada.setForeground(Color.WHITE);
        panelDetalleLlegada.add(btnConfirmarLlegada, "growx, span 2, gaptop 10");

        add(panelDetalleLlegada, "growx");
    }

    public JTextField getTxtLlegadaBusqueda() { return txtLlegadaBusqueda; }
    public JButton getBtnLlegadaBuscar() { return btnLlegadaBuscar; }
    public JPanel getPanelDetalleLlegada() { return panelDetalleLlegada; }
    public JTextArea getTxtInfoLlegada() { return txtInfoLlegada; }
    public JComboBox<String> getComboLlegadaEstadoPkg() { return comboLlegadaEstadoPkg; }
    public JTextField getTxtLlegadaAlmacenActual() { return txtLlegadaAlmacenActual; }
    public JButton getBtnConfirmarLlegada() { return btnConfirmarLlegada; }
}
