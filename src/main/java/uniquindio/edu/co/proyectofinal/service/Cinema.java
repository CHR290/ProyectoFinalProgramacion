package uniquindio.edu.co.proyectofinal.service;

import uniquindio.edu.co.proyectofinal.exception.*;
import uniquindio.edu.co.proyectofinal.model.*;
import uniquindio.edu.co.proyectofinal.model.enums.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cinema {

    private final String nombre;
    private final List<Pelicula> peliculas = new ArrayList<>();
    private final List<Sala> salas = new ArrayList<>();
    private final List<Funcion> funciones = new ArrayList<>();
    private final List<Cliente> clientes = new ArrayList<>();
    private final List<Venta> ventas = new ArrayList<>();

    private int contadorPeliculas = 0;
    private int contadorSalas = 0;
    private int contadorFunciones = 0;
    private int contadorVentas = 0;

    public Cinema(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public Pelicula registrarPelicula(String nombrePelicula, String genero,
                                      int duracionMinutos, Clasificacion clasificacion) {
        return registrarPelicula(null, nombrePelicula, genero, duracionMinutos, clasificacion);
    }

    // Si el código viene vacío se genera automáticamente (P001, P002...).
    // Si el administrador escribe uno, debe ser único.
    public Pelicula registrarPelicula(String codigoIngresado, String nombrePelicula, String genero,
                                      int duracionMinutos, Clasificacion clasificacion) {
        validarNombrePelicula(nombrePelicula, null);
        boolean automatico = estaVacio(codigoIngresado);
        int numero = contadorPeliculas + 1;
        String codigo;
        if (automatico) {
            while (existePelicula(generarCodigo('P', numero))) {
                numero++;
            }
            codigo = generarCodigo('P', numero);
        } else {
            codigo = codigoIngresado.trim().toUpperCase();
            if (existePelicula(codigo)) {
                throw new PeliculaDuplicadaException(
                        "Ya existe una película con el código " + codigo);
            }
        }
        Pelicula pelicula = new Pelicula(codigo, nombrePelicula, genero,
                duracionMinutos, clasificacion);
        peliculas.add(pelicula);
        if (automatico) {
            contadorPeliculas = numero;
        }
        return pelicula;
    }

    private boolean existePelicula(String codigo) {
        return peliculas.stream().anyMatch(p -> p.getCodigo().equals(codigo));
    }

    public List<Pelicula> consultarPeliculas() {
        return Collections.unmodifiableList(peliculas);
    }

    public Pelicula buscarPelicula(String codigo) {
        for (Pelicula pelicula : peliculas) {
            if (pelicula.getCodigo().equals(codigo)) {
                return pelicula;
            }
        }
        throw new PeliculaNoEncontradaException("No existe la película " + codigo);
    }

    public void modificarPelicula(String codigo, String nuevoNombre, String genero,
                                  int duracionMinutos, Clasificacion clasificacion) {
        Pelicula pelicula = buscarPelicula(codigo);
        validarNombrePelicula(nuevoNombre, codigo);
        pelicula.modificar(nuevoNombre, genero, duracionMinutos, clasificacion);
    }

    public void deshabilitarPelicula(String codigo) {
        buscarPelicula(codigo).deshabilitar();
    }

    public void habilitarPelicula(String codigo) {
        buscarPelicula(codigo).habilitar();
    }

    private void validarNombrePelicula(String nombrePelicula, String codigoIgnorado) {
        for (Pelicula pelicula : peliculas) {
            if (pelicula.getNombre().equalsIgnoreCase(nombrePelicula)
                    && !pelicula.getCodigo().equals(codigoIgnorado)) {
                throw new PeliculaDuplicadaException(
                        "Ya existe una película llamada " + nombrePelicula);
            }
        }
    }

    public Sala registrarSala(String nombreSala, int capacidad) {
        return registrarSala(null, nombreSala, capacidad);
    }

    // Si el código viene vacío se genera automáticamente (S001, S002...).
    // Si el administrador escribe uno, debe ser único.
    public Sala registrarSala(String codigoIngresado, String nombreSala, int capacidad) {
        validarNombreSalaLibre(nombreSala, null);
        boolean automatico = estaVacio(codigoIngresado);
        int numero = contadorSalas + 1;
        String codigo;
        if (automatico) {
            while (existeSala(generarCodigo('S', numero))) {
                numero++;
            }
            codigo = generarCodigo('S', numero);
        } else {
            codigo = codigoIngresado.trim().toUpperCase();
            if (existeSala(codigo)) {
                throw new SalaDuplicadaException("Ya existe una sala con el código " + codigo);
            }
        }
        Sala sala = new Sala(codigo, nombreSala, capacidad);
        salas.add(sala);
        if (automatico) {
            contadorSalas = numero;
        }
        return sala;
    }

    private boolean existeSala(String codigo) {
        return salas.stream().anyMatch(s -> s.getCodigo().equals(codigo));
    }

    public List<Sala> consultarSalas() {
        return Collections.unmodifiableList(salas);
    }

    public Sala buscarSala(String codigo) {
        for (Sala sala : salas) {
            if (sala.getCodigo().equals(codigo)) {
                return sala;
            }
        }
        throw new SalaNoEncontradaException("No existe la sala " + codigo);
    }

    public void modificarSala(String codigo, String nuevoNombre, int capacidad) {
        Sala sala = buscarSala(codigo);
        validarNombreSalaLibre(nuevoNombre, codigo);
        sala.modificar(nuevoNombre, capacidad);
    }

    public void deshabilitarSala(String codigo) {
        buscarSala(codigo).deshabilitar();
    }

    public void habilitarSala(String codigo) {
        buscarSala(codigo).habilitar();
    }

    private void validarNombreSalaLibre(String nombreSala, String codigoIgnorado) {
        for (Sala sala : salas) {
            if (sala.getNombre().equalsIgnoreCase(nombreSala)
                    && !sala.getCodigo().equals(codigoIgnorado)) {
                throw new SalaDuplicadaException(
                        "Ya existe una sala llamada " + nombreSala);
            }
        }
    }

    private String generarCodigo(char inicial, int numero) {
        return String.format("%c%03d", inicial, numero);
    }

    private boolean estaVacio(String texto) {
        return texto == null || texto.isBlank();
    }

    public Funcion programarFuncion(String codigoPelicula, String codigoSala,
                                    LocalDate fecha, LocalTime hora, double valorBase) {
        if (valorBase <= 0) {
            throw new IllegalArgumentException("El valor base debe ser mayor que cero");
        }
        Pelicula pelicula = buscarPelicula(codigoPelicula);
        if (!pelicula.estaDisponible()) {
            throw new PeliculaNoDisponibleException(
                    "La película " + codigoPelicula + " no está disponible");
        }

        Sala sala = buscarSala(codigoSala);
        if (!sala.estaDisponible()) {
            throw new SalaNoDisponibleException(
                    "La sala " + codigoSala + " está fuera de servicio");
        }

        String codigo = generarCodigo('F', contadorFunciones + 1);
        Funcion funcion = new Funcion(codigo, pelicula, sala, fecha, hora, valorBase);
        funciones.add(funcion);
        contadorFunciones++;
        return funcion;
    }

    public Funcion buscarFuncion(String codigo) {
        for (Funcion funcion : funciones) {
            if (funcion.getCodigo().equals(codigo)) {
                return funcion;
            }
        }
        throw new FuncionNoEncontradaException("No existe la función " + codigo);
    }

    public List<Funcion> consultarFunciones() {
        return Collections.unmodifiableList(funciones);
    }

    public List<Funcion> consultarFuncionesPorFecha(LocalDate fecha) {
        List<Funcion> resultado = new ArrayList<>();
        for (Funcion funcion : funciones) {
            if (funcion.getFecha().equals(fecha)) {
                resultado.add(funcion);
            }
        }
        return resultado;
    }

    public void cancelarFuncion(String codigo) {
        buscarFuncion(codigo).cancelar();
    }

    public void finalizarFuncion(String codigo) {
        buscarFuncion(codigo).finalizar();
    }

    public boolean existeCliente(String identificacion) {
        return buscarClienteOpcional(identificacion) != null;
    }

    public Cliente registrarCliente(String identificacion, String nombreCliente,
                                    TipoCliente tipo) {
        if (existeCliente(identificacion)) {
            throw new ClienteDuplicadoException(
                    "Ya existe un cliente con identificación " + identificacion);
        }
        Cliente nuevo = new Cliente(identificacion, nombreCliente, tipo);
        clientes.add(nuevo);
        return nuevo;
    }

    public Cliente buscarCliente(String identificacion) {
        Cliente cliente = buscarClienteOpcional(identificacion);
        if (cliente == null) {
            throw new ClienteNoEncontradoException(
                    "No existe el cliente con identificación " + identificacion);
        }
        return cliente;
    }

    public List<Cliente> consultarClientes() {
        return Collections.unmodifiableList(clientes);
    }

    private Cliente buscarClienteOpcional(String identificacion) {
        for (Cliente cliente : clientes) {
            if (cliente.getIdentificacion().equals(identificacion)) {
                return cliente;
            }
        }
        return null;
    }

    public Venta venderEntrada(String codigoFuncion, String identificacionCliente,
                               TipoEntrada tipo, String codigoAsiento) {
        Funcion funcion = buscarFuncion(codigoFuncion);
        if (funcion.getEstado() != EstadoFuncion.PROGRAMADA) {
            throw new FuncionNoDisponibleException(
                    "La función " + codigoFuncion + " no está disponible para la venta");
        }

        Cliente cliente = buscarCliente(identificacionCliente);
        if (!tipo.permitidaPara(cliente.getTipoCliente())) {
            throw new TipoEntradaNoPermitidaException(
                    "La entrada " + tipo + " solo está disponible para clientes "
                            + tipo.getPerfilRequerido());
        }

        if (!funcion.tieneAsientosDisponibles()) {
            throw new AsientosAgotadosException(
                    "La función " + codigoFuncion + " no tiene asientos disponibles");
        }

        Asiento asiento = funcion.buscarAsiento(codigoAsiento);
        if (!asiento.estaDisponible()) {
            throw new AsientoOcupadoException(
                    "El asiento " + codigoAsiento + " ya está ocupado");
        }

        String codigo = generarCodigo('V', contadorVentas + 1);
        Venta venta = new Venta(codigo, cliente, funcion, asiento, tipo, LocalDateTime.now());
        asiento.ocupar();
        ventas.add(venta);
        contadorVentas++;
        return venta;
    }

    public Venta buscarVenta(String codigo) {
        for (Venta venta : ventas) {
            if (venta.getCodigo().equals(codigo)) {
                return venta;
            }
        }
        throw new VentaNoEncontradaException("No existe la venta " + codigo);
    }

    public void cancelarVenta(String codigo) {
        Venta venta = buscarVenta(codigo);
        if (venta.getFuncion().getEstado() == EstadoFuncion.FINALIZADA) {
            throw new FuncionNoDisponibleException(
                    "No se puede cancelar la venta " + codigo
                            + " porque la función ya finalizó");
        }
        venta.cancelar();
        venta.getAsiento().liberar();
    }

    public List<Venta> consultarVentas() {
        return Collections.unmodifiableList(ventas);
    }
}