package uniquindio.edu.co.proyectofinal.model;

import uniquindio.edu.co.proyectofinal.model.enums.EstadoVenta;
import uniquindio.edu.co.proyectofinal.model.enums.TipoEntrada;

import java.time.LocalDateTime;

public class Venta {

    private String codigo;
    private Cliente cliente;
    private Funcion funcion;
    private Asiento asiento;
    private TipoEntrada tipoEntrada;
    private LocalDateTime fechaHora;
    private double valorPagado;
    private EstadoVenta estado;

    public Venta(String codigo, Cliente cliente, Funcion funcion, Asiento asiento,
                 TipoEntrada tipoEntrada, LocalDateTime fechaHora) {
        this.codigo = codigo;
        this.cliente = cliente;
        this.funcion = funcion;
        this.asiento = asiento;
        this.tipoEntrada = tipoEntrada;
        this.fechaHora = fechaHora;
        this.valorPagado = tipoEntrada.getPrecio();
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

    public void setEstado(EstadoVenta estado) {
        this.estado = estado;
    }
}
