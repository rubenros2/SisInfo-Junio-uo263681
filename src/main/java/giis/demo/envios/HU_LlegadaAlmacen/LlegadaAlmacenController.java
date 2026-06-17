package giis.demo.envios.HU_LlegadaAlmacen;

import java.util.List;
import javax.swing.JOptionPane;
import giis.demo.envios.entity.EnvioDTO;
import giis.demo.envios.entity.HistorialDTO;
import giis.demo.envios.entity.UsuarioDTO;
import giis.demo.util.ApplicationException;
import giis.demo.util.SwingUtil;

public class LlegadaAlmacenController {
    private LlegadaAlmacenModel model;
    private LlegadaAlmacenView view;
    private UsuarioDTO activeUser;
    private Runnable onEnvioActualizado;

    public LlegadaAlmacenController(LlegadaAlmacenModel model, LlegadaAlmacenView view, UsuarioDTO user) {
        this.model = model;
        this.view = view;
        this.activeUser = user;
        this.initController();
    }

    public void setOnEnvioActualizado(Runnable callback) {
        this.onEnvioActualizado = callback;
    }

    private void initController() {
        view.getBtnLlegadaBuscar().addActionListener(e -> SwingUtil.exceptionWrapper(this::handleLlegadaBuscar));
        view.getBtnConfirmarLlegada().addActionListener(e -> SwingUtil.exceptionWrapper(this::handleConfirmarLlegada));
    }

    private void handleLlegadaBuscar() {
        String tracking = view.getTxtLlegadaBusqueda().getText().trim();
        if (tracking.isEmpty()) {
            throw new ApplicationException("Por favor, introduzca un número de seguimiento.");
        }

        EnvioDTO envio = model.getEnvio(tracking);
        if (envio == null) {
            view.getPanelDetalleLlegada().setVisible(false);
            throw new ApplicationException("No se encontró ningún envío con el código: " + tracking);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Envío Encontrado: ").append(envio.getNumero_seguimiento()).append("\n");
        sb.append("Remitente: ").append(envio.getRemitente_nombre()).append("\n");
        sb.append("Destinatario: ").append(envio.getDestinatario_nombre()).append("\n");
        sb.append("Estado Actual del Envío: ").append(envio.getEstado_actual()).append("\n");
        
        List<HistorialDTO> hist = model.getHistorialEnvio(tracking);
        String ultUbicacion = hist.isEmpty() ? "Desconocida" : hist.get(hist.size() - 1).getUbicacion();
        sb.append("Última Ubicación Conocida: ").append(ultUbicacion);

        view.getTxtInfoLlegada().setText(sb.toString());
        view.getComboLlegadaEstadoPkg().setSelectedItem("Correcto");
        view.getPanelDetalleLlegada().setVisible(true);
    }

    private void handleConfirmarLlegada() {
        String tracking = view.getTxtLlegadaBusqueda().getText().trim();
        String estadoPkg = view.getComboLlegadaEstadoPkg().getSelectedItem().toString();
        String almacenActual = view.getTxtLlegadaAlmacenActual().getText().trim();

        if (almacenActual.isEmpty()) {
            throw new ApplicationException("Debe especificar el nombre del almacén actual.");
        }

        boolean incidencia = model.confirmarLlegada(tracking, estadoPkg, almacenActual, activeUser.getId());

        if (incidencia) {
            JOptionPane.showMessageDialog(view,
                "¡INCIDENCIA DETECTADA EN EL PAQUETE " + tracking + "!\n" +
                "- Estado: " + estadoPkg + "\n\n" +
                "El envío ha pasado al estado 'Incidencia detectada'. Se ha enviado una notificación automática al remitente.",
                "Alerta de Incidencia",
                JOptionPane.WARNING_MESSAGE
            );
        } else {
            JOptionPane.showMessageDialog(view,
                "Llegada del paquete " + tracking + " confirmada con éxito en " + almacenActual + ".",
                "Llegada Confirmada",
                JOptionPane.INFORMATION_MESSAGE
            );
        }

        view.getPanelDetalleLlegada().setVisible(false);
        view.getTxtLlegadaBusqueda().setText("");
        
        if (onEnvioActualizado != null) {
            onEnvioActualizado.run();
        }
    }
}
