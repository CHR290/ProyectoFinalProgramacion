package uniquindio.edu.co.proyectofinal.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uniquindio.edu.co.proyectofinal.exception.FuncionNoEncontradaException;
import uniquindio.edu.co.proyectofinal.model.enums.Clasificacion;
import uniquindio.edu.co.proyectofinal.model.enums.TipoCliente;
import uniquindio.edu.co.proyectofinal.model.enums.TipoEntrada;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class ReportesTest {

    private Cinema cinema;
    private Reportes reportes;

    @BeforeEach
    void setUp() {
        cinema = new Cinema("CINEMAUQ");
        reportes = new Reportes(cinema);

        cinema.registrarPelicula("Interestelar", "Ciencia ficción", 169, Clasificacion.MAYORES_12);
        cinema.registrarPelicula("Dune", "Ciencia ficción", 155, Clasificacion.MAYORES_15);
        cinema.registrarSala("Sala 1", 4);
        cinema.programarFuncion("P001", "S001",
                LocalDate.of(2026, 11, 20), LocalTime.of(19, 0), 15000);
        cinema.programarFuncion("P002", "S001",
                LocalDate.of(2026, 11, 21), LocalTime.of(19, 0), 15000);
        cinema.registrarCliente("123", "Ana", TipoCliente.GENERAL);
        cinema.registrarCliente("321", "Pepe", TipoCliente.ESTUDIANTE);
    }

    @Test
    void sinVentas_totalesEnCero() {
        assertEquals(0, reportes.totalEntradasVendidas());
        assertEquals(0.0, reportes.totalIngresos(), 0.001);
        assertTrue(reportes.peliculaMasVendida().isEmpty());
    }

    @Test
    void totalEntradasVendidas_noCuentaLasCanceladas() {
        cinema.venderEntrada("F001", "123", TipoEntrada.GENERAL, "A1");
        cinema.venderEntrada("F001", "123", TipoEntrada.GENERAL, "A2");
        cinema.cancelarVenta("V001");

        assertEquals(1, reportes.totalEntradasVendidas());
    }

    @Test
    void totalIngresos_sumaSoloVentasActivas() {
        double precioGeneral = TipoEntrada.GENERAL.getPrecio();
        double precioEstudiante = TipoEntrada.ESTUDIANTE.getPrecio();

        cinema.venderEntrada("F001", "123", TipoEntrada.GENERAL, "A1");
        cinema.venderEntrada("F001", "321", TipoEntrada.ESTUDIANTE, "A2");
        assertEquals(precioGeneral + precioEstudiante, reportes.totalIngresos(), 0.001);

        cinema.cancelarVenta("V001");
        assertEquals(precioEstudiante, reportes.totalIngresos(), 0.001);
    }

    @Test
    void peliculaMasVendida_devuelveLaDeMasEntradas() {
        cinema.venderEntrada("F001", "123", TipoEntrada.GENERAL, "A1");
        cinema.venderEntrada("F002", "123", TipoEntrada.GENERAL, "A1");
        cinema.venderEntrada("F002", "123", TipoEntrada.GENERAL, "A2");

        assertEquals("P002", reportes.peliculaMasVendida().get().getCodigo());
    }

    @Test
    void peliculaMasVendida_empate_devuelveLaDeCodigoMenor() {
        cinema.venderEntrada("F001", "123", TipoEntrada.GENERAL, "A1");
        cinema.venderEntrada("F002", "123", TipoEntrada.GENERAL, "A1");

        assertEquals("P001", reportes.peliculaMasVendida().get().getCodigo());
    }

    @Test
    void peliculaMasVendida_ignoraVentasCanceladas() {
        cinema.venderEntrada("F001", "123", TipoEntrada.GENERAL, "A1");
        cinema.venderEntrada("F001", "123", TipoEntrada.GENERAL, "A2");
        cinema.venderEntrada("F002", "123", TipoEntrada.GENERAL, "A1");
        cinema.cancelarVenta("V001");
        cinema.cancelarVenta("V002");

        assertEquals("P002", reportes.peliculaMasVendida().get().getCodigo());
    }

    @Test
    void ocupacionFuncion_muestraConteos() {
        cinema.venderEntrada("F001", "123", TipoEntrada.GENERAL, "A1");

        assertEquals("Total: 4 | Disponibles: 3 | Ocupados: 1",
                reportes.ocupacionFuncion("F001"));
    }

    @Test
    void ocupacionFuncion_inexistente_lanzaExcepcion() {
        assertThrows(FuncionNoEncontradaException.class,
                () -> reportes.ocupacionFuncion("F999"));
    }

    @Test
    void funcionesPorFecha_devuelveLasDeEseDia() {
        assertEquals(1, reportes.funcionesPorFecha(LocalDate.of(2026, 11, 20)).size());
        assertEquals("F001",
                reportes.funcionesPorFecha(LocalDate.of(2026, 11, 20)).get(0).getCodigo());
    }
}