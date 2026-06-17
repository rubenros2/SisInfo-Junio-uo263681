package giis.demo.envios.HU_SolicitarEnvio;

import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import java.awt.*;

public class SolicitarEnvioView extends JPanel {
    private JTextField txtDestNombre;
    private JTextField txtDestDni;
    private JComboBox<String> comboDestTipo;
    private JComboBox<String> comboPkgTamano;

    private JComboBox<String> comboTipoRecogida;
    private JTextField txtLugarRecogida;
    private JComboBox<String> comboTipoEntrega;
    private JTextField txtLugarEntrega;
    private JButton btnCalcularCoste;
    private JLabel lblCosteCalculado;
    private JButton btnConfirmarContratar;
    private JTextArea txtResumenContrato;

    public SolicitarEnvioView() {
        setLayout(new MigLayout("wrap 2, fillx", "[35%][65%]"));

        JPanel formPanel = new JPanel(new MigLayout("wrap 2, fillx", "[35%][65%]"));
        formPanel.setBorder(BorderFactory.createTitledBorder("Datos del Envío"));

        formPanel.add(new JLabel("Destinatario Nombre:"));
        txtDestNombre = new JTextField(20);
        formPanel.add(txtDestNombre, "growx");

        formPanel.add(new JLabel("Destinatario DNI/CIF:"));
        txtDestDni = new JTextField(20);
        formPanel.add(txtDestDni, "growx");

        formPanel.add(new JLabel("Destinatario Tipo:"));
        comboDestTipo = new JComboBox<>(new String[] { "Particular", "Empresa" });
        formPanel.add(comboDestTipo, "growx");

        formPanel.add(new JLabel("Categoría de Tamaño:"));
        comboPkgTamano = new JComboBox<>(
                new String[] { "Pequeño (hasta 15x25 cm)", "Mediano (hasta 30x50 cm)", "Grande" });
        formPanel.add(comboPkgTamano, "growx");

        formPanel.add(new JLabel("Tipo Recogida:"));
        comboTipoRecogida = new JComboBox<>(
                new String[] { "Recogida en domicilio", "Entrega en oficina/punto de recogida" });
        formPanel.add(comboTipoRecogida, "growx");

        formPanel.add(new JLabel("Lugar de Recogida (dirección):"));
        txtLugarRecogida = new JTextField(20);
        formPanel.add(txtLugarRecogida, "growx");

        formPanel.add(new JLabel("Tipo Entrega:"));
        comboTipoEntrega = new JComboBox<>(
                new String[] { "Entrega a domicilio", "Entrega en oficina/punto de recogida" });
        formPanel.add(comboTipoEntrega, "growx");

        formPanel.add(new JLabel("Lugar de Entrega (dirección):"));
        txtLugarEntrega = new JTextField(20);
        formPanel.add(txtLugarEntrega, "growx");

        add(formPanel, "grow, top");

        JPanel rightPanel = new JPanel(new MigLayout("wrap 1, fillx", "[grow]"));
        rightPanel.setBorder(BorderFactory.createTitledBorder("Precio y Contratación"));

        btnCalcularCoste = new JButton("Calcular Coste de Envío");
        btnCalcularCoste.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCalcularCoste.setBackground(new Color(40, 150, 80));
        btnCalcularCoste.setForeground(Color.WHITE);
        rightPanel.add(btnCalcularCoste, "growx");

        lblCosteCalculado = new JLabel("Importe Estimado: -- €");
        lblCosteCalculado.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblCosteCalculado.setForeground(new Color(200, 50, 50));
        lblCosteCalculado.setHorizontalAlignment(SwingConstants.CENTER);
        rightPanel.add(lblCosteCalculado, "growx, gaptop 10");

        rightPanel.add(new JLabel("Justificante y Resumen de Contratación:"), "gaptop 10");
        txtResumenContrato = new JTextArea(10, 30);
        txtResumenContrato.setEditable(false);
        txtResumenContrato.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollResumen = new JScrollPane(txtResumenContrato);
        rightPanel.add(scrollResumen, "grow, push");

        btnConfirmarContratar = new JButton("Aceptar Importe y Contratar");
        btnConfirmarContratar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnConfirmarContratar.setBackground(new Color(20, 100, 200));
        btnConfirmarContratar.setForeground(Color.WHITE);
        btnConfirmarContratar.setEnabled(false);
        rightPanel.add(btnConfirmarContratar, "growx, gaptop 10");

        add(rightPanel, "grow");
    }

    public JTextField getTxtDestNombre() { return txtDestNombre; }
    public JTextField getTxtDestDni() { return txtDestDni; }
    public JComboBox<String> getComboDestTipo() { return comboDestTipo; }
    public JComboBox<String> getComboPkgTamano() { return comboPkgTamano; }
    public JComboBox<String> getComboTipoRecogida() { return comboTipoRecogida; }
    public JTextField getTxtLugarRecogida() { return txtLugarRecogida; }
    public JComboBox<String> getComboTipoEntrega() { return comboTipoEntrega; }
    public JTextField getTxtLugarEntrega() { return txtLugarEntrega; }
    public JButton getBtnCalcularCoste() { return btnCalcularCoste; }
    public JLabel getLblCosteCalculado() { return lblCosteCalculado; }
    public JButton getBtnConfirmarContratar() { return btnConfirmarContratar; }
    public JTextArea getTxtResumenContrato() { return txtResumenContrato; }
}
