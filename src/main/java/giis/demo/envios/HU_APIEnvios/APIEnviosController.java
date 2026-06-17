package giis.demo.envios.HU_APIEnvios;

import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import giis.demo.envios.entity.EnvioDTO;
import giis.demo.envios.entity.TarifaDTO;
import giis.demo.envios.entity.TramoDTO;

public class APIEnviosController {
    private APIEnviosModel model;
    private ObjectMapper mapper;

    public APIEnviosController(APIEnviosModel model) {
        this.model = model;
        this.mapper = new ObjectMapper();
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    /**
     * Procesa la petición JSON externa (HU-03).
     */
    @SuppressWarnings("unchecked")
    public String procesarPeticion(String jsonString) {
        Map<String, Object> response = new LinkedHashMap<>();
        List<String> errores = new ArrayList<>();

        try {
            // 1. Validar formato JSON
            Map<String, Object> input;
            try {
                input = mapper.readValue(jsonString, Map.class);
            } catch (Exception e) {
                response.put("resultado", "error");
                response.put("errores", Collections.singletonList("Formato JSON inválido: " + e.getMessage()));
                return mapper.writeValueAsString(response);
            }

            // 2. Validar campos obligatorios y reglas de negocio
            String nombreEmpresa = (String) input.get("nombre_empresa");
            if (nombreEmpresa == null || nombreEmpresa.trim().isEmpty()) {
                errores.add("El campo 'nombre_empresa' es obligatorio.");
            }

            String cif = (String) input.get("cif");
            if (cif == null || cif.trim().isEmpty()) {
                errores.add("El campo 'cif' es obligatorio.");
            }

            Map<String, Object> dest = (Map<String, Object>) input.get("destinatario");
            String destTipo = null;
            String destNombre = null;
            String destDniCif = null;
            if (dest == null) {
                errores.add("El objeto 'destinatario' es obligatorio.");
            } else {
                destTipo = (String) dest.get("tipo");
                if (destTipo == null || (!destTipo.equals("Particular") && !destTipo.equals("Empresa"))) {
                    errores.add("El campo 'destinatario.tipo' debe ser 'Particular' o 'Empresa'.");
                }
                destNombre = (String) dest.get("nombre");
                if (destNombre == null || destNombre.trim().isEmpty()) {
                    errores.add("El campo 'destinatario.nombre' es obligatorio.");
                }
                destDniCif = (String) dest.get("dni_cif");
                if (destDniCif == null || destDniCif.trim().isEmpty()) {
                    errores.add("El campo 'destinatario.dni_cif' es obligatorio.");
                }
            }

            Map<String, Object> pkg = (Map<String, Object>) input.get("paquete");
            String sizeCategory = null;
            if (pkg == null) {
                errores.add("El objeto 'paquete' es obligatorio.");
            } else {
                sizeCategory = (String) pkg.get("tamano");
                if (sizeCategory == null || (!sizeCategory.equals("Pequeño") && !sizeCategory.equals("Mediano")
                        && !sizeCategory.equals("Grande"))) {
                    errores.add("El campo 'paquete.tamano' debe ser 'Pequeño', 'Mediano' o 'Grande'.");
                }
            }

            Map<String, Object> env = (Map<String, Object>) input.get("envio");
            String tipoRecogida = null;
            String lugarRecogida = null;
            String tipoEntrega = null;
            String lugarEntrega = null;
            if (env == null) {
                errores.add("El objeto 'envio' es obligatorio.");
            } else {
                tipoRecogida = (String) env.get("tipo_recogida");
                if (tipoRecogida == null || (!tipoRecogida.equals("Recogida en domicilio") &&
                        !tipoRecogida.equals("Entrega en oficina/punto de recogida por parte del remitente") &&
                        !tipoRecogida.equals("Entrega en oficina/punto de recogida"))) {
                    errores.add("El campo 'envio.tipo_recogida' no es válido.");
                }
                lugarRecogida = (String) env.get("lugar_recogida");
                if ("Recogida en domicilio".equals(tipoRecogida)
                        && (lugarRecogida == null || lugarRecogida.trim().isEmpty())) {
                    errores.add(
                            "El campo 'envio.lugar_recogida' es obligatorio si el tipo de recogida es a domicilio.");
                }
                tipoEntrega = (String) env.get("tipo_entrega");
                if (tipoEntrega == null || (!tipoEntrega.equals("Entrega a domicilio") &&
                        !tipoEntrega.equals("Entrega en oficina/punto de recogida"))) {
                    errores.add("El campo 'envio.tipo_entrega' no es válido.");
                }
                lugarEntrega = (String) env.get("lugar_entrega");
                if (lugarEntrega == null || lugarEntrega.trim().isEmpty()) {
                    errores.add("El campo 'envio.lugar_entrega' es obligatorio.");
                }
            }

            // Si hay errores de validación, retornamos la respuesta con la lista de fallos
            if (!errores.isEmpty()) {
                response.put("resultado", "error");
                response.put("errores", errores);
                return mapper.writeValueAsString(response);
            }

            // 3. Obtener precio (la API externa usa "Pequeño", etc.)
            TarifaDTO tarifa = model.getTarifa(sizeCategory);
            double precio = tarifa.getPrecio();

            // 5. Registrar envío en BD
            String tracking = "TRK-API-" + System.currentTimeMillis();
            EnvioDTO nuevoEnvio = new EnvioDTO();
            nuevoEnvio.setNumero_seguimiento(tracking);
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            nuevoEnvio.setFecha_creacion(sdf.format(new java.util.Date()));
            nuevoEnvio.setRemitente_nombre(nombreEmpresa);
            nuevoEnvio.setRemitente_dni_cif(cif);
            nuevoEnvio.setDestinatario_tipo(destTipo);
            nuevoEnvio.setDestinatario_nombre(destNombre);
            nuevoEnvio.setDestinatario_dni_cif(destDniCif);
            nuevoEnvio.setCategoria_tamano(sizeCategory);

            // Unificar strings de tipo recogida
            String tipoRecogidaNormalized = tipoRecogida;
            if (tipoRecogida.contains("oficina") || tipoRecogida.contains("remitente")) {
                tipoRecogidaNormalized = "Entrega en oficina/punto de recogida";
            }
            nuevoEnvio.setTipo_recogida(tipoRecogidaNormalized);
            nuevoEnvio.setLugar_recogida(lugarRecogida);
            nuevoEnvio.setTipo_entrega(tipoEntrega);
            nuevoEnvio.setLugar_entrega(lugarEntrega);
            nuevoEnvio.setImporte_total(precio);
            nuevoEnvio.setEstado_actual("Envío solicitado");
            nuevoEnvio.setFecha_prevista_recogida("2026-06-18 10:00:00"); // Fecha por defecto

            // Asociar a la empresa si existe en Usuarios_Sistema (buscar por CIF)
            String userSql = "SELECT id FROM Usuarios_Sistema WHERE dni_nif = ? LIMIT 1";
            List<Object[]> users = model.getDb().executeQueryArray(userSql, cif);
            if (!users.isEmpty() && users.get(0)[0] != null) {
                nuevoEnvio.setUsuario_creador_id((Integer) users.get(0)[0]);
            } else {
                nuevoEnvio.setUsuario_creador_id(2); // ID por defecto de Tech Solutions
            }

            model.registrarEnvio(nuevoEnvio);

            // Crear primer tramo automáticamente si es recogida a domicilio para poder
            // probar HU-04
            if ("Recogida en domicilio".equals(tipoRecogidaNormalized)) {
                TramoDTO tr = new TramoDTO();
                tr.setNumero_seguimiento(tracking);
                tr.setOrden_tramo(1);
                tr.setOrigen(lugarRecogida);
                tr.setDestino("Madrid Central Almacén");
                tr.setVehiculo_matricula("1234-XYZ");
                tr.setTransportista_id(3); // Carlos Ruiz
                model.registrarTramoRuta(tr);
            }

            // 6. Generar respuesta exitosa
            response.put("resultado", "éxito");
            response.put("numero_seguimiento", tracking);
            response.put("estado_inicial", "Envío solicitado");
            response.put("importe_calculado", precio);

            return mapper.writeValueAsString(response);

        } catch (Exception e) {
            e.printStackTrace();
            try {
                response.clear();
                response.put("resultado", "error");
                response.put("errores", Collections.singletonList("Excepción en servidor: " + e.toString()));
                return mapper.writeValueAsString(response);
            } catch (Exception ex) {
                return "{\"resultado\":\"error\",\"errores\":[\"Error al serializar respuesta\"]}";
            }
        }
    }
}
