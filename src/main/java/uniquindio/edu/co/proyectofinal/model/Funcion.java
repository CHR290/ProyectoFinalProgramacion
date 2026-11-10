package uniquindio.edu.co.proyectofinal.model;

import uniquindio.edu.co.proyectofinal.exception.AsientoNoEncontradoException;
import uniquindio.edu.co.proyectofinal.model.enums.EstadoFuncion;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Funcion {

    private final String codigo;
    private final Pelicula pelicula;
    private final Sala sala;
    private final LocalDate fecha;
    private final LocalTime hora;
    private final double valorBase;
    private EstadoFuncion estado;
    private final List<Asiento> asientos;

    public Funcion(String codigo, Pelicula pelicula, Sala sala,
                   LocalDate fecha, LocalTime hora, double valorBase) {
        this.codigo = codigo;
        this.pelicula = pelicula;
        this.sala = sala;
        this.fecha = fecha;
        this.hora = hora;
        this.valorBase = valorBase;
        this.estado = EstadoFuncion.PROGRAMADA;
        this.asientos = new ArrayList<>();
        copiarAsientosDeSala();
    }

    public String getCodigo() {
        return codigo;
    }

    public Pelicula getPelicula() {
        return pelicula;
    }

    public Sala getSala() {
        return sala;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public double getValorBase() {
        return valorBase;
    }

    public EstadoFuncion getEstado() {
        return estado;
    }

    public List<Asiento> getAsientos() {
        return Collections.unmodifiableList(asientos);
    }

    public Asiento buscarAsiento(String codigoAsiento) {
        for (Asiento asiento : asientos) {
            if (asiento.getCodigo().equals(codigoAsiento)) {
                return asiento;
            }
        }
        throw new AsientoNoEncontradoException(
                "No existe el asiento " + codigoAsiento + " en la función " + codigo);
    }

    public int getAsientosDisponibles() {
        return (int) asientos.stream().filter(Asiento::estaDisponible).count();
    }

    public int getAsientosOcupados() {
        return asientos.size() - getAsientosDisponibles();
    }

    public boolean tieneAsientosDisponibles() {
        return getAsientosDisponibles() > 0;
    }

    public void cancelar() {
        estado = EstadoFuncion.CANCELADA;
    }

    public void finalizar() {
        estado = EstadoFuncion.FINALIZADA;
    }

    private void copiarAsientosDeSala() {
        // Se crean objetos nuevos. Si se reutilizaran los de la sala,
        // ocupar un asiento en una función lo ocuparía en todas.
        for (Asiento original : sala.getAsientos()) {
            asientos.add(new Asiento(original.getFila(), original.getNumero()));
        }
    }
}