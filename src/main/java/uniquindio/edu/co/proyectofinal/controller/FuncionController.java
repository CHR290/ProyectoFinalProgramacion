package uniquindio.edu.co.proyectofinal.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import uniquindio.edu.co.proyectofinal.App;
import uniquindio.edu.co.proyectofinal.exception.DomainException;
import uniquindio.edu.co.proyectofinal.model.Funcion;
import uniquindio.edu.co.proyectofinal.model.Pelicula;
import uniquindio.edu.co.proyectofinal.model.Sala;
import uniquindio.edu.co.proyectofinal.model.enums.EstadoFuncion;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.stream.Collectors;

public class FuncionController extends ControladorBase {

    @FXML private TableView<Funcion> tablaFunciones;
    @FXML private TableColumn<Funcion, String> colCodigo;
    @FXML private TableColumn<Funcion, String> colPelicula;
    @FXML private TableColumn<Funcion, String> colSala;
    @FXML private TableColumn<Funcion, LocalDate> colFecha;
    @FXML private TableColumn<Funcion, LocalTime> colHora;
    @FXML private TableColumn<Funcion, Double> colValorBase;
    @FXML private TableColumn<Funcion, Integer> colDisponibles;
    @FXML private TableColumn<Funcion, EstadoFuncion> colEstado;

    @FXML private ComboBox<Pelicula> cmbPelicula;
    @FXML private ComboBox<Sala> cmbSala;
    @FXML private DatePicker dpFecha;
    @FXML private TextField txtHora;
    @FXML private TextField txtValorBase;
    @FXML private Button btnCancelar;
    @FXML private Button btnFinalizar;
    @FXML private Label lblMensaje;

    private final ObservableList<Funcion> datos = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colPelicula.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getPelicula().getNombre()));
        colSala.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getSala().getNombre()));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colHora.setCellValueFactory(new PropertyValueFactory<>("hora"));
        colValorBase.setCellValueFactory(new PropertyValueFactory<>("valorBase"));
        colDisponibles.setCellValueFactory(new PropertyValueFactory<>("asientosDisponibles"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        tablaFunciones.setItems(datos);
        tablaFunciones.getSelectionModel().selectedItemProperty()
                .addListener((obs, anterior, seleccionada) -> actualizarBotones(seleccionada));

        cmbPelicula.setConverter(new StringConverter<Pelicula>() {
            @Override
            public String toString(Pelicula pelicula) {
                return pelicula == null ? "" : pelicula.getCodigo() + " - " + pelicula.getNombre();
            }

            @Override
            public Pelicula fromString(String texto) {
                return null;
            }
        });

        cmbSala.setConverter(new StringConverter<Sala>() {
            @Override
            public String toString(Sala sala) {
                return sala == null ? "" : sala.getCodigo() + " - " + sala.getNombre();
            }

            @Override
            public Sala fromString(String texto) {
                return null;
            }
        });
    }

    @Override
    protected void alCargarCinema() {
        // Solo se ofrecen películas y salas disponibles para programar
        cmbPelicula.getItems().setAll(cinema.consultarPeliculas().stream()
                .filter(Pelicula::estaDisponible)
                .collect(Collectors.toList()));
        cmbSala.getItems().setAll(cinema.consultarSalas().stream()
                .filter(Sala::estaDisponible)
                .collect(Collectors.toList()));
        refrescarTabla();
    }

    @FXML
    private void programarFuncion() {
        try {
            Funcion nueva = cinema.programarFuncion(
                    leerPelicula().getCodigo(),
                    leerSala().getCodigo(),
                    leerFecha(),
                    leerHora(),
                    leerValorBase());
            refrescarTabla();
            limpiarFormulario();
            mostrarMensaje("Función " + nueva.getCodigo() + " programada.");
        } catch (DomainException | IllegalArgumentException e) {
            App.mostrarError("No se pudo programar", e.getMessage());
        }
    }

    @FXML
    private void cancelarFuncion() {
        Funcion seleccionada = tablaFunciones.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarMensaje("Seleccione una función de la tabla.");
            return;
        }
        try {
            cinema.cancelarFuncion(seleccionada.getCodigo());
            refrescarTabla();
            mostrarMensaje("Función " + seleccionada.getCodigo() + " cancelada.");
        } catch (DomainException e) {
            App.mostrarError("No se pudo cancelar", e.getMessage());
        }
    }

    @FXML
    private void finalizarFuncion() {
        Funcion seleccionada = tablaFunciones.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarMensaje("Seleccione una función de la tabla.");
            return;
        }
        try {
            cinema.finalizarFuncion(seleccionada.getCodigo());
            refrescarTabla();
            mostrarMensaje("Función " + seleccionada.getCodigo() + " finalizada.");
        } catch (DomainException e) {
            App.mostrarError("No se pudo finalizar", e.getMessage());
        }
    }

    @FXML
    private void limpiarFormulario() {
        cmbPelicula.setValue(null);
        cmbSala.setValue(null);
        dpFecha.setValue(null);
        txtHora.clear();
        txtValorBase.clear();
        tablaFunciones.getSelectionModel().clearSelection();
    }

    @FXML
    private void irAPeliculas() {
        App.cambiarVista("admin-view.fxml");
    }

    @FXML
    private void irASalas() {
        App.cambiarVista("sala-view.fxml");
    }

    @FXML
    private void volverAlMenu() {
        App.cambiarVista("menu-view.fxml");
    }

    private void refrescarTabla() {
        datos.setAll(cinema.consultarFunciones());
        actualizarBotones(tablaFunciones.getSelectionModel().getSelectedItem());
    }

    // Solo una función programada se puede cancelar o finalizar
    private void actualizarBotones(Funcion seleccionada) {
        boolean programada = seleccionada != null
                && seleccionada.getEstado() == EstadoFuncion.PROGRAMADA;
        btnCancelar.setDisable(!programada);
        btnFinalizar.setDisable(!programada);
    }

    private void mostrarMensaje(String texto) {
        lblMensaje.setText(texto);
    }

    // ===== Validación de formulario (solo formato) =====

    private Pelicula leerPelicula() {
        Pelicula pelicula = cmbPelicula.getValue();
        if (pelicula == null) {
            throw new IllegalArgumentException("Seleccione una película");
        }
        return pelicula;
    }

    private Sala leerSala() {
        Sala sala = cmbSala.getValue();
        if (sala == null) {
            throw new IllegalArgumentException("Seleccione una sala");
        }
        return sala;
    }

    private LocalDate leerFecha() {
        LocalDate fecha = dpFecha.getValue();
        if (fecha == null) {
            throw new IllegalArgumentException("Seleccione una fecha");
        }
        return fecha;
    }

    private LocalTime leerHora() {
        try {
            return LocalTime.parse(txtHora.getText().trim());
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("La hora debe tener el formato HH:mm, por ejemplo 16:00");
        }
    }

    private double leerValorBase() {
        double valor;
        try {
            valor = Double.parseDouble(txtValorBase.getText().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El valor base debe ser un número");
        }
        if (valor <= 0) {
            throw new IllegalArgumentException("El valor base debe ser mayor que cero");
        }
        return valor;
    }
}
