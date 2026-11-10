package uniquindio.edu.co.proyectofinal.service;

import uniquindio.edu.co.proyectofinal.exception.PeliculaDuplicadaException;
import uniquindio.edu.co.proyectofinal.exception.PeliculaNoEncontradaException;
import uniquindio.edu.co.proyectofinal.exception.SalaDuplicadaException;
import uniquindio.edu.co.proyectofinal.exception.SalaNoEncontradaException;
import uniquindio.edu.co.proyectofinal.model.*;
import uniquindio.edu.co.proyectofinal.model.enums.Clasificacion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cinema {

    private final String nombre;
    private final List<Pelicula> peliculas = new ArrayList<>();
    private final List<Sala> salas = new ArrayList<>();
    private final List<Funcion> funciones = new ArrayList<>();
    private final List<Cliente> clientes = new ArrayList<>();
    private final List<Venta> ventas = new ArrayList<>();

    private int contadorPeliculas = 0;
    private int contadorSalas = 0;
    private int contadorFunciones = 0;
    private int contadorVentas = 0;

    public Cinema(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public Pelicula registrarPelicula(String nombrePelicula, String genero,
                                      int duracionMinutos, Clasificacion clasificacion) {
        validarNombrePelicula(nombrePelicula, null);
        String codigo = generarCodigo('P', contadorPeliculas + 1);
        Pelicula pelicula = new Pelicula(codigo, nombrePelicula, genero,
                duracionMinutos, clasificacion);
        peliculas.add(pelicula);
        contadorPeliculas++;
        return pelicula;
    }

    public List<Pelicula> consultarPeliculas() {
        return Collections.unmodifiableList(peliculas);
    }

    public Pelicula buscarPelicula(String codigo) {
        for (Pelicula pelicula : peliculas) {
            if (pelicula.getCodigo().equals(codigo)) {
                return pelicula;
            }
        }
        throw new PeliculaNoEncontradaException("No existe la película " + codigo);
    }

    public void modificarPelicula(String codigo, String nuevoNombre, String genero,
                                  int duracionMinutos, Clasificacion clasificacion) {
        Pelicula pelicula = buscarPelicula(codigo);
        validarNombrePelicula(nuevoNombre, codigo);
        pelicula.modificar(nuevoNombre, genero, duracionMinutos, clasificacion);
    }

    public void deshabilitarPelicula(String codigo) {
        buscarPelicula(codigo).deshabilitar();
    }

    public void habilitarPelicula(String codigo) {
        buscarPelicula(codigo).habilitar();
    }

    private void validarNombrePelicula(String nombrePelicula, String codigoIgnorado) {
        for (Pelicula pelicula : peliculas) {
            if (pelicula.getNombre().equalsIgnoreCase(nombrePelicula)
                    && !pelicula.getCodigo().equals(codigoIgnorado)) {
                throw new PeliculaDuplicadaException(
                        "Ya existe una película llamada " + nombrePelicula);
            }
        }
    }

    public Sala registrarSala(String nombreSala, int capacidad) {
        validarNombreSalaLibre(nombreSala, null);
        String codigo = generarCodigo('S', contadorSalas + 1);
        Sala sala = new Sala(codigo, nombreSala, capacidad);
        salas.add(sala);
        contadorSalas++;
        return sala;
    }

    public List<Sala> consultarSalas() {
        return Collections.unmodifiableList(salas);
    }

    public Sala buscarSala(String codigo) {
        for (Sala sala : salas) {
            if (sala.getCodigo().equals(codigo)) {
                return sala;
            }
        }
        throw new SalaNoEncontradaException("No existe la sala " + codigo);
    }

    public void modificarSala(String codigo, String nuevoNombre, int capacidad) {
        Sala sala = buscarSala(codigo);
        validarNombreSalaLibre(nuevoNombre, codigo);
        sala.modificar(nuevoNombre, capacidad);
    }

    public void deshabilitarSala(String codigo) {
        buscarSala(codigo).deshabilitar();
    }

    private void validarNombreSalaLibre(String nombreSala, String codigoIgnorado) {
        for (Sala sala : salas) {
            if (sala.getNombre().equalsIgnoreCase(nombreSala)
                    && !sala.getCodigo().equals(codigoIgnorado)) {
                throw new SalaDuplicadaException(
                        "Ya existe una sala llamada " + nombreSala);
            }
        }
    }

    private String generarCodigo(char inicial, int numero) {
        return String.format("%c%03d", inicial, numero);
    }
}