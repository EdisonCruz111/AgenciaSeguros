package modelo;

public class Aseguradora {
    private int idAseguradora;
    private String rtn;
    private String nombreComercial;
    private String contactoPrincipal;
    private String telefono;
    private String correo;

    public Aseguradora() {}

    public Aseguradora(int idAseguradora, String rtn, String nombreComercial, String contactoPrincipal, String telefono, String correo) {
        this.idAseguradora = idAseguradora;
        this.rtn = rtn;
        this.nombreComercial = nombreComercial;
        this.contactoPrincipal = contactoPrincipal;
        this.telefono = telefono;
        this.correo = correo;
    }

    public int getIdAseguradora() { return idAseguradora; }
    public void setIdAseguradora(int idAseguradora) { this.idAseguradora = idAseguradora; }

    public String getRtn() { return rtn; }
    public void setRtn(String rtn) { this.rtn = rtn; }

    public String getNombreComercial() { return nombreComercial; }
    public void setNombreComercial(String nombreComercial) { this.nombreComercial = nombreComercial; }

    public String getContactoPrincipal() { return contactoPrincipal; }
    public void setContactoPrincipal(String contactoPrincipal) { this.contactoPrincipal = contactoPrincipal; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
}