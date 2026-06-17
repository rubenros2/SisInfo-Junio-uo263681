package giis.demo.envios.HU_LlegadaAlmacen;

import java.util.List;
import giis.demo.util.Database;
import giis.demo.util.ApplicationException;
import giis.demo.envios.entity.EnvioDTO;
import giis.demo.envios.entity.HistorialDTO;

public class LlegadaAlmacenModel {
    private Database db;

    public LlegadaAlmacenModel(Database db) {
        this.db = db;
    }

    public EnvioDTO getEnvio(String numeroSeguimiento) {
        String sql = "SELECT * FROM Envios WHERE numero_seguimiento = ?";
        List<EnvioDTO> envios = db.executeQueryPojo(EnvioDTO.class, sql, numeroSeguimiento);
        if (envios.isEmpty()) {
            return null;
        }
        return envios.get(0);
    }

    public List<HistorialDTO> getHistorialEnvio(String numeroSeguimiento) {
        String sql = "SELECT h.*, u.nombre as responsable_nombre " +
                "FROM Historial_Envios h " +
                "LEFT JOIN Usuarios_Sistema u ON h.usuario_responsable_id = u.id " +
                "WHERE h.numero_seguimiento = ? ORDER BY h.fecha_hora ASC";
        return db.executeQueryPojo(HistorialDTO.class, sql, numeroSeguimiento);
    }

    public boolean confirmarLlegada(String numeroSeguimiento, String estadoPaquete, String ubicacionAlmacen, int operarioId) {
        EnvioDTO envio = getEnvio(numeroSeguimiento);
        if (envio == null) {
            throw new ApplicationException("Envío no encontrado: " + numeroSeguimiento);
        }

        boolean incidencia = false;
        String motivoIncidencia = null;
        String nuevoEstado;

        if (!"Correcto".equals(estadoPaquete)) {
            incidencia = true;
            motivoIncidencia = "Paquete reportado como " + estadoPaquete;
        }

        if (incidencia) {
            nuevoEstado = "Incidencia detectada";
        } else {
            nuevoEstado = "En almacén";
        }

        String updateSql = "UPDATE Envios SET estado_paquete = ?, estado_actual = ?, motivo_incidencia = ? WHERE numero_seguimiento = ?";
        db.executeUpdate(updateSql, estadoPaquete, nuevoEstado, motivoIncidencia, numeroSeguimiento);

        String obs = incidencia ? "Llegada con incidencia: " + motivoIncidencia : "Llegada registrada correctamente";
        String histSql = "INSERT INTO Historial_Envios (numero_seguimiento, estado, ubicacion, observaciones, usuario_responsable_id) " +
                "VALUES (?, ?, ?, ?, ?)";
        db.executeUpdate(histSql, numeroSeguimiento, nuevoEstado, ubicacionAlmacen, obs, operarioId);

        return incidencia;
    }
}
