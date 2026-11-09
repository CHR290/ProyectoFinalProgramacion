package uniquindio.edu.co.proyectofinal.service;

import uniquindio.edu.co.proyectofinal.model.*;

import java.util.ArrayList;
import java.util.List;

public class Cine {

    private String nombre;
    private List<Pelicula> peliculas;
    private List<Sala> salas;
    private List<Funcion> funciones;
    private List<Cliente> clientes;
    private List<Venta> ventas;

    public Cine(String nombre) {
        this.nombre = nombre;
        this.peliculas = new ArrayList<>();
        this.salas = new ArrayList<>();
        this.funciones = new ArrayList<>();
        this.clientes = new ArrayList<>();
        this.ventas = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Pelicula> getPeliculas() {
        return peliculas;
    }

    public List<Sala> getSalas() {
        return salas;
    }

    public List<Funcion> getFunciones() {
        return funciones;
    }

    public List<Cliente> getClientes() {
        return clientes;
    }

    public List<Venta> getVentas() {
        return ventas;
    }
}
