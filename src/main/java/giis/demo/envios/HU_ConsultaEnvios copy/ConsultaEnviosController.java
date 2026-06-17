package giis.demo.envios.HU_ConsultaEnvios;

import java.util.List;
import javax.swing.table.TableModel;
import giis.demo.envios.entity.EnvioDTO;
import giis.demo.envios.entity.HistorialDTO;
import giis.demo.envios.entity.UsuarioDTO;
import giis.demo.util.ApplicationException;
import giis.demo.util.SwingUtil;

public class ConsultaEnviosController {
    private ConsultaEnviosModel model;
    private ConsultaEnviosView view;
    private UsuarioDTO activeUser;

    public ConsultaEnviosController(ConsultaEnviosModel model, ConsultaEnviosView view, UsuarioDTO user) {
        this.model = model;
        this.view = view;
        this.activeUser = user;
        this.initController();
    }

    private void initController() {
        view.getBtnConsultarDetalle().addActionListener(e -> SwingUtil.exceptionWrapper(this::handleConsultarDetalleCliente));
    }

    public void loadData() {
        List<EnvioDTO> envios = model.getEnviosUsuario(activeUser.getId(), activeUser.getDniNif());
        TableModel tm = SwingUtil.getTableModelFromPojos(envios, new String[] { "numero_seguimiento", "fecha_creacion", "destinatario_nombre", "tipo_entrega", "estado_actual", "importe_total" });
        view.getTabMisEnvios().setModel(tm);
        SwingUtil.autoAdjustColumns(view.getTabMisEnvios());
        view.getTxtDetalleEnvioCliente().setText("");
        view.getTabHistorialCliente().setModel(new javax.swing.table.DefaultTableModel());
    }

    private void handleConsultarDetalleCliente() {
        int row = view.getTabMisEnvios().getSelectedRow();
        if (row < 0) {
            throw new ApplicationException("Por favor, seleccione un envío de la tabla para consultar su detalle.");
        }

        String tracking = (String) view.getTabMisEnvios().getValueAt(row, 0);
        EnvioDTO envio = model.getEnvio(tracking);
        if (envio == null) {
            throw new ApplicationException("No se encontró el envío.");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Detalle del Envío: ").append(envio.getNumero_seguimiento()).append("\n");
        sb.append("------------------------------------------\n");
        sb.append("Fecha Creación: ").append(envio.getFecha_creacion()).append("\n");
        sb.append("Remitente: ").append(envio.getRemitente_nombre()).append(" (").append(envio.getRemitente_dni_cif()).append(")\n");
        sb.append("Destinatario: ").append(envio.getDestinatario_nombre()).append(" (").append(envio.getDestinatario_dni_cif()).append(")\n");
        sb.append("Paquete: ").append(envio.getCategoria_tamano()).append("\n");
        sb.append("Estado Paquete: ").append(envio.getEstado_paquete()).append("\n");
        sb.append("Tipo Recogida: ").append(envio.getTipo_recogida()).append("\n");
        if (envio.getLugar_recogida() != null) {
            sb.append("Dirección Recogida: ").append(envio.getLugar_recogida()).append("\n");
        }
        sb.append("Tipo Entrega: ").append(envio.getTipo_entrega()).append("\n");
        sb.append("Dirección Entrega: ").append(envio.getLugar_entrega()).append("\n");
        sb.append(String.format("Importe Pagado: %.2f €\n", envio.getImporte_total()));
        sb.append("Estado Actual: ").append(envio.getEstado_actual()).append("\n");
        if (envio.getMotivo_incidencia() != null) {
            sb.append("Incidencia: ").append(envio.getMotivo_incidencia()).append("\n");
        }

        view.getTxtDetalleEnvioCliente().setText(sb.toString());

        List<HistorialDTO> hist = model.getHistorialEnvio(tracking);
        TableModel tm = SwingUtil.getTableModelFromPojos(hist, new String[] { "fecha_hora", "estado", "ubicacion", "observaciones", "responsable_nombre", "vehiculo_asociado" });
        view.getTabHistorialCliente().setModel(tm);
        SwingUtil.autoAdjustColumns(view.getTabHistorialCliente());
    }
}
