package giis.demo.envios.entity;

public class TarifaDTO {
    private int id;
    private String categoria_tamano;
    private double precio;

    public TarifaDTO() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCategoria_tamano() { return categoria_tamano; }
    public void setCategoria_tamano(String categoria_tamano) { this.categoria_tamano = categoria_tamano; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
}
