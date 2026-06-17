package giis.demo.util;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Punto de entrada principal que inicializa el sistema y lanza el panel de
 * control (Dashboard).
 */
public class SwingMain {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SwingMain window = new SwingMain();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SwingMain() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Sistema Logístico - Lanzador de Aplicación");
		frame.setBounds(100, 100, 350, 250);
		frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		frame.setLocationRelativeTo(null);

		JButton btnEjecutarDashboard = new JButton("Iniciar Panel Logística (Dashboard)");
		btnEjecutarDashboard.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
		btnEjecutarDashboard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SwingUtil.exceptionWrapper(() -> {
					Database db = new Database();
					giis.demo.envios.auth.LoginView view = new giis.demo.envios.auth.LoginView();
					new giis.demo.envios.auth.LoginController(db, view);
					frame.dispose(); // Cierra el lanzador al iniciar
				});
			}
		});
		frame.getContentPane().add(btnEjecutarDashboard);

		JButton btnInicializarBaseDeDatos = new JButton("Inicializar Base de Datos en Blanco");
		btnInicializarBaseDeDatos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Database db = new Database();
				db.createDatabase(false);
				javax.swing.JOptionPane.showMessageDialog(frame, "Base de datos inicializada (Tablas vacías creadas)");
			}
		});
		frame.getContentPane().add(btnInicializarBaseDeDatos);

		JButton btnCargarDatosIniciales = new JButton("Cargar Datos Iniciales para Pruebas");
		btnCargarDatosIniciales.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Database db = new Database();
				db.createDatabase(false);
				db.loadDatabase();
				javax.swing.JOptionPane.showMessageDialog(frame, "Datos iniciales de prueba cargados correctamente");
			}
		});
		frame.getContentPane().add(btnCargarDatosIniciales);
	}

	public JFrame getFrame() {
		return this.frame;
	}

}
