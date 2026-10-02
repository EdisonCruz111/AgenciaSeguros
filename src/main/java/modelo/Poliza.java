package modelo;

import java.sql.Date;

public class Poliza {
    private int idPoliza;
    private String numPoliza;
    private int idCliente;
    private int idAseguradora;
    private int idTipoSeguro;
    private int idAgente;
    private Date fechaInicio;
    private Date fechaVencimiento;
    private double primaAnual;
    private String estadoPoliza;

    // Campos auxiliares para mostrar en la JTable mediante JOINs
    private String nombreCliente;
    private String nombreAseguradora;
    private String nombreTipoSeguro;
    private String nombreAgente;

    public Poliza() {}

    public Poliza(int idPoliza, String numPoliza, int idCliente, int idAseguradora, int idTipoSeguro, int idAgente, Date fechaInicio, Date fechaVencimiento, double primaAnual, String estadoPoliza) {
        this.idPoliza = idPoliza;
        this.numPoliza = numPoliza;
        this.idCliente = idCliente;
        this.idAseguradora = idAseguradora;
        this.idTipoSeguro = idTipoSeguro;
        this.idAgente = idAgente;
        this.fechaInicio = fechaInicio;
        this.fechaVencimiento = fechaVencimiento;
        this.primaAnual = primaAnual;
        this.estadoPoliza = estadoPoliza;
    }

    // Getters y Setters base
    public int getIdPoliza() { return idPoliza; }
    public void setIdPoliza(int idPoliza) { this.idPoliza = idPoliza; }

    public String getNumPoliza() { return numPoliza; }
    public void setNumPoliza(String numPoliza) { this.numPoliza = numPoliza; }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public int getIdAseguradora() { return idAseguradora; }
    public void setIdAseguradora(int idAseguradora) { this.idAseguradora = idAseguradora; }

    public int getIdTipoSeguro() { return idTipoSeguro; }
    public void setIdTipoSeguro(int idTipoSeguro) { this.idTipoSeguro = idTipoSeguro; }

    public int getIdAgente() { return idAgente; }
    public void setIdAgente(int idAgente) { this.idAgente = idAgente; }

    public Date getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(Date fechaInicio) { this.fechaInicio = fechaInicio; }

    public Date getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(Date fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }

    public double getPrimaAnual() { return primaAnual; }
    public void setPrimaAnual(double primaAnual) { this.primaAnual = primaAnual; }

    public String getEstadoPoliza() { return estadoPoliza; }
    public void setEstadoPoliza(String estadoPoliza) { this.estadoPoliza = estadoPoliza; }

    // Getters y Setters de apoyo para la vista JTable
    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getNombreAseguradora() { return nombreAseguradora; }
    public void setNombreAseguradora(String nombreAseguradora) { this.nombreAseguradora = nombreAseguradora; }

    public String getNombreTipoSeguro() { return nombreTipoSeguro; }
    public void setNombreTipoSeguro(String nombreTipoSeguro) { this.nombreTipoSeguro = nombreTipoSeguro; }

    public String getNombreAgente() { return nombreAgente; }
    public void setNombreAgente(String nombreAgente) { this.nombreAgente = nombreAgente; }
}