package modelo;

import java.sql.Timestamp;

public class Siniestro {
    private int idSiniestro;
    private String numReclamo;
    private int idPoliza;
    private Timestamp fechaOcurrencia;
    private Timestamp fechaReporte;
    private String descripcionHechos;
    private double montoReclamado;
    private double montoAprobado;
    private String estadoReclamo;

    public Siniestro() {}

    public int getIdSiniestro() { return idSiniestro; }
    public void setIdSiniestro(int idSiniestro) { this.idSiniestro = idSiniestro; }

    public String getNumReclamo() { return numReclamo; }
    public void setNumReclamo(String numReclamo) { this.numReclamo = numReclamo; }

    public int getIdPoliza() { return idPoliza; }
    public void setIdPoliza(int idPoliza) { this.idPoliza = idPoliza; }

    public Timestamp getFechaOcurrencia() { return fechaOcurrencia; }
    public void setFechaOcurrencia(Timestamp fechaOcurrencia) { this.fechaOcurrencia = fechaOcurrencia; }

    public Timestamp getFechaReporte() { return fechaReporte; }
    public void setFechaReporte(Timestamp fechaReporte) { this.fechaReporte = fechaReporte; }

    public String getDescripcionHechos() { return descripcionHechos; }
    public void setDescripcionHechos(String descripcionHechos) { this.descripcionHechos = descripcionHechos; }

    public double getMontoReclamado() { return montoReclamado; }
    public void setMontoReclamado(double montoReclamado) { this.montoReclamado = montoReclamado; }

    public double getMontoAprobado() { return montoAprobado; }
    public void setMontoAprobado(double montoAprobado) { this.montoAprobado = montoAprobado; }

    public String getEstadoReclamo() { return estadoReclamo; }
    public void setEstadoReclamo(String estadoReclamo) { this.estadoReclamo = estadoReclamo; }
}