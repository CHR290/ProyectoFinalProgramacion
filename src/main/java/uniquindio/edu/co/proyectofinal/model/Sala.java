package uniquindio.edu.co.proyectofinal.model;

import uniquindio.edu.co.proyectofinal.model.enums.EstadoSala;

import java.util.ArrayList;
import java.util.List;

public class Sala {

    private String codigo;
    private String nombre;
    private int capacidad;
    private EstadoSala estado;
    private List<Asiento> asientos;

    public Sala(String codigo, String nombre, int capacidad) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.capacidad = capacidad;
        this.estado = EstadoSala.DISPONIBLE;
        this.asientos = new ArrayList<>();
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public EstadoSala getEstado() {
        return estado;
    }

    public void setEstado(EstadoSala estado) {
        this.estado = estado;
    }

    public List<Asiento> getAsientos() {
        return asientos;
    }
}