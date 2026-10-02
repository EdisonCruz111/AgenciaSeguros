package modelo;

public class TipoSeguro {
    private int idTipoSeguro;
    private String nombre;
    private String descripcion;

    public TipoSeguro() {}

    public TipoSeguro(int idTipoSeguro, String nombre, String descripcion) {
        this.idTipoSeguro = idTipoSeguro;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public int getIdTipoSeguro() { return idTipoSeguro; }
    public void setIdTipoSeguro(int idTipoSeguro) { this.idTipoSeguro = idTipoSeguro; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}