package giis.demo.envios.HU_RecogidaPaquete;

import java.util.List;
import giis.demo.util.Database;
import giis.demo.util.ApplicationException;
import giis.demo.envios.entity.EnvioDTO;

public class RecogidaPaqueteModel {
    private Database db;

    public RecogidaPaqueteModel(Database db) {
        this.db = db;
    }

    public List<EnvioDTO> getRecogidasAsignadas(int transportistaId) {
        String sql = "SELECT e.* FROM Envios e " +
                "JOIN Tramos_Ruta t ON e.numero_seguimiento = t.numero_seguimiento " +
                "WHERE t.transportista_id = ? AND t.orden_tramo = 1 AND e.estado_actual = 'Envío solicitado'";
        return db.executeQueryPojo(EnvioDTO.class, sql, transportistaId);
    }

    public EnvioDTO getEnvio(String numeroSeguimiento) {
        String sql = "SELECT * FROM Envios WHERE numero_seguimiento = ?";
        List<EnvioDTO> envios = db.executeQueryPojo(EnvioDTO.class, sql, numeroSeguimiento);
        if (envios.isEmpty()) {
            return null;
        }
        return envios.get(0);
    }

    public void confirmarRecogida(String numeroSeguimiento, int transportistaId) {
        EnvioDTO envio = getEnvio(numeroSeguimiento);
        if (envio == null) {
            throw new ApplicationException("Envío no encontrado: " + numeroSeguimiento);
        }

        String vehiculo = "";
        String tramoSql = "SELECT vehiculo_matricula FROM Tramos_Ruta WHERE numero_seguimiento = ? AND orden_tramo = 1";
        List<Object[]> rows = db.executeQueryArray(tramoSql, numeroSeguimiento);
        if (!rows.isEmpty() && rows.get(0)[0] != null) {
            vehiculo = rows.get(0)[0].toString();
        }

        String updateSql = "UPDATE Envios SET estado_actual = 'Recogido', fecha_recogida = CURRENT_TIMESTAMP WHERE numero_seguimiento = ?";
        db.executeUpdate(updateSql, numeroSeguimiento);

        String histSql = "INSERT INTO Historial_Envios (numero_seguimiento, estado, ubicacion, observaciones, usuario_responsable_id, vehiculo_asociado) " +
                "VALUES (?, 'Recogido', ?, 'Paquete recogido por el transportista', ?, ?)";
        db.executeUpdate(histSql, 
            numeroSeguimiento, 
            envio.getLugar_recogida() != null && !envio.getLugar_recogida().isEmpty() ? envio.getLugar_recogida() : "Oficina/Punto de Recogida", 
            transportistaId, 
            vehiculo
        );
    }
}
