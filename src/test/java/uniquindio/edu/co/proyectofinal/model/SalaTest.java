package uniquindio.edu.co.proyectofinal.model;

import org.junit.jupiter.api.Test;
import uniquindio.edu.co.proyectofinal.model.enums.EstadoSala;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SalaTest {

    @Test
    void nuevaSala_generaUnAsientoPorCadaCapacidad() {
        Sala sala = new Sala("S1", "Sala 1", 25);
        assertEquals(25, sala.getAsientos().size());
    }

    @Test
    void nuevaSala_asientosTienenCodigosEnFilasDe10() {
        Sala sala = new Sala("S1", "Sala 1", 25);
        List<Asiento> asientos = sala.getAsientos();

        assertEquals("A1", asientos.get(0).getCodigo());
        assertEquals("B5", asientos.get(9).getCodigo());
        assertEquals("C1", asientos.get(10).getCodigo());
        assertEquals("E5", asientos.get(24).getCodigo());
    }

    @Test
    void nuevaSala_estaDisponible() {
        Sala sala = new Sala("S1", "Sala 1", 10);
        assertTrue(sala.estaDisponible());
        assertEquals(EstadoSala.DISPONIBLE, sala.getEstado());
    }

    @Test
    void capacidadCero_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> new Sala("S1", "Sala 1", 0));
    }

    @Test
    void deshabilitar_cambiaEstadoAFueraDeServicio() {
        Sala sala = new Sala("S1", "Sala 1", 10);
        sala.deshabilitar();
        assertFalse(sala.estaDisponible());
        assertEquals(EstadoSala.FUERA_DE_SERVICIO, sala.getEstado());
    }

    @Test
    void modificar_cambiaNombreYCapacidad_regeneraAsientos() {
        Sala sala = new Sala("S1", "Sala 1", 10);
        sala.modificar("Sala VIP", 20);

        assertEquals("Sala VIP", sala.getNombre());
        assertEquals(20, sala.getCapacidad());
        assertEquals(20, sala.getAsientos().size());
    }

    @Test
    void modificar_capacidadInvalida_noCambiaNada() {
        Sala sala = new Sala("S1", "Sala 1", 10);

        assertThrows(IllegalArgumentException.class,
                () -> sala.modificar("Otro nombre", -5));

        assertEquals("Sala 1", sala.getNombre());
        assertEquals(10, sala.getAsientos().size());
    }

}