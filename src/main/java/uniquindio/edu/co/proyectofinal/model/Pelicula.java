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

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(int duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public Clasificacion getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(Clasificacion clasificacion) {
        this.clasificacion = clasificacion;
    }

    public EstadoPelicula getEstado() {
        return estado;
    }

    public void setEstado(EstadoPelicula estado) {
        this.estado = estado;
    }
}