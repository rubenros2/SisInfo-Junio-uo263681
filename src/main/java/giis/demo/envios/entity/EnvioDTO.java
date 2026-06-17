package giis.demo.envios.entity;

public class EnvioDTO {
    private String numero_seguimiento;
    private String fecha_creacion;
    private Integer usuario_creador_id;
    private String remitente_nombre;
    private String remitente_dni_cif;
    private String destinatario_tipo;
    private String destinatario_nombre;
    private String destinatario_dni_cif;
    private String categoria_tamano;

    private String estado_paquete;
    private String tipo_recogida;
    private String lugar_recogida;
    private String tipo_entrega;
    private String lugar_entrega;
    private String fecha_prevista_recogida;
    private String fecha_recogida;
    private double importe_total;
    private String estado_actual;
    private String motivo_incidencia;
    private int intentos_entrega;

    public EnvioDTO() {}

    // Getters and Setters
    public String getNumero_seguimiento() { return numero_seguimiento; }
    public void setNumero_seguimiento(String numero_seguimiento) { this.numero_seguimiento = numero_seguimiento; }

    public String getFecha_creacion() { return fecha_creacion; }
    public void setFecha_creacion(String fecha_creacion) { this.fecha_creacion = fecha_creacion; }

    public Integer getUsuario_creador_id() { return usuario_creador_id; }
    public void setUsuario_creador_id(Integer usuario_creador_id) { this.usuario_creador_id = usuario_creador_id; }

    public String getRemitente_nombre() { return remitente_nombre; }
    public void setRemitente_nombre(String remitente_nombre) { this.remitente_nombre = remitente_nombre; }

    public String getRemitente_dni_cif() { return remitente_dni_cif; }
    public void setRemitente_dni_cif(String remitente_dni_cif) { this.remitente_dni_cif = remitente_dni_cif; }

    public String getDestinatario_tipo() { return destinatario_tipo; }
    public void setDestinatario_tipo(String destinatario_tipo) { this.destinatario_tipo = destinatario_tipo; }

    public String getDestinatario_nombre() { return destinatario_nombre; }
    public void setDestinatario_nombre(String destinatario_nombre) { this.destinatario_nombre = destinatario_nombre; }

    public String getDestinatario_dni_cif() { return destinatario_dni_cif; }
    public void setDestinatario_dni_cif(String destinatario_dni_cif) { this.destinatario_dni_cif = destinatario_dni_cif; }

    public String getCategoria_tamano() { return categoria_tamano; }
    public void setCategoria_tamano(String categoria_tamano) { this.categoria_tamano = categoria_tamano; }



    public String getEstado_paquete() { return estado_paquete; }
    public void setEstado_paquete(String estado_paquete) { this.estado_paquete = estado_paquete; }

    public String getTipo_recogida() { return tipo_recogida; }
    public void setTipo_recogida(String tipo_recogida) { this.tipo_recogida = tipo_recogida; }

    public String getLugar_recogida() { return lugar_recogida; }
    public void setLugar_recogida(String lugar_recogida) { this.lugar_recogida = lugar_recogida; }

    public String getTipo_entrega() { return tipo_entrega; }
    public void setTipo_entrega(String tipo_entrega) { this.tipo_entrega = tipo_entrega; }

    public String getLugar_entrega() { return lugar_entrega; }
    public void setLugar_entrega(String lugar_entrega) { this.lugar_entrega = lugar_entrega; }

    public String getFecha_prevista_recogida() { return fecha_prevista_recogida; }
    public void setFecha_prevista_recogida(String fecha_prevista_recogida) { this.fecha_prevista_recogida = fecha_prevista_recogida; }

    public String getFecha_recogida() { return fecha_recogida; }
    public void setFecha_recogida(String fecha_recogida) { this.fecha_recogida = fecha_recogida; }

    public double getImporte_total() { return importe_total; }
    public void setImporte_total(double importe_total) { this.importe_total = importe_total; }

    public String getEstado_actual() { return estado_actual; }
    public void setEstado_actual(String estado_actual) { this.estado_actual = estado_actual; }

    public String getMotivo_incidencia() { return motivo_incidencia; }
    public void setMotivo_incidencia(String motivo_incidencia) { this.motivo_incidencia = motivo_incidencia; }

    public int getIntentos_entrega() { return intentos_entrega; }
    public void setIntentos_entrega(int intentos_entrega) { this.intentos_entrega = intentos_entrega; }
}
