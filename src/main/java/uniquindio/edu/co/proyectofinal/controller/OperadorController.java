package uniquindio.edu.co.proyectofinal.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import uniquindio.edu.co.proyectofinal.App;
import uniquindio.edu.co.proyectofinal.model.Asiento;
import uniquindio.edu.co.proyectofinal.model.Funcion;
import uniquindio.edu.co.proyectofinal.model.Pelicula;
import uniquindio.edu.co.proyectofinal.model.enums.Clasificacion;
import uniquindio.edu.co.proyectofinal.model.enums.EstadoFuncion;
import uniquindio.edu.co.proyectofinal.service.Reportes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.stream.Collectors;

public class OperadorController extends ControladorBase {

    // ===== Pestaña de películas =====
    @FXML private TableView<Pelicula> tablaPeliculas;
    @FXML private TableColumn<Pelicula, String> colPeliculaCodigo;
    @FXML private TableColumn<Pelicula, String> colPeliculaNombre;
    @FXML private TableColumn<Pelicula, String> colPeliculaGenero;
    @FXML private TableColumn<Pelicula, Integer> colPeliculaDuracion;
    @FXML private TableColumn<Pelicula, Clasificacion> colPeliculaClasificacion;

    // ===== Pestaña de funciones =====
    @FXML private DatePicker dpFiltro;
    @FXML private TableView<Funcion> tablaFunciones;
    @FXML private TableColumn<Funcion, String> colCodigo;
    @FXML private TableColumn<Funcion, String> colPelicula;
    @FXML private TableColumn<Funcion, String> colSala;
    @FXML private TableColumn<Funcion, LocalDate> colFecha;
    @FXML private TableColumn<Funcion, LocalTime> colHora;
    @FXML private TableColumn<Funcion, Integer> colDisponibles;
    @FXML private TableColumn<Funcion, EstadoFuncion> colEstado;
    @FXML private GridPane mapaAsientos;
    @FXML private Label lblResumen;

    private final ObservableList<Pelicula> datosPeliculas = FXCollections.observableArrayList();
    private final ObservableList<Funcion> datosFunciones = FXCollections.observableArrayList();
    private Reportes reportes;

    @FXML
    public void initialize() {
        colPeliculaCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colPeliculaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colPeliculaGenero.setCellValueFactory(new PropertyValueFactory<>("genero"));
        colPeliculaDuracion.setCellValueFactory(new PropertyValueFactory<>("duracionMinutos"));
        colPeliculaClasificacion.setCellValueFactory(new PropertyValueFactory<>("clasificacion"));
        tablaPeliculas.setItems(datosPeliculas);

        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colPelicula.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getPelicula().getNombre()));
        colSala.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getSala().getNombre()));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colHora.setCellValueFactory(new PropertyValueFactory<>("hora"));
        colDisponibles.setCellValueFactory(new PropertyValueFactory<>("asientosDisponibles"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        tablaFunciones.setItems(datosFunciones);

        tablaFunciones.getSelectionModel().selectedItemProperty()
                .addListener((obs, anterior, seleccionada) -> mostrarMapa(seleccionada));
        dpFiltro.valueProperty().addListener((obs, anterior, nueva) -> refrescarFunciones());
    }

    @Override
    protected void alCargarCinema() {
        reportes = new Reportes(cinema);
        refrescarPeliculas();
        refrescarFunciones();
    }

    @FXML
    private void verTodasLasFunciones() {
        dpFiltro.setValue(null);
    }

    @FXML
    private void volverAlMenu() {
        App.cambiarVista("menu-view.fxml");
    }

    private void refrescarPeliculas() {
        datosPeliculas.setAll(cinema.consultarPeliculas().stream()
                .filter(Pelicula::estaDisponible)
                .collect(Collectors.toList()));
    }

    private void refrescarFunciones() {
        if (dpFiltro.getValue() == null) {
            datosFunciones.setAll(cinema.consultarFunciones());
        } else {
            datosFunciones.setAll(cinema.consultarFuncionesPorFecha(dpFiltro.getValue()));
        }
        mostrarMapa(tablaFunciones.getSelectionModel().getSelectedItem());
    }

    private void mostrarMapa(Funcion funcion) {
        mapaAsientos.getChildren().clear();
        if (funcion == null) {
            lblResumen.setText("Seleccione una función para ver su mapa de asientos.");
            return;
        }

        for (Asiento asiento : funcion.getAsientos()) {
            Button boton = new Button(asiento.getCodigo());
            boton.setMinWidth(52.0);
            boton.setStyle(estiloAsiento(asiento));
            mapaAsientos.add(boton, asiento.getNumero() - 1, asiento.getFila() - 'A');
        }

        lblResumen.setText(reportes.ocupacionFuncion(funcion.getCodigo()));
    }

    private String estiloAsiento(Asiento asiento) {
        String color = asiento.estaDisponible() ? "#2ecc71" : "#e74c3c";
        return "-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-size: 11px;";
    }
}