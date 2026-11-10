package uniquindio.edu.co.proyectofinal.model;

import org.junit.jupiter.api.Test;
import uniquindio.edu.co.proyectofinal.model.enums.Clasificacion;
import uniquindio.edu.co.proyectofinal.model.enums.EstadoPelicula;

import static org.junit.jupiter.api.Assertions.*;

class PeliculaTest {

    private Pelicula crearPelicula() {
        return new Pelicula("P1", "Interestelar", "Ciencia ficción",
                169, Clasificacion.MAYORES_12);
    }

    @Test
    void nuevaPelicula_guardaSusDatos() {
        Pelicula p = crearPelicula();

        assertEquals("P1", p.getCodigo());
        assertEquals("Interestelar", p.getNombre());
        assertEquals("Ciencia ficción", p.getGenero());
        assertEquals(169, p.getDuracionMinutos());
        assertEquals(Clasificacion.MAYORES_12, p.getClasificacion());
    }

    @Test
    void nuevaPelicula_estaDisponible() {
        Pelicula p = crearPelicula();

        assertTrue(p.estaDisponible());
        assertEquals(EstadoPelicula.DISPONIBLE, p.getEstado());
    }

    @Test
    void duracionCero_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> new Pelicula("P1", "X", "Drama", 0, Clasificacion.TODOS));
    }

    @Test
    void modificar_actualizaTodosLosDatos() {
        Pelicula p = crearPelicula();

        p.modificar("Dune", "Ciencia ficción", 155, Clasificacion.MAYORES_15);

        assertEquals("Dune", p.getNombre());
        assertEquals("Ciencia ficción", p.getGenero());
        assertEquals(155, p.getDuracionMinutos());
        assertEquals(Clasificacion.MAYORES_15, p.getClasificacion());
    }

    @Test
    void modificar_duracionInvalida_noCambiaNada() {
        Pelicula p = crearPelicula();

        assertThrows(IllegalArgumentException.class,
                () -> p.modificar("Dune", "Drama", -10, Clasificacion.TODOS));

        assertEquals("Interestelar", p.getNombre());
        assertEquals(169, p.getDuracionMinutos());
    }

    @Test
    void modificar_noCambiaElEstado() {
        Pelicula p = crearPelicula();
        p.deshabilitar();

        p.modificar("Dune", "Drama", 155, Clasificacion.TODOS);

        assertFalse(p.estaDisponible());
    }

    @Test
    void deshabilitar_cambiaEstadoANoDisponible() {
        Pelicula p = crearPelicula();

        p.deshabilitar();

        assertFalse(p.estaDisponible());
        assertEquals(EstadoPelicula.NO_DISPONIBLE, p.getEstado());
    }
}