package uniquindio.edu.co.proyectofinal.service;

import uniquindio.edu.co.proyectofinal.model.Funcion;
import uniquindio.edu.co.proyectofinal.model.Pelicula;
import uniquindio.edu.co.proyectofinal.model.Venta;
import uniquindio.edu.co.proyectofinal.model.enums.EstadoVenta;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Reportes {

    private final Cinema cinema;

    public Reportes(Cinema cinema) {
        this.cinema = cinema;
    }

    public int totalEntradasVendidas() {
        return ventasActivas().size();
    }

    public double totalIngresos() {
        return ventasActivas().stream()
                .mapToDouble(Venta::getValorPagado)
                .sum();
    }

    public Optional<Pelicula> peliculaMasVendida() {
        Map<String, Integer> conteoPorPelicula = new TreeMap<>();
        for (Venta venta : ventasActivas()) {
            String codigo = venta.getFuncion().getPelicula().getCodigo();
            conteoPorPelicula.merge(codigo, 1, Integer::sum);
        }

        String mejorCodigo = null;
        int mejorConteo = 0;
        for (Map.Entry<String, Integer> entrada : conteoPorPelicula.entrySet()) {
            if (entrada.getValue() > mejorConteo) {
                mejorConteo = entrada.getValue();
                mejorCodigo = entrada.getKey();
            }
        }

        return Optional.ofNullable(mejorCodigo).map(cinema::buscarPelicula);
    }

    public String ocupacionFuncion(String codigoFuncion) {
        Funcion funcion = cinema.buscarFuncion(codigoFuncion);
        return String.format("Total: %d | Disponibles: %d | Ocupados: %d",
                funcion.getAsientos().size(),
                funcion.getAsientosDisponibles(),
                funcion.getAsientosOcupados());
    }

    public List<Funcion> funcionesPorFecha(LocalDate fecha) {
        return cinema.consultarFuncionesPorFecha(fecha);
    }

    private List<Venta> ventasActivas() {
        return cinema.consultarVentas().stream()
                .filter(venta -> venta.getEstado() == EstadoVenta.ACTIVA)
                .collect(Collectors.toList());
    }
}