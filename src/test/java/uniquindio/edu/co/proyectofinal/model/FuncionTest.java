package uniquindio.edu.co.proyectofinal.model;

import org.junit.jupiter.api.Test;
import uniquindio.edu.co.proyectofinal.exception.AsientoNoEncontradoException;
import uniquindio.edu.co.proyectofinal.model.enums.Clasificacion;
import uniquindio.edu.co.proyectofinal.model.enums.EstadoFuncion;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class FuncionTest {

    private Pelicula pelicula() {
        return new Pelicula("P1", "Interestelar", "Ciencia ficción",
                169, Clasificacion.MAYORES_12);
    }

    private Sala sala(int capacidad) {
        return new Sala("S1", "Sala 1", capacidad);
    }

    private Funcion funcion(Sala sala) {
        return new Funcion("F1", pelicula(), sala,
                LocalDate.of(2026, 11, 20), LocalTime.of(19, 0), 15000);
    }

    @Test
    void nuevaFuncion_estaProgramada() {
        Funcion f = funcion(sala(4));
        assertEquals(EstadoFuncion.PROGRAMADA, f.getEstado());
    }

    @Test
    void nuevaFuncion_copiaTodosLosAsientosDeLaSala() {
        Funcion f = funcion(sala(4));
        assertEquals(4, f.getAsientos().size());
    }

    @Test
    void nuevaFuncion_todosLosAsientosEstanDisponibles() {
        Funcion f = funcion(sala(4));
        assertEquals(4, f.getAsientosDisponibles());
        assertEquals(0, f.getAsientosOcupados());
    }

    @Test
    void nuevaFuncion_asientosSonCopiasNoLosDeLaSala() {
        Sala sala = sala(4);
        Funcion f = funcion(sala);

        for (int i = 0; i < 4; i++) {
            assertNotSame(sala.getAsientos().get(i), f.getAsientos().get(i));
        }
    }

    @Test
    void ocuparAsiento_enFuncion_noAfectaLaSala() {
        Sala sala = sala(4);
        Funcion f = funcion(sala);

        f.buscarAsiento("A1").ocupar();

        assertTrue(sala.getAsientos().get(0).estaDisponible());
    }

    @Test
    void ocuparAsiento_enUnaFuncion_noAfectaOtraFuncionDeLaMismaSala() {
        Sala sala = sala(4);
        Funcion f1 = funcion(sala);
        Funcion f2 = funcion(sala);

        f1.buscarAsiento("A1").ocupar();

        assertFalse(f1.buscarAsiento("A1").estaDisponible());
        assertTrue(f2.buscarAsiento("A1").estaDisponible());
    }

    @Test
    void getAsientosOcupados_cuentaCorrectamente() {
        Funcion f = funcion(sala(4));
        f.buscarAsiento("A1").ocupar();
        f.buscarAsiento("A2").ocupar();

        assertEquals(2, f.getAsientosOcupados());
        assertEquals(2, f.getAsientosDisponibles());
        assertTrue(f.tieneAsientosDisponibles());
    }

    @Test
    void tieneAsientosDisponibles_esFalsoCuandoTodosEstanOcupados() {
        Funcion f = funcion(sala(2));
        f.buscarAsiento("A1").ocupar();
        f.buscarAsiento("A2").ocupar();

        assertFalse(f.tieneAsientosDisponibles());
        assertEquals(0, f.getAsientosDisponibles());
    }

    @Test
    void buscarAsiento_existente_devuelveElAsiento() {
        Funcion f = funcion(sala(4));
        assertEquals("A2", f.buscarAsiento("A2").getCodigo());
    }

    @Test
    void buscarAsiento_inexistente_lanzaExcepcion() {
        Funcion f = funcion(sala(4));
        assertThrows(AsientoNoEncontradoException.class,
                () -> f.buscarAsiento("Z9"));
    }

    @Test
    void cancelar_cambiaEstadoACancelada() {
        Funcion f = funcion(sala(4));
        f.cancelar();
        assertEquals(EstadoFuncion.CANCELADA, f.getEstado());
    }

    @Test
    void finalizar_cambiaEstadoAFinalizada() {
        Funcion f = funcion(sala(4));
        f.finalizar();
        assertEquals(EstadoFuncion.FINALIZADA, f.getEstado());
    }
}