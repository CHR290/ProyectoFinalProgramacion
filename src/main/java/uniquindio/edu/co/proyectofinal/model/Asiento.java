package uniquindio.edu.co.proyectofinal.model;

import uniquindio.edu.co.proyectofinal.model.enums.EstadoAsiento;

public class Asiento {

    private String codigo;
    private char fila;
    private int numero;
    private EstadoAsiento estado;

    public Asiento(char fila, int numero) {
        this.fila = fila;
        this.numero = numero;
        this.codigo = fila + String.valueOf(numero);
        this.estado = EstadoAsiento.DISPONIBLE;
    }

    public String getCodigo() {
        return codigo;
    }

    public char getFila() {
        return fila;
    }

    public int getNumero() {
        return numero;
    }

    public EstadoAsiento getEstado() {
        return estado;
    }

    public void setEstado(EstadoAsiento estado) {
        this.estado = estado;
    }
}