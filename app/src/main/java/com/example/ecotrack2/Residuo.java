package com.example.ecotrack2;

public class Residuo {
    private int id;
    private String tipoResiduo;
    private double pesoKg;
    private String fecha;
    private String hora;
    private String cliente;
    private String observaciones;

    public Residuo() {
    }

    public Residuo(String tipoResiduo, double pesoKg, String fecha, String hora,
                   String cliente, String observaciones) {
        this.tipoResiduo = tipoResiduo;
        this.pesoKg = pesoKg;
        this.fecha = fecha;
        this.hora = hora;
        this.cliente = cliente;
        this.observaciones = observaciones;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTipoResiduo() { return tipoResiduo; }
    public void setTipoResiduo(String tipoResiduo) { this.tipoResiduo = tipoResiduo; }

    public double getPesoKg() { return pesoKg; }
    public void setPesoKg(double pesoKg) { this.pesoKg = pesoKg; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }

    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}