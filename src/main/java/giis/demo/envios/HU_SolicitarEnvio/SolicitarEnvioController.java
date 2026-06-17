package giis.demo.envios.HU_SolicitarEnvio;

import java.util.Calendar;
import javax.swing.JOptionPane;
import giis.demo.envios.entity.EnvioDTO;
import giis.demo.envios.entity.TarifaDTO;
import giis.demo.envios.entity.TramoDTO;
import giis.demo.envios.entity.UsuarioDTO;
import giis.demo.util.ApplicationException;
import giis.demo.util.SwingUtil;
import giis.demo.util.Util;

public class SolicitarEnvioController {
    private SolicitarEnvioModel model;
    private SolicitarEnvioView view;
    private UsuarioDTO activeUser;
    private double calculatedCost = 0.0;
    private Runnable onEnvioCreado;

    public SolicitarEnvioController(SolicitarEnvioModel model, SolicitarEnvioView view, UsuarioDTO user) {
        this.model = model;
        this.view = view;
        this.activeUser = user;
        this.initController();
    }

    public void setOnEnvioCreado(Runnable callback) {
        this.onEnvioCreado = callback;
    }

    private void initController() {
        view.getTxtLugarRecogida().setText("Calle Alcalá 15, Madrid");
        view.getTxtLugarEntrega().setText("Calle Uría 4, Oviedo");

        view.getBtnCalcularCoste().addActionListener(e -> SwingUtil.exceptionWrapper(this::handleCalcularCoste));
        view.getBtnConfirmarContratar().addActionListener(e -> SwingUtil.exceptionWrapper(this::handleConfirmarContratar));

        view.getComboTipoRecogida().addActionListener(e -> {
            boolean aDomicilio = "Recogida en domicilio".equals(view.getComboTipoRecogida().getSelectedItem());
            view.getTxtLugarRecogida().setEnabled(aDomicilio);
            if (!aDomicilio) {
                view.getTxtLugarRecogida().setText("");
            } else {
                view.getTxtLugarRecogida().setText("Calle Alcalá 15, Madrid");
            }
        });
    }

    private void handleCalcularCoste() {
        String destNombre = view.getTxtDestNombre().getText().trim();
        String destDni = view.getTxtDestDni().getText().trim();
        String tipoRecogida = view.getComboTipoRecogida().getSelectedItem().toString();
        String lugarRecogida = view.getTxtLugarRecogida().getText().trim();
        String tipoEntrega = view.getComboTipoEntrega().getSelectedItem().toString();
        String lugarEntrega = view.getTxtLugarEntrega().getText().trim();
        String sizeCategory = view.getComboPkgTamano().getSelectedItem().toString();

        if (destNombre.isEmpty() || destDni.isEmpty() || lugarEntrega.isEmpty()) {
            throw new ApplicationException("Por favor, rellene todos los campos obligatorios del destinatario, paquete y entrega.");
        }
        if ("Recogida en domicilio".equals(tipoRecogida) && lugarRecogida.isEmpty()) {
            throw new ApplicationException("Debe especificar la dirección de recogida si solicita recogida a domicilio.");
        }

        String categoriaQuery = sizeCategory.split(" ")[0];
        TarifaDTO tarifa = model.getTarifa(categoriaQuery);
        calculatedCost = tarifa.getPrecio();
        
        view.getLblCosteCalculado().setText(String.format("Importe Estimado: %.2f €", calculatedCost));

        StringBuilder sb = new StringBuilder();
        sb.append("============= RESUMEN DE CONTRATACIÓN =============\n");
        sb.append("Remitente: ").append(activeUser.getNombre()).append(" (").append(activeUser.getDniNif()).append(")\n");
        sb.append("Destinatario: ").append(destNombre).append(" (").append(destDni).append(" - ").append(view.getComboDestTipo().getSelectedItem()).append(")\n");
        sb.append("Paquete: ").append(sizeCategory).append("\n");
        sb.append("Recogida: ").append(tipoRecogida);
        if ("Recogida en domicilio".equals(tipoRecogida)) {
            sb.append(" en ").append(lugarRecogida);
        }
        sb.append("\nEntrega: ").append(tipoEntrega).append(" en ").append(lugarEntrega).append("\n");
        sb.append("---------------------------------------------------\n");
        sb.append(String.format("IMPORTE TOTAL CALCULADO: %.2f €\n", calculatedCost));
        sb.append("===================================================\n");
        
        view.getTxtResumenContrato().setText(sb.toString());
        view.getBtnConfirmarContratar().setEnabled(true);
    }

    private void handleConfirmarContratar() {
        if (calculatedCost <= 0.0) return;

        String tracking = "TRK-" + System.currentTimeMillis();
        EnvioDTO envio = new EnvioDTO();
        envio.setNumero_seguimiento(tracking);
        envio.setUsuario_creador_id(activeUser.getId());
        envio.setRemitente_nombre(activeUser.getNombre());
        envio.setRemitente_dni_cif(activeUser.getDniNif());
        envio.setDestinatario_tipo(view.getComboDestTipo().getSelectedItem().toString());
        envio.setDestinatario_nombre(view.getTxtDestNombre().getText().trim());
        envio.setDestinatario_dni_cif(view.getTxtDestDni().getText().trim());
        String categoriaQuery = view.getComboPkgTamano().getSelectedItem().toString().split(" ")[0];
        envio.setCategoria_tamano(categoriaQuery);
        envio.setTipo_recogida(view.getComboTipoRecogida().getSelectedItem().toString());
        envio.setLugar_recogida(view.getTxtLugarRecogida().getText().trim());
        envio.setTipo_entrega(view.getComboTipoEntrega().getSelectedItem().toString());
        envio.setLugar_entrega(view.getTxtLugarEntrega().getText().trim());
        envio.setImporte_total(calculatedCost);
        envio.setEstado_actual("Envío solicitado");
        
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        envio.setFecha_prevista_recogida(Util.dateToIsoString(cal.getTime()) + " 10:00:00");

        model.registrarEnvio(envio);

        if ("Recogida en domicilio".equals(envio.getTipo_recogida())) {
            TramoDTO tr = new TramoDTO();
            tr.setNumero_seguimiento(tracking);
            tr.setOrden_tramo(1);
            tr.setOrigen(envio.getLugar_recogida());
            tr.setDestino("Madrid Central Almacén");
            tr.setVehiculo_matricula("1234-XYZ");
            tr.setTransportista_id(3);
            model.registrarTramoRuta(tr);
        }

        JOptionPane.showMessageDialog(view, 
            "¡Envío contratado con éxito!\nNúmero de seguimiento: " + tracking + 
            "\nImporte: " + String.format("%.2f €", calculatedCost), 
            "Contratación Completada", 
            JOptionPane.INFORMATION_MESSAGE
        );

        view.getTxtDestNombre().setText("");
        view.getTxtDestDni().setText("");
        view.getTxtLugarRecogida().setText("Calle Alcalá 15, Madrid");
        view.getTxtLugarEntrega().setText("Calle Uría 4, Oviedo");
        view.getLblCosteCalculado().setText("Importe Estimado: -- €");
        view.getTxtResumenContrato().setText("");
        view.getBtnConfirmarContratar().setEnabled(false);
        calculatedCost = 0.0;

        if (onEnvioCreado != null) {
            onEnvioCreado.run();
        }
    }
}
