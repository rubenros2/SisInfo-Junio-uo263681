package giis.demo.envios.HU_SalidaAlmacen;

import java.util.List;
import giis.demo.util.Database;
import giis.demo.util.ApplicationException;
import giis.demo.envios.entity.EnvioDTO;
import giis.demo.envios.entity.TramoDTO;

public class SalidaAlmacenModel {
    private Database db;

    public SalidaAlmacenModel(Database db) {
        this.db = db;
    }

    public List<EnvioDTO> getEnviosDisponiblesSalida(String origenAlmacen) {
        String sql = "SELECT e.* FROM Envios e " +
                "JOIN Tramos_Ruta t ON e.numero_seguimiento = t.numero_seguimiento " +
                "WHERE e.estado_actual = 'En almacén' AND t.origen = ?";
        return db.executeQueryPojo(EnvioDTO.class, sql, origenAlmacen);
    }

    public EnvioDTO getEnvio(String numeroSeguimiento) {
        String sql = "SELECT * FROM Envios WHERE numero_seguimiento = ?";
        List<EnvioDTO> envios = db.executeQueryPojo(EnvioDTO.class, sql, numeroSeguimiento);
        if (envios.isEmpty()) {
            return null;
        }
        return envios.get(0);
    }

    public List<TramoDTO> getTramosEnvio(String numeroSeguimiento) {
        String sql = "SELECT * FROM Tramos_Ruta WHERE numero_seguimiento = ? ORDER BY orden_tramo ASC";
        return db.executeQueryPojo(TramoDTO.class, sql, numeroSeguimiento);
    }

    public void confirmarSalida(String numeroSeguimiento, String origenAlmacen, int operarioId) {
        EnvioDTO envio = getEnvio(numeroSeguimiento);
        if (envio == null) {
            throw new ApplicationException("Envío no encontrado: " + numeroSeguimiento);
        }

        if ("Incidencia detectada".equals(envio.getEstado_actual())) {
            throw new ApplicationException("No se puede expedir el envío " + numeroSeguimiento + " porque tiene una incidencia abierta.");
        }

        String tramoSql = "SELECT destino, vehiculo_matricula FROM Tramos_Ruta WHERE numero_seguimiento = ? AND origen = ?";
        List<Object[]> rows = db.executeQueryArray(tramoSql, numeroSeguimiento, origenAlmacen);
        if (rows.isEmpty()) {
            throw new ApplicationException("No hay tramo de ruta planificado que salga de " + origenAlmacen + " para el envío " + numeroSeguimiento);
        }
        
        String vehiculo = rows.get(0)[1] != null ? rows.get(0)[1].toString() : "No asignado";
        if ("No asignado".equals(vehiculo) || "".equals(vehiculo)) {
            throw new ApplicationException("No existe un vehículo asignado para el siguiente tramo desde " + origenAlmacen);
        }

        String updateSql = "UPDATE Envios SET estado_actual = 'En tránsito' WHERE numero_seguimiento = ?";
        db.executeUpdate(updateSql, numeroSeguimiento);

        String histSql = "INSERT INTO Historial_Envios (numero_seguimiento, estado, ubicacion, observaciones, usuario_responsable_id, vehiculo_asociado) " +
                "VALUES (?, 'En tránsito', ?, ?, ?, ?)";
        db.executeUpdate(histSql, 
            numeroSeguimiento, 
            origenAlmacen, 
            "Salida del paquete en tránsito hacia " + rows.get(0)[0], 
            operarioId, 
            vehiculo
        );
    }
}
