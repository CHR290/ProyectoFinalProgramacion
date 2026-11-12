package uniquindio.edu.co.proyectofinal.model;

import uniquindio.edu.co.proyectofinal.exception.VentaYaCanceladaException;
import uniquindio.edu.co.proyectofinal.model.enums.EstadoVenta;
import uniquindio.edu.co.proyectofinal.model.enums.TipoEntrada;

import java.time.LocalDateTime;

public class Venta {

    private final String codigo;
    private final Cliente cliente;
    private final Funcion funcion;
    private final Asiento asiento;
    private final TipoEntrada tipoEntrada;
    private final LocalDateTime fechaHora;
    private final double valorPagado;
    private EstadoVenta estado;

    public Venta(String codigo, Cliente cliente, Funcion funcion, Asiento asiento,
                 TipoEntrada tipoEntrada, LocalDateTime fechaHora) {
        this.codigo = codigo;
        this.cliente = cliente;
        this.funcion = funcion;
        this.asiento = asiento;
        this.tipoEntrada = tipoEntrada;
        this.fechaHora = fechaHora;
        this.valorPagado = tipoEntrada.calcularValor(funcion.getValorBase());
        this.estado = EstadoVenta.ACTIVA;
    }

    public String getCodigo() {
        return codigo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Funcion getFuncion() {
        return funcion;
    }

    public Asiento getAsiento() {
        return asiento;
    }

    public TipoEntrada getTipoEntrada() {
        return tipoEntrada;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public double getValorPagado() {
        return valorPagado;
    }

    public EstadoVenta getEstado() {
        return estado;
    }

    public void cancelar() {
        if (estado == EstadoVenta.CANCELADA) {
            throw new VentaYaCanceladaException("La venta " + codigo + " ya está cancelada");
        }
        estado = EstadoVenta.CANCELADA;
    }

    public String generarEntrada() {
        return "Entrada: " + codigo + "\n"
                + "Película: " + funcion.getPelicula().getNombre() + "\n"
                + "Sala: " + funcion.getSala().getNombre() + "\n"
                + "Fecha: " + funcion.getFecha() + " " + funcion.getHora() + "\n"
                + "Asiento: " + asiento.getCodigo() + "\n"
                + "Tipo: " + tipoEntrada + "\n"
                + "Valor pagado: " + valorPagado;
    }
}
