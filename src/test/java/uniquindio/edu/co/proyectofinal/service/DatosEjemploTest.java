package uniquindio.edu.co.proyectofinal.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uniquindio.edu.co.proyectofinal.model.enums.EstadoVenta;

import static org.junit.jupiter.api.Assertions.*;

class DatosEjemploTest {

    private Cinema cinema;

    @BeforeEach
    void setUp() {
        cinema = new Cinema("CINEMAUQ");
        DatosEjemplo.cargar(cinema);
    }

    @Test
    void cargar_creaLaCantidadEsperadaDeRegistros() {
        assertEquals(4, cinema.consultarPeliculas().size());
        assertEquals(2, cinema.consultarSalas().size());
        assertEquals(3, cinema.consultarFunciones().size());
        assertEquals(3, cinema.consultarClientes().size());
        assertEquals(3, cinema.consultarVentas().size());
    }

    @Test
    void cargar_dejaUnaPeliculaDeshabilitada() {
        assertFalse(cinema.buscarPelicula("P003").estaDisponible());
    }

    @Test
    void cargar_dejaUnaVentaCancelada() {
        assertEquals(EstadoVenta.CANCELADA, cinema.buscarVenta("V003").getEstado());
    }

    @Test
    void cargar_datosSonCompatiblesConReportes() {
        Reportes reportes = new Reportes(cinema);
        assertEquals(2, reportes.totalEntradasVendidas());
    }
}