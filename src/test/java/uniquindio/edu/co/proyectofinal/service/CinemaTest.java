package uniquindio.edu.co.proyectofinal.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import uniquindio.edu.co.proyectofinal.exception.*;
import uniquindio.edu.co.proyectofinal.model.Cliente;
import uniquindio.edu.co.proyectofinal.model.Pelicula;
import uniquindio.edu.co.proyectofinal.model.Sala;
import uniquindio.edu.co.proyectofinal.model.enums.Clasificacion;
import uniquindio.edu.co.proyectofinal.model.Funcion;
import uniquindio.edu.co.proyectofinal.model.enums.EstadoFuncion;
import uniquindio.edu.co.proyectofinal.model.enums.TipoCliente;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
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
    @Nested
    class Funciones {

        private static final LocalDate FECHA = LocalDate.of(2026, 11, 20);
        private static final LocalTime HORA = LocalTime.of(19, 0);

        // Crea la película P001 y la sala S001 con la capacidad indicada
        private void datosBase() {
            cinema.registrarPelicula("Interestelar", "Ciencia ficción", 169, Clasificacion.MAYORES_12);
            cinema.registrarSala("Sala 1", 4);
        }

        private Funcion programar(LocalDate fecha) {
            return cinema.programarFuncion("P001", "S001", fecha, HORA, 15000);
        }

        @Test
        void programarFuncion_generaCodigoF001() {
            datosBase();
            assertEquals("F001", programar(FECHA).getCodigo());
        }

        @Test
        void programarFuncion_copiaLosAsientosDeLaSala() {
            datosBase();
            Funcion f = programar(FECHA);

            assertEquals(4, f.getAsientos().size());
            assertEquals(4, f.getAsientosDisponibles());
        }

        @Test
        void programarFuncion_peliculaNoEncontrada_lanzaExcepcion() {
            datosBase();
            assertThrows(PeliculaNoEncontradaException.class,
                    () -> cinema.programarFuncion("P999", "S001", FECHA, HORA, 15000));
        }

        @Test
        void programarFuncion_peliculaDeshabilitada_lanzaExcepcion() {
            datosBase();
            cinema.deshabilitarPelicula("P001");

            assertThrows(PeliculaNoDisponibleException.class, () -> programar(FECHA));
        }

        @Test
        void programarFuncion_salaNoEncontrada_lanzaExcepcion() {
            datosBase();
            assertThrows(SalaNoEncontradaException.class,
                    () -> cinema.programarFuncion("P001", "S999", FECHA, HORA, 15000));
        }

        @Test
        void programarFuncion_salaFueraDeServicio_lanzaExcepcion() {
            datosBase();
            cinema.deshabilitarSala("S001");

            assertThrows(SalaNoDisponibleException.class, () -> programar(FECHA));
        }

        @Test
        void programarFuncion_fallida_noConsumeCodigo() {
            datosBase();
            assertThrows(PeliculaNoEncontradaException.class,
                    () -> cinema.programarFuncion("P999", "S001", FECHA, HORA, 15000));

            assertEquals("F001", programar(FECHA).getCodigo());
        }

        @Test
        void buscarFuncion_existente_devuelveLaFuncion() {
            datosBase();
            Funcion creada = programar(FECHA);

            assertSame(creada, cinema.buscarFuncion("F001"));
        }

        @Test
        void buscarFuncion_inexistente_lanzaExcepcion() {
            assertThrows(FuncionNoEncontradaException.class,
                    () -> cinema.buscarFuncion("F999"));
        }

        @Test
        void consultarFuncionesPorFecha_filtraPorFecha() {
            datosBase();
            programar(LocalDate.of(2026, 11, 20));
            programar(LocalDate.of(2026, 11, 21));

            List<Funcion> lista = cinema.consultarFuncionesPorFecha(LocalDate.of(2026, 11, 20));

            assertEquals(1, lista.size());
            assertEquals("F001", lista.get(0).getCodigo());
        }

        @Test
        void cancelarFuncion_cambiaEstadoACancelada() {
            datosBase();
            programar(FECHA);

            cinema.cancelarFuncion("F001");

            assertEquals(EstadoFuncion.CANCELADA, cinema.buscarFuncion("F001").getEstado());
        }

        @Test
        void finalizarFuncion_cambiaEstadoAFinalizada() {
            datosBase();
            programar(FECHA);

            cinema.finalizarFuncion("F001");

            assertEquals(EstadoFuncion.FINALIZADA, cinema.buscarFuncion("F001").getEstado());
        }

        @Test
        void cancelarFuncion_inexistente_lanzaExcepcion() {
            assertThrows(FuncionNoEncontradaException.class,
                    () -> cinema.cancelarFuncion("F999"));
        }
    }
    @Nested
    class Clientes {

        @Test
        void existeCliente_inexistente_devuelveFalse() {
            assertFalse(cinema.existeCliente("123"));
        }

        @Test
        void existeCliente_registrado_devuelveTrue() {
            cinema.registrarCliente("123", "Ana", TipoCliente.GENERAL);
            assertTrue(cinema.existeCliente("123"));
        }

        @Test
        void registrarCliente_quedaEnLaLista() {
            Cliente c = cinema.registrarCliente("123", "Ana", TipoCliente.GENERAL);

            assertEquals("123", c.getIdentificacion());
            assertTrue(cinema.consultarClientes().contains(c));
        }

        @Test
        void registrarCliente_guardaNombreYTipo() {
            Cliente c = cinema.registrarCliente("123", "Ana", TipoCliente.ESTUDIANTE);

            assertEquals("Ana", c.getNombre());
            assertEquals(TipoCliente.ESTUDIANTE, c.getTipoCliente());
        }

        @Test
        void registrarCliente_duplicado_lanzaExcepcionSinCambiarNada() {
            cinema.registrarCliente("123", "Ana", TipoCliente.GENERAL);

            assertThrows(ClienteDuplicadoException.class,
                    () -> cinema.registrarCliente("123", "Otro nombre", TipoCliente.ESTUDIANTE));

            assertEquals(1, cinema.consultarClientes().size());
            assertEquals("Ana", cinema.buscarCliente("123").getNombre());
        }

        @Test
        void buscarCliente_existente_devuelveElCliente() {
            Cliente registrado = cinema.registrarCliente("123", "Ana", TipoCliente.GENERAL);

            assertSame(registrado, cinema.buscarCliente("123"));
        }

        @Test
        void buscarCliente_inexistente_lanzaExcepcion() {
            assertThrows(ClienteNoEncontradoException.class,
                    () -> cinema.buscarCliente("999"));
        }

        @Test
        void consultarClientes_noPermiteModificarLaLista() {
            cinema.registrarCliente("123", "Ana", TipoCliente.GENERAL);

            assertThrows(UnsupportedOperationException.class,
                    () -> cinema.consultarClientes().clear());
        }
    }
}