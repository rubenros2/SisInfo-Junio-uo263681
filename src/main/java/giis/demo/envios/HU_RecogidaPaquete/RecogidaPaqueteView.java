package giis.demo.envios.HU_RecogidaPaquete;

import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import java.awt.*;

public class RecogidaPaqueteView extends JPanel {
    private JTable tabRecogidas;
    private JTextArea txtDetalleRecogida;
    private JButton btnConfirmarRecogida;

    public RecogidaPaqueteView() {
        setLayout(new MigLayout("wrap 1, fill", "[grow]", "[50%][grow]"));

        JPanel top = new JPanel(new MigLayout("fill", "[grow]", "[grow]"));
        top.setBorder(BorderFactory.createTitledBorder("Pendientes de Recogida"));
        tabRecogidas = new JTable();
        tabRecogidas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        top.add(new JScrollPane(tabRecogidas), "grow, push");
        add(top, "grow");

        JPanel bottom = new JPanel(new MigLayout("fill", "[grow][]", "[grow]"));
        bottom.setBorder(BorderFactory.createTitledBorder("Detalle de Recogida Seleccionada"));

        txtDetalleRecogida = new JTextArea();
        txtDetalleRecogida.setEditable(false);
        txtDetalleRecogida.setFont(new Font("Monospaced", Font.PLAIN, 12));
        bottom.add(new JScrollPane(txtDetalleRecogida), "grow, push");

        btnConfirmarRecogida = new JButton("Confirmar Recogida");
        btnConfirmarRecogida.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnConfirmarRecogida.setBackground(new Color(40, 150, 80));
        btnConfirmarRecogida.setForeground(Color.WHITE);
        btnConfirmarRecogida.setEnabled(false);
        bottom.add(btnConfirmarRecogida, "growx, bottom");

        add(bottom, "grow");
    }

    public JTable getTabRecogidas() { return tabRecogidas; }
    public JTextArea getTxtDetalleRecogida() { return txtDetalleRecogida; }
    public JButton getBtnConfirmarRecogida() { return btnConfirmarRecogida; }
}
