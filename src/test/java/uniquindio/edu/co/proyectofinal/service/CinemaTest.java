package uniquindio.edu.co.proyectofinal.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import uniquindio.edu.co.proyectofinal.exception.*;
import uniquindio.edu.co.proyectofinal.model.Pelicula;
import uniquindio.edu.co.proyectofinal.model.Sala;
import uniquindio.edu.co.proyectofinal.model.enums.Clasificacion;

import static org.junit.jupiter.api.Assertions.*;

class CinemaTest {

    private Cinema cinema;

    @BeforeEach
    void setUp() {
        cinema = new Cinema("CINEMAUQ");
    }

    @Nested
    class PeliculasYSalas {

        @Test
        void registrarPelicula_generaCodigoP001() {
            Pelicula p = cinema.registrarPelicula("Interestelar", "Ciencia ficción",
                    169, Clasificacion.MAYORES_12);
            assertEquals("P001", p.getCodigo());
        }

        @Test
        void registrarPelicula_codigosSonConsecutivos() {
            cinema.registrarPelicula("A", "Drama", 100, Clasificacion.TODOS);
            Pelicula b = cinema.registrarPelicula("B", "Drama", 100, Clasificacion.TODOS);
            assertEquals("P002", b.getCodigo());
        }

        @Test
        void registrarPelicula_quedaEnLaLista() {
            Pelicula p = cinema.registrarPelicula("A", "Drama", 100, Clasificacion.TODOS);
            assertTrue(cinema.consultarPeliculas().contains(p));
        }

        @Test
        void registrarPelicula_nombreDuplicado_ignoraMayusculas() {
            cinema.registrarPelicula("Interestelar", "Drama", 169, Clasificacion.TODOS);
            assertThrows(PeliculaDuplicadaException.class,
                    () -> cinema.registrarPelicula("interestelar", "Drama", 169, Clasificacion.TODOS));
        }

        @Test
        void buscarPelicula_inexistente_lanzaExcepcion() {
            assertThrows(PeliculaNoEncontradaException.class,
                    () -> cinema.buscarPelicula("P999"));
        }

        @Test
        void modificarPelicula_cambiaDatos() {
            cinema.registrarPelicula("A", "Drama", 100, Clasificacion.TODOS);

            cinema.modificarPelicula("P001", "Dune", "Ciencia ficción", 155, Clasificacion.MAYORES_15);

            Pelicula p = cinema.buscarPelicula("P001");
            assertEquals("Dune", p.getNombre());
            assertEquals(155, p.getDuracionMinutos());
            assertEquals(Clasificacion.MAYORES_15, p.getClasificacion());
        }

        @Test
        void modificarPelicula_conNombreDeOtra_lanzaExcepcion() {
            cinema.registrarPelicula("A", "Drama", 100, Clasificacion.TODOS);
            cinema.registrarPelicula("B", "Drama", 100, Clasificacion.TODOS);

            assertThrows(PeliculaDuplicadaException.class,
                    () -> cinema.modificarPelicula("P002", "A", "Drama", 100, Clasificacion.TODOS));
        }

        @Test
        void modificarPelicula_conSuPropioNombre_noLanzaExcepcion() {
            cinema.registrarPelicula("A", "Drama", 100, Clasificacion.TODOS);

            assertDoesNotThrow(() ->
                    cinema.modificarPelicula("P001", "A", "Comedia", 100, Clasificacion.TODOS));
        }

        @Test
        void deshabilitarPelicula_cambiaEstado() {
            cinema.registrarPelicula("A", "Drama", 100, Clasificacion.TODOS);

            cinema.deshabilitarPelicula("P001");

            assertFalse(cinema.buscarPelicula("P001").estaDisponible());
        }

        @Test
        void deshabilitarPelicula_inexistente_lanzaExcepcion() {
            assertThrows(PeliculaNoEncontradaException.class,
                    () -> cinema.deshabilitarPelicula("P999"));
        }

        @Test
        void consultarPeliculas_noPermiteModificarLaLista() {
            cinema.registrarPelicula("A", "Drama", 100, Clasificacion.TODOS);
            assertThrows(UnsupportedOperationException.class,
                    () -> cinema.consultarPeliculas().clear());
        }

        @Test
        void registrarSala_generaCodigoS001() {
            Sala s = cinema.registrarSala("Sala 1", 25);
            assertEquals("S001", s.getCodigo());
        }

        @Test
        void registrarSala_nombreDuplicado_lanzaExcepcion() {
            cinema.registrarSala("Sala 1", 25);
            assertThrows(SalaDuplicadaException.class,
                    () -> cinema.registrarSala("Sala 1", 10));
        }

        @Test
        void buscarSala_inexistente_lanzaExcepcion() {
            assertThrows(SalaNoEncontradaException.class,
                    () -> cinema.buscarSala("S999"));
        }

        @Test
        void modificarSala_cambiaNombreYCapacidad() {
            cinema.registrarSala("Sala 1", 10);

            cinema.modificarSala("S001", "Sala VIP", 20);

            Sala s = cinema.buscarSala("S001");
            assertEquals("Sala VIP", s.getNombre());
            assertEquals(20, s.getCapacidad());
        }

        @Test
        void deshabilitarSala_cambiaEstado() {
            cinema.registrarSala("Sala 1", 10);

            cinema.deshabilitarSala("S001");

            assertFalse(cinema.buscarSala("S001").estaDisponible());
        }
    }
}