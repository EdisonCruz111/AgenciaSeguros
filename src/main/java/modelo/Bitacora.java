package modelo;

import java.sql.Timestamp;

public class Bitacora {
    private int idLog;
    private int idUsuario;
    private String nombreUsuario; // Para mostrar el username en la JTable
    private Timestamp fechaIngreso;
    private String ipOrigen;
    private String accion;

    public Bitacora() {}

    // Getters y Setters
    public int getIdLog() { return idLog; }
    public void setIdLog(int idLog) { this.idLog = idLog; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    public Timestamp getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(Timestamp fechaIngreso) { this.fechaIngreso = fechaIngreso; }

    public String getIpOrigen() { return ipOrigen; }
    public void setIpOrigen(String ipOrigen) { this.ipOrigen = ipOrigen; }

    public String getAccion() { return accion; }
    public void setAccion(String accion) { this.accion = accion; }
}