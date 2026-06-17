package giis.demo.envios.entity;

public class TramoDTO {
    private int id;
    private String numero_seguimiento;
    private int orden_tramo;
    private String origen;
    private String destino;
    private String vehiculo_matricula;
    private Integer transportista_id;

    public TramoDTO() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNumero_seguimiento() { return numero_seguimiento; }
    public void setNumero_seguimiento(String numero_seguimiento) { this.numero_seguimiento = numero_seguimiento; }

    public int getOrden_tramo() { return orden_tramo; }
    public void setOrden_tramo(int orden_tramo) { this.orden_tramo = orden_tramo; }

    public String getOrigen() { return origen; }
    public void setOrigen(String origen) { this.origen = origen; }

    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }

    public String getVehiculo_matricula() { return vehiculo_matricula; }
    public void setVehiculo_matricula(String vehiculo_matricula) { this.vehiculo_matricula = vehiculo_matricula; }

    public Integer getTransportista_id() { return transportista_id; }
    public void setTransportista_id(Integer transportista_id) { this.transportista_id = transportista_id; }
}
