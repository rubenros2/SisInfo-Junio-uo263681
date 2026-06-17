package giis.demo.envios.HU_SolicitarEnvio;

import java.util.List;
import giis.demo.util.Database;
import giis.demo.util.ApplicationException;
import giis.demo.envios.entity.EnvioDTO;
import giis.demo.envios.entity.TarifaDTO;
import giis.demo.envios.entity.TramoDTO;

public class SolicitarEnvioModel {
    private Database db;

    public SolicitarEnvioModel(Database db) {
        this.db = db;
    }

    public TarifaDTO getTarifa(String categoriaTamano) {
        String sql = "SELECT id, categoria_tamano, precio FROM Tarifas WHERE categoria_tamano = ?";
        List<TarifaDTO> tarifas = db.executeQueryPojo(TarifaDTO.class, sql, categoriaTamano);
        if (tarifas.isEmpty()) {
            throw new ApplicationException("Categoría de tamaño no válida: " + categoriaTamano);
        }
        return tarifas.get(0);
    }

    public void registrarEnvio(EnvioDTO envio) {
        String sql = "INSERT INTO Envios (numero_seguimiento, fecha_creacion, usuario_creador_id, remitente_nombre, remitente_dni_cif, destinatario_tipo, destinatario_nombre, destinatario_dni_cif, categoria_tamano, estado_paquete, tipo_recogida, lugar_recogida, tipo_entrega, lugar_entrega, fecha_prevista_recogida, importe_total, estado_actual) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        db.executeUpdate(sql, 
            envio.getNumero_seguimiento(),
            envio.getFecha_creacion(),
            envio.getUsuario_creador_id(),
            envio.getRemitente_nombre(),
            envio.getRemitente_dni_cif(),
            envio.getDestinatario_tipo(),
            envio.getDestinatario_nombre(),
            envio.getDestinatario_dni_cif(),
            envio.getCategoria_tamano(),
            "Correcto",
            envio.getTipo_recogida(),
            envio.getLugar_recogida(),
            envio.getTipo_entrega(),
            envio.getLugar_entrega(),
            envio.getFecha_prevista_recogida(),
            envio.getImporte_total(),
            envio.getEstado_actual()
        );

        String histSql = "INSERT INTO Historial_Envios (numero_seguimiento, estado, ubicacion, observaciones, usuario_responsable_id) " +
                "VALUES (?, ?, ?, ?, ?)";
        db.executeUpdate(histSql, 
            envio.getNumero_seguimiento(), 
            "Envío solicitado", 
            envio.getLugar_recogida() != null && !envio.getLugar_recogida().isEmpty() ? envio.getLugar_recogida() : "Oficina/Punto de Recogida", 
            "Envío registrado en el sistema", 
            envio.getUsuario_creador_id()
        );
    }

    public void registrarTramoRuta(TramoDTO tramo) {
        String sql = "INSERT INTO Tramos_Ruta (numero_seguimiento, orden_tramo, origen, destino, vehiculo_matricula, transportista_id) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        db.executeUpdate(sql, 
            tramo.getNumero_seguimiento(), 
            tramo.getOrden_tramo(), 
            tramo.getOrigen(), 
            tramo.getDestino(), 
            tramo.getVehiculo_matricula(), 
            tramo.getTransportista_id()
        );
    }
}
