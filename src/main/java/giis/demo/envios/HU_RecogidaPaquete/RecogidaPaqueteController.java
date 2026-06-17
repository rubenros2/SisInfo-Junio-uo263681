package giis.demo.envios.HU_RecogidaPaquete;

import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.TableModel;
import giis.demo.envios.entity.EnvioDTO;
import giis.demo.envios.entity.UsuarioDTO;
import giis.demo.util.SwingUtil;

public class RecogidaPaqueteController {
    private RecogidaPaqueteModel model;
    private RecogidaPaqueteView view;
    private UsuarioDTO activeUser;

    public RecogidaPaqueteController(RecogidaPaqueteModel model, RecogidaPaqueteView view, UsuarioDTO user) {
        this.model = model;
        this.view = view;
        this.activeUser = user;
        this.initController();
    }

    private void initController() {
        view.getTabRecogidas().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                handleSelectionRecogida();
            }
        });
        view.getBtnConfirmarRecogida().addActionListener(e -> SwingUtil.exceptionWrapper(this::handleConfirmarRecogida));
    }

    public void loadData() {
        List<EnvioDTO> recogidas = model.getRecogidasAsignadas(activeUser.getId());
        TableModel trm = SwingUtil.getTableModelFromPojos(recogidas, new String[] { "numero_seguimiento", "remitente_nombre", "lugar_recogida", "tipo_entrega", "fecha_prevista_recogida" });
        view.getTabRecogidas().setModel(trm);
        SwingUtil.autoAdjustColumns(view.getTabRecogidas());
        view.getTxtDetalleRecogida().setText("");
        view.getBtnConfirmarRecogida().setEnabled(false);
    }

    private void handleSelectionRecogida() {
        int row = view.getTabRecogidas().getSelectedRow();
        if (row < 0) {
            view.getTxtDetalleRecogida().setText("");
            view.getBtnConfirmarRecogida().setEnabled(false);
            return;
        }

        String tracking = (String) view.getTabRecogidas().getValueAt(row, 0);
        EnvioDTO envio = model.getEnvio(tracking);
        if (envio != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("DATOS DE RECOGIDA - ").append(envio.getNumero_seguimiento()).append("\n");
            sb.append("=========================================\n");
            sb.append("Remitente (Recoger en): ").append(envio.getRemitente_nombre()).append(" (").append(envio.getRemitente_dni_cif()).append(")\n");
            sb.append("Dirección Recogida: ").append(envio.getLugar_recogida()).append("\n");
            sb.append("Fecha Prevista Recogida: ").append(envio.getFecha_prevista_recogida()).append("\n");
            sb.append("-----------------------------------------\n");
            sb.append("Destinatario: ").append(envio.getDestinatario_nombre()).append(" (").append(envio.getDestinatario_dni_cif()).append(")\n");
            sb.append("Dirección Entrega: ").append(envio.getLugar_entrega()).append("\n");
            sb.append("Tipo Entrega: ").append(envio.getTipo_entrega()).append("\n");
            sb.append("-----------------------------------------\n");
            sb.append("Paquete: ").append(envio.getCategoria_tamano()).append("\n");
            sb.append("=========================================\n");
            
            view.getTxtDetalleRecogida().setText(sb.toString());
            view.getBtnConfirmarRecogida().setEnabled(true);
        }
    }

    private void handleConfirmarRecogida() {
        int row = view.getTabRecogidas().getSelectedRow();
        if (row < 0) return;

        String tracking = (String) view.getTabRecogidas().getValueAt(row, 0);
        model.confirmarRecogida(tracking, activeUser.getId());
        
        JOptionPane.showMessageDialog(view, 
            "Recogida del paquete " + tracking + " confirmada correctamente.", 
            "Recogida Completada", 
            JOptionPane.INFORMATION_MESSAGE
        );
        
        loadData();
    }
}
