package uniquindio.edu.co.proyectofinal.model;

import org.junit.jupiter.api.Test;
import uniquindio.edu.co.proyectofinal.exception.AsientoOcupadoException;
import uniquindio.edu.co.proyectofinal.model.enums.EstadoAsiento;

import static org.junit.jupiter.api.Assertions.*;

class AsientoTest {

    @Test
    void nuevoAsiento_estaDisponible() {
        Asiento asiento = new Asiento('A', 1);
        assertTrue(asiento.estaDisponible());
        assertEquals(EstadoAsiento.DISPONIBLE, asiento.getEstado());
    }

    @Test
    void codigo_seFormaConFilaYNumero() {
        Asiento asiento = new Asiento('B', 12);
        assertEquals("B12", asiento.getCodigo());
    }

    @Test
    void ocupar_asientoLibre_cambiaEstadoAOcupado() {
        Asiento asiento = new Asiento('A', 1);
        asiento.ocupar();
        assertEquals(EstadoAsiento.OCUPADO, asiento.getEstado());
        assertFalse(asiento.estaDisponible());
    }

    @Test
    void ocupar_asientoYaOcupado_lanzaExcepcion() {
        Asiento asiento = new Asiento('A', 1);
        asiento.ocupar();
        assertThrows(AsientoOcupadoException.class, asiento::ocupar);
        assertEquals(EstadoAsiento.OCUPADO, asiento.getEstado());
    }

    @Test
    void liberar_asientoOcupado_vuelveADisponible() {
        Asiento asiento = new Asiento('A', 1);
        asiento.ocupar();
        asiento.liberar();
        assertTrue(asiento.estaDisponible());
    }

    @Test
    void liberar_asientoLibre_noCambiaNada() {
        Asiento asiento = new Asiento('A', 1);
        asiento.liberar();
        assertTrue(asiento.estaDisponible());
    }
}