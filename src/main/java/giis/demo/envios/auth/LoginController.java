package giis.demo.envios.auth;

import java.util.List;
import javax.swing.JOptionPane;
import giis.demo.envios.entity.UsuarioDTO;
import giis.demo.envios.main.DashboardMainController;
import giis.demo.envios.main.DashboardMainView;

import giis.demo.util.ApplicationException;
import giis.demo.util.Database;
import giis.demo.util.SwingUtil;

public class LoginController {
    private Database db;
    private LoginView view;

    public LoginController(Database db, LoginView view) {
        this.db = db;
        this.view = view;
        
        try {
            String checkSql = "SELECT count(*) FROM Usuarios_Sistema";
            List<Object[]> res = db.executeQueryArray(checkSql);
            if (res.isEmpty() || ((Number) res.get(0)[0]).intValue() == 0) {
                db.createDatabase(true);
                db.loadDatabase();
            }
        } catch (Exception e) {
            // Si la tabla no existe o hay error, inicializamos la base de datos
            db.createDatabase(true);
            db.loadDatabase();
        }

        initController();
    }

    private void initController() {
        view.getBtnLogin().addActionListener(e -> SwingUtil.exceptionWrapper(this::handleLogin));
        
        // Tecla Enter en el campo de texto
        view.getTxtDni().addActionListener(e -> SwingUtil.exceptionWrapper(this::handleLogin));
        
        view.getFrame().setVisible(true);
    }

    private void handleLogin() {
        String dni = view.getTxtDni().getText().trim();
        if (dni.isEmpty()) {
            throw new ApplicationException("Por favor, introduzca su DNI o CIF.");
        }

        String sql = "SELECT id, nombre, dni_nif as dniNif, email, password_hash as passwordHash, rol, numero_empleado as numeroEmpleado FROM Usuarios_Sistema WHERE dni_nif = ?";
        List<UsuarioDTO> usuarios = db.executeQueryPojo(UsuarioDTO.class, sql, dni);
        UsuarioDTO usuario = usuarios.isEmpty() ? null : usuarios.get(0);
        if (usuario == null) {
            throw new ApplicationException("Usuario no encontrado en el sistema con ese DNI/CIF.");
        }

        // Login exitoso
        JOptionPane.showMessageDialog(view.getFrame(), "Bienvenido, " + usuario.getNombre() + " (" + usuario.getRol() + ")", "Inicio de sesión correcto", JOptionPane.INFORMATION_MESSAGE);
        
        view.getFrame().dispose();
        
        DashboardMainView mainView = new DashboardMainView();
        new DashboardMainController(db, mainView, usuario);

        // Cerrar ventana de login
        view.getFrame().dispose();
    }
}
