package uniquindio.edu.co.proyectofinal.model;

import uniquindio.edu.co.proyectofinal.exception.AsientoOcupadoException;
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

    public boolean estaDisponible() {
        return estado == EstadoAsiento.DISPONIBLE;
    }

    public void ocupar() {
        if (!estaDisponible()) {
            throw new AsientoOcupadoException("El asiento " + codigo + " ya está ocupado");
        }
        estado = EstadoAsiento.OCUPADO;
    }

    public void liberar() {
        estado = EstadoAsiento.DISPONIBLE;
    }
}