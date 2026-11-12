package uniquindio.edu.co.proyectofinal.model;

import uniquindio.edu.co.proyectofinal.model.enums.EstadoSala;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Sala {

    private static final int ASIENTOS_POR_FILA = 5;

    private final String codigo;
    private String nombre;
    private int capacidad;
    private EstadoSala estado;
    private final List<Asiento> asientos = new ArrayList<>();

    public Sala(String codigo, String nombre, int capacidad) {
        validarCapacidad(capacidad);
        this.codigo = codigo;
        this.nombre = nombre;
        this.capacidad = capacidad;
        this.estado = EstadoSala.DISPONIBLE;
        generarAsientos();
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public EstadoSala getEstado() {
        return estado;
    }

    public boolean estaDisponible() {
        return estado == EstadoSala.DISPONIBLE;
    }

    public List<Asiento> getAsientos() {
        return Collections.unmodifiableList(asientos);
    }

    public void modificar(String nombre, int capacidad) {
        validarCapacidad(capacidad);
        this.nombre = nombre;
        if (capacidad != this.capacidad) {
            this.capacidad = capacidad;
            generarAsientos();
        }
    }

    public void habilitar() {
        estado = EstadoSala.DISPONIBLE;
    }

    public void deshabilitar() {
        estado = EstadoSala.FUERA_DE_SERVICIO;
    }

    private void generarAsientos() {
        asientos.clear();
        for (int i = 0; i < capacidad; i++) {
            char fila = (char) ('A' + i / ASIENTOS_POR_FILA);
            int numero = i % ASIENTOS_POR_FILA + 1;
            asientos.add(new Asiento(fila, numero));
        }
    }

    private void validarCapacidad(int capacidad) {
        if (capacidad <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor que cero");
        }
    }
}
