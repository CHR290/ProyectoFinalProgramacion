package uniquindio.edu.co.proyectofinal.model;

import org.junit.jupiter.api.Test;
import uniquindio.edu.co.proyectofinal.exception.VentaYaCanceladaException;
import uniquindio.edu.co.proyectofinal.model.enums.Clasificacion;
import uniquindio.edu.co.proyectofinal.model.enums.EstadoVenta;
import uniquindio.edu.co.proyectofinal.model.enums.TipoCliente;
import uniquindio.edu.co.proyectofinal.model.enums.TipoEntrada;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class VentaTest {

    private Funcion funcion() {
        Pelicula pelicula = new Pelicula("P1", "Interestelar", "Ciencia ficción",
                169, Clasificacion.MAYORES_12);
        Sala sala = new Sala("S1", "Sala 1", 4);
        return new Funcion("F1", pelicula, sala,
                LocalDate.of(2026, 11, 20), LocalTime.of(19, 0), 15000);
    }

    private Venta venta(Funcion funcion, String codigoAsiento, TipoEntrada tipo) {
        Cliente cliente = new Cliente("123", "Ana", TipoCliente.GENERAL);
        return new Venta("V1", cliente, funcion, funcion.buscarAsiento(codigoAsiento),
                tipo, LocalDateTime.of(2026, 11, 10, 10, 0));
    }

    @Test
    void nuevaVenta_estaActiva() {
        Venta v = venta(funcion(), "A1", TipoEntrada.GENERAL);
        assertEquals(EstadoVenta.ACTIVA, v.getEstado());
    }

    @Test
    void nuevaVenta_valorPagadoEsPrecioDelTipoEntrada() {
        Venta v = venta(funcion(), "A1", TipoEntrada.ESTUDIANTE);
        assertEquals(TipoEntrada.ESTUDIANTE.getPrecio(), v.getValorPagado());
    }

    @Test
    void nuevaVenta_asientoEsElDeLaFuncion() {
        Funcion f = funcion();
        Venta v = venta(f, "A2", TipoEntrada.GENERAL);
        assertSame(f.buscarAsiento("A2"), v.getAsiento());
    }

    @Test
    void cancelar_cambiaEstadoACancelada() {
        Venta v = venta(funcion(), "A1", TipoEntrada.GENERAL);
        v.cancelar();
        assertEquals(EstadoVenta.CANCELADA, v.getEstado());
    }

    @Test
    void cancelar_ventaYaCancelada_lanzaExcepcion() {
        Venta v = venta(funcion(), "A1", TipoEntrada.GENERAL);
        v.cancelar();
        assertThrows(VentaYaCanceladaException.class, v::cancelar);
    }

    @Test
    void generarEntrada_incluyePeliculaSalaYAsiento() {
        Venta v = venta(funcion(), "A2", TipoEntrada.GENERAL);
        String entrada = v.generarEntrada();

        assertTrue(entrada.contains("Interestelar"));
        assertTrue(entrada.contains("Sala 1"));
        assertTrue(entrada.contains("A2"));
    }

    @Test
    void generarEntrada_incluyeFechaYHora() {
        Venta v = venta(funcion(), "A1", TipoEntrada.GENERAL);
        String entrada = v.generarEntrada();

        assertTrue(entrada.contains("2026-11-20"));
        assertTrue(entrada.contains("19:00"));
    }
}