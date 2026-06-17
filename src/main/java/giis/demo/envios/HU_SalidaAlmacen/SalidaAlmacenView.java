package giis.demo.envios.HU_SalidaAlmacen;

import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import java.awt.*;

public class SalidaAlmacenView extends JPanel {
    private JComboBox<String> comboSalidaAlmacenActual;
    private JTable tabEnviosDisponiblesSalida;
    private JTextArea txtDetalleSalida;
    private JButton btnConfirmarSalida;

    public SalidaAlmacenView() {
        setLayout(new MigLayout("wrap 1, fill", "[grow]", "[][60%][grow]"));

        JPanel filter = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        filter.setBorder(BorderFactory.createTitledBorder("Seleccionar Almacén de Expedición"));
        filter.add(new JLabel("Almacén Actual:"));
        comboSalidaAlmacenActual = new JComboBox<>(new String[] {
                "Madrid Central Almacén",
                "Sevilla Almacén",
                "Calle Mayor 10, Madrid"
        });
        filter.add(comboSalidaAlmacenActual);
        add(filter, "growx");

        JPanel mid = new JPanel(new MigLayout("fill", "[grow]", "[grow]"));
        mid.setBorder(BorderFactory.createTitledBorder("Envíos Disponibles para Salida"));
        tabEnviosDisponiblesSalida = new JTable();
        tabEnviosDisponiblesSalida.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mid.add(new JScrollPane(tabEnviosDisponiblesSalida), "grow");
        add(mid, "grow");

        JPanel bottom = new JPanel(new MigLayout("fill", "[grow][]", "[grow]"));
        bottom.setBorder(BorderFactory.createTitledBorder("Detalle del Tramo Siguiente"));
        txtDetalleSalida = new JTextArea();
        txtDetalleSalida.setEditable(false);
        txtDetalleSalida.setFont(new Font("Monospaced", Font.PLAIN, 12));
        bottom.add(new JScrollPane(txtDetalleSalida), "grow, push");

        btnConfirmarSalida = new JButton("Confirmar Salida (Expedir)");
        btnConfirmarSalida.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnConfirmarSalida.setBackground(new Color(40, 150, 80));
        btnConfirmarSalida.setForeground(Color.WHITE);
        btnConfirmarSalida.setEnabled(false);
        bottom.add(btnConfirmarSalida, "growx, bottom");
        add(bottom, "grow");
    }

    public JComboBox<String> getComboSalidaAlmacenActual() { return comboSalidaAlmacenActual; }
    public JTable getTabEnviosDisponiblesSalida() { return tabEnviosDisponiblesSalida; }
    public JTextArea getTxtDetalleSalida() { return txtDetalleSalida; }
    public JButton getBtnConfirmarSalida() { return btnConfirmarSalida; }
}
