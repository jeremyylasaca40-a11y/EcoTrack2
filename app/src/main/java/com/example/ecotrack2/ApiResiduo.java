package com.example.ecotrack2;

public class ApiResiduo {
    private String tipo;
    private double peso;
    private String cliente;

    public ApiResiduo(String tipo, double peso, String cliente) {
        this.tipo = tipo;
        this.peso = peso;
        this.cliente = cliente;
    }

    // Getters y Setters (necesarios para Gson)
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public double getPeso() { return peso; }
    public void setPeso(double peso) { this.peso = peso; }
    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }
}