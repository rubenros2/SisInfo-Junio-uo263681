package giis.demo.envios.HU_SalidaAlmacen;

import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.TableModel;
import giis.demo.envios.entity.EnvioDTO;
import giis.demo.envios.entity.TramoDTO;
import giis.demo.envios.entity.UsuarioDTO;
import giis.demo.util.SwingUtil;

public class SalidaAlmacenController {
    private SalidaAlmacenModel model;
    private SalidaAlmacenView view;
    private UsuarioDTO activeUser;
    private Runnable onEnvioActualizado;

    public SalidaAlmacenController(SalidaAlmacenModel model, SalidaAlmacenView view, UsuarioDTO user) {
        this.model = model;
        this.view = view;
        this.activeUser = user;
        this.initController();
    }

    public void setOnEnvioActualizado(Runnable callback) {
        this.onEnvioActualizado = callback;
    }

    private void initController() {
        view.getComboSalidaAlmacenActual().addActionListener(e -> SwingUtil.exceptionWrapper(this::handleSalidasAlmacenChange));
        view.getTabEnviosDisponiblesSalida().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                handleSelectionSalida();
            }
        });
        view.getBtnConfirmarSalida().addActionListener(e -> SwingUtil.exceptionWrapper(this::handleConfirmarSalida));
    }

    public void loadData() {
        handleSalidasAlmacenChange();
    }

    private void handleSalidasAlmacenChange() {
        String almacen = view.getComboSalidaAlmacenActual().getSelectedItem().toString();
        List<EnvioDTO> envios = model.getEnviosDisponiblesSalida(almacen);
        
        TableModel tm = SwingUtil.getTableModelFromPojos(envios, new String[] { "numero_seguimiento", "estado_actual", "estado_paquete" });
        view.getTabEnviosDisponiblesSalida().setModel(tm);
        SwingUtil.autoAdjustColumns(view.getTabEnviosDisponiblesSalida());
        
        view.getTxtDetalleSalida().setText("");
        view.getBtnConfirmarSalida().setEnabled(false);
    }

    private void handleSelectionSalida() {
        int row = view.getTabEnviosDisponiblesSalida().getSelectedRow();
        if (row < 0) {
            view.getTxtDetalleSalida().setText("");
            view.getBtnConfirmarSalida().setEnabled(false);
            return;
        }

        String tracking = (String) view.getTabEnviosDisponiblesSalida().getValueAt(row, 0);
        EnvioDTO envio = model.getEnvio(tracking);
        String almacenActual = view.getComboSalidaAlmacenActual().getSelectedItem().toString();

        if (envio != null) {
            List<TramoDTO> tramos = model.getTramosEnvio(tracking);
            TramoDTO proxTramo = null;
            for (TramoDTO t : tramos) {
                if (almacenActual.equals(t.getOrigen())) {
                    proxTramo = t;
                    break;
                }
            }

            StringBuilder sb = new StringBuilder();
            sb.append("EXPEDICIÓN DE ENVÍO - ").append(envio.getNumero_seguimiento()).append("\n");
            sb.append("=========================================\n");
            sb.append("Estado Paquete: ").append(envio.getEstado_paquete()).append("\n");
            
            boolean vehiculoAsignado = false;
            boolean noIncidencias = !"Incidencia detectada".equals(envio.getEstado_actual());

            if (proxTramo != null) {
                sb.append("Próximo Destino: ").append(proxTramo.getDestino()).append("\n");
                sb.append("Vehículo Asignado (Matrícula): ").append(proxTramo.getVehiculo_matricula() != null ? proxTramo.getVehiculo_matricula() : "No asignado").append("\n");
                sb.append("ID Transportista Asignado: ").append(proxTramo.getTransportista_id() != null ? proxTramo.getTransportista_id() : "No asignado").append("\n");
                
                if (proxTramo.getVehiculo_matricula() != null && !proxTramo.getVehiculo_matricula().trim().isEmpty()) {
                    vehiculoAsignado = true;
                }
            } else {
                sb.append("¡ADVERTENCIA: No hay ningún tramo de ruta planificado que salga de este almacén para este paquete!\n");
            }
            sb.append("=========================================\n");
            sb.append("Verificaciones de Seguridad:\n");
            sb.append("- Físicamente disponible: SÍ\n");
            sb.append("- Sin incidencias abiertas: ").append(noIncidencias ? "SÍ" : "NO (Paquete bloqueado)").append("\n");
            sb.append("- Vehículo asignado: ").append(vehiculoAsignado ? "SÍ" : "NO").append("\n");

            view.getTxtDetalleSalida().setText(sb.toString());
            
            view.getBtnConfirmarSalida().setEnabled(vehiculoAsignado && noIncidencias && proxTramo != null);
        }
    }

    private void handleConfirmarSalida() {
        int row = view.getTabEnviosDisponiblesSalida().getSelectedRow();
        if (row < 0) return;

        String tracking = (String) view.getTabEnviosDisponiblesSalida().getValueAt(row, 0);
        String almacenActual = view.getComboSalidaAlmacenActual().getSelectedItem().toString();

        model.confirmarSalida(tracking, almacenActual, activeUser.getId());

        JOptionPane.showMessageDialog(view,
            "Salida autorizada. El paquete " + tracking + " ha sido expedido del almacén y está en estado 'En tránsito'.",
            "Salida Confirmada",
            JOptionPane.INFORMATION_MESSAGE
        );

        loadData();
        
        if (onEnvioActualizado != null) {
            onEnvioActualizado.run();
        }
    }
}
