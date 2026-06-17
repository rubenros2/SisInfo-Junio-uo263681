package giis.demo.envios.auth;

import javax.swing.*;
import java.awt.*;
import net.miginfocom.swing.MigLayout;

public class LoginView {
    private JFrame frame;
    private JTextField txtDni;
    private JButton btnLogin;

    public LoginView() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Sistema de Gestión de Envíos - Inicio de Sesión");
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setLayout(new BorderLayout());

        JPanel panelCentro = new JPanel(new MigLayout("wrap 1, align center, insets 20", "[grow, center]", "[]20[]20[]"));
        
        JLabel lblTitulo = new JLabel("<html><center><h2>Bienvenido</h2>Gestiona todos tus envíos desde una sola plataforma.</center></html>");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panelCentro.add(lblTitulo);

        JPanel panelForm = new JPanel(new MigLayout("wrap 2", "[][200!]", "[]"));
        panelForm.add(new JLabel("DNI / CIF:"));
        txtDni = new JTextField();
        panelForm.add(txtDni, "growx");
        panelCentro.add(panelForm);

        btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setBackground(new Color(20, 100, 200));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setOpaque(true);
        btnLogin.setBorderPainted(false);
        panelCentro.add(btnLogin);

        // Ayuda
        JLabel lblAyuda = new JLabel("<html><div style='text-align: center;'><small>Usuarios de prueba:<br>Cliente: 12345678A | Empresa: B12345678<br>Transportista: 87654321B | Operario: 56781234C</small></div></html>");
        lblAyuda.setForeground(Color.GRAY);
        panelCentro.add(lblAyuda, "gaptop 10");

        frame.getContentPane().add(panelCentro, BorderLayout.CENTER);
    }

    public JFrame getFrame() { return frame; }
    public JTextField getTxtDni() { return txtDni; }
    public JButton getBtnLogin() { return btnLogin; }
}
