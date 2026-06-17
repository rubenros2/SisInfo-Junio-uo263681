package giis.demo.envios.entity;

public class UsuarioDTO {
    private int id;
    private String nombre;
    private String dniNif;
    private String email;
    private String passwordHash;
    private String rol;
    private String numeroEmpleado;

    public UsuarioDTO() {}

    public UsuarioDTO(int id, String nombre, String dniNif, String email, String passwordHash, String rol, String numeroEmpleado) {
        this.id = id;
        this.nombre = nombre;
        this.dniNif = dniNif;
        this.email = email;
        this.passwordHash = passwordHash;
        this.rol = rol;
        this.numeroEmpleado = numeroEmpleado;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDniNif() { return dniNif; }
    public void setDniNif(String dniNif) { this.dniNif = dniNif; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public String getNumeroEmpleado() { return numeroEmpleado; }
    public void setNumeroEmpleado(String numeroEmpleado) { this.numeroEmpleado = numeroEmpleado; }

    @Override
    public String toString() {
        return nombre + " (" + rol + ")";
    }
}
