package giis.demo.envios.entity;

public class HistorialDTO {
    private int id;
    private String numero_seguimiento;
    private String fecha_hora;
    private String estado;
    private String ubicacion;
    private String observaciones;
    private Integer usuario_responsable_id;
    private String vehiculo_asociado;
    
    // Campo adicional para mostrar el nombre del responsable en lugar del ID
    private String responsable_nombre;

    public HistorialDTO() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNumero_seguimiento() { return numero_seguimiento; }
    public void setNumero_seguimiento(String numero_seguimiento) { this.numero_seguimiento = numero_seguimiento; }

    public String getFecha_hora() { return fecha_hora; }
    public void setFecha_hora(String fecha_hora) { this.fecha_hora = fecha_hora; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public Integer getUsuario_responsable_id() { return usuario_responsable_id; }
    public void setUsuario_responsable_id(Integer usuario_responsable_id) { this.usuario_responsable_id = usuario_responsable_id; }

    public String getVehiculo_asociado() { return vehiculo_asociado; }
    public void setVehiculo_asociado(String vehiculo_asociado) { this.vehiculo_asociado = vehiculo_asociado; }

    public String getResponsable_nombre() { return responsable_nombre; }
    public void setResponsable_nombre(String responsable_nombre) { this.responsable_nombre = responsable_nombre; }
}
