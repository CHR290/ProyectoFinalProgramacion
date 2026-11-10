package uniquindio.edu.co.proyectofinal.service;

import uniquindio.edu.co.proyectofinal.model.enums.Clasificacion;
import uniquindio.edu.co.proyectofinal.model.enums.TipoCliente;
import uniquindio.edu.co.proyectofinal.model.enums.TipoEntrada;
import java.time.LocalDate;
import java.time.LocalTime;

public final class DatosEjemplo {

    private DatosEjemplo() {
    }

    public static void cargar(Cinema cinema) {
        LocalDate manana = LocalDate.now().plusDays(1);
        LocalDate pasado = LocalDate.now().plusDays(2);

        // Películas
        cinema.registrarPelicula("Interestelar", "Ciencia ficción", 169, Clasificacion.MAYORES_12); // P001
        cinema.registrarPelicula("Coco", "Animación", 105, Clasificacion.TODOS);                    // P002
        cinema.registrarPelicula("Dune", "Ciencia ficción", 155, Clasificacion.MAYORES_15);         // P003
        cinema.registrarPelicula("Obsession", "Terror", 128, Clasificacion.MAYORES_15);
        cinema.deshabilitarPelicula("P003");

        // Salas
        cinema.registrarSala("Sala 1", 30); // S001
        cinema.registrarSala("Sala 2", 20); // S002

        // Funciones
        cinema.programarFuncion("P001", "S001", manana, LocalTime.of(16, 0), 15000); // F001
        cinema.programarFuncion("P002", "S002", manana, LocalTime.of(18, 0), 15000); // F002
        cinema.programarFuncion("P001", "S002", pasado, LocalTime.of(20, 0), 15000); // F003

        // Clientes
        cinema.registrarCliente("123", "Pepito Pérez", TipoCliente.GENERAL);
        cinema.registrarCliente("6767", "Luis Gómez", TipoCliente.ESTUDIANTE);
        cinema.registrarCliente("2546", "María Ruiz", TipoCliente.ADULTO_MAYOR);

        // Ventas
        cinema.venderEntrada("F001", "123", TipoEntrada.GENERAL, "A1");      // V001
        cinema.venderEntrada("F001", "6767", TipoEntrada.ESTUDIANTE, "A2");   // V002
        cinema.venderEntrada("F002", "2546", TipoEntrada.ADULTO_MAYOR, "B5"); // V003
        cinema.cancelarVenta("V003");
    }
}
