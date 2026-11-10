package uniquindio.edu.co.proyectofinal.model;

import uniquindio.edu.co.proyectofinal.model.enums.Clasificacion;
import uniquindio.edu.co.proyectofinal.model.enums.EstadoPelicula;

public class Pelicula {

    private String codigo;
    private String nombre;
    private String genero;
    private int duracionMinutos;
    private Clasificacion clasificacion;
    private EstadoPelicula estado;

    public Pelicula(String codigo, String nombre, String genero,
                    int duracionMinutos, Clasificacion clasificacion) {
        validarDuracion(duracionMinutos);
        this.codigo = codigo;
        this.nombre = nombre;
        this.genero = genero;
        this.duracionMinutos = duracionMinutos;
        this.clasificacion = clasificacion;
        this.estado = EstadoPelicula.DISPONIBLE;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getGenero() {
        return genero;
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    public Clasificacion getClasificacion() {
        return clasificacion;
    }

    public EstadoPelicula getEstado(){
        return estado;
    }

    public boolean estaDisponible() {
        return estado == EstadoPelicula.DISPONIBLE;
    }

    public void modificar(String nombre, String genero,
                          int duracionMinutos, Clasificacion clasificacion) {
        validarDuracion(duracionMinutos);
        this.nombre = nombre;
        this.genero = genero;
        this.duracionMinutos = duracionMinutos;
        this.clasificacion = clasificacion;
    }

    public void habilitar(){
        estado = EstadoPelicula.DISPONIBLE;
    }

    public void deshabilitar() {
        estado = EstadoPelicula.NO_DISPONIBLE;
    }

    private void validarDuracion(int duracionMinutos) {
        if (duracionMinutos <= 0) {
            throw new IllegalArgumentException("La duración debe ser mayor que cero");
        }
    }
}