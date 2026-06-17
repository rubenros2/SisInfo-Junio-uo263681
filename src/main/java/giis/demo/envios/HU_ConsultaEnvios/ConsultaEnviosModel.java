package giis.demo.envios.HU_ConsultaEnvios;

import java.util.List;
import giis.demo.util.Database;
import giis.demo.envios.entity.EnvioDTO;
import giis.demo.envios.entity.HistorialDTO;

public class ConsultaEnviosModel {
    private Database db;

    public ConsultaEnviosModel(Database db) {
        this.db = db;
    }

    public List<EnvioDTO> getEnviosUsuario(int usuarioId, String dniCif) {
        String sql = "SELECT * FROM Envios WHERE usuario_creador_id = ? OR remitente_dni_cif = ? ORDER BY fecha_creacion DESC";
        return db.executeQueryPojo(EnvioDTO.class, sql, usuarioId, dniCif);
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
}
