package giis.demo.envios.main;

import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import java.awt.*;

public class DashboardMainView {
    private JFrame frame;
    private JLabel lblSesionInfo;
    private JTabbedPane tabbedPane;
    private JButton btnAbrirPortalExterno;

    public DashboardMainView() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Sistema de Gestión de Envíos");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(950, 700);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.setBackground(new Color(230, 240, 250));
        topPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.LIGHT_GRAY));

        JLabel lblSesion = new JLabel("Sesión activa:");
        lblSesion.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblSesionInfo = new JLabel("Cargando...");
        lblSesionInfo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSesionInfo.setForeground(new Color(20, 100, 200));
        topPanel.add(lblSesion);
        topPanel.add(lblSesionInfo);
        frame.getContentPane().add(topPanel, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
    }

    public JPanel crearTabPortalExterno() {
        JPanel panel = new JPanel(new MigLayout("wrap 1, align center, insets 50", "[grow, center]", "[]30[]"));

        JLabel lblInfo = new JLabel("<html><div style='text-align:center; font-size:14px;'>"
                + "Las empresas acceden al sistema mediante integraciones B2B (API Web).<br><br>"
                + "Haz clic en el botón inferior para abrir un simulador del <b>Portal Web Externo</b> en tu navegador.<br>"
                + "El portal enviará automáticamente las peticiones JSON a nuestra aplicación de escritorio."
                + "</div></html>");
        panel.add(lblInfo);

        btnAbrirPortalExterno = new JButton("Abrir Portal Web Externo");
        btnAbrirPortalExterno.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAbrirPortalExterno.setBackground(new Color(20, 100, 200));
        btnAbrirPortalExterno.setForeground(Color.WHITE);
        btnAbrirPortalExterno.setOpaque(true);
        btnAbrirPortalExterno.setBorderPainted(false);
        btnAbrirPortalExterno.setPreferredSize(new Dimension(250, 50));
        panel.add(btnAbrirPortalExterno);

        return panel;
    }

    public JFrame getFrame() { return frame; }
    public JLabel getLblSesionInfo() { return lblSesionInfo; }
    public JTabbedPane getTabbedPane() { return tabbedPane; }
    public JButton getBtnAbrirPortalExterno() { return btnAbrirPortalExterno; }
}
