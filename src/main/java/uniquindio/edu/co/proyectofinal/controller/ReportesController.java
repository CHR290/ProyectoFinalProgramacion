package uniquindio.edu.co.proyectofinal.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import uniquindio.edu.co.proyectofinal.App;
import uniquindio.edu.co.proyectofinal.model.Funcion;
import uniquindio.edu.co.proyectofinal.model.enums.EstadoFuncion;
import uniquindio.edu.co.proyectofinal.service.Reportes;

import java.time.LocalTime;
import java.util.Locale;

public class ReportesController extends ControladorBase {

    @FXML private Label lblTotalEntradas;
    @FXML private Label lblTotalIngresos;
    @FXML private Label lblPeliculaMasVendida;

    @FXML private ComboBox<Funcion> cmbFuncion;
    @FXML private Label lblOcupacion;

    @FXML private DatePicker dpFecha;
    @FXML private TableView<Funcion> tablaFunciones;
    @FXML private TableColumn<Funcion, String> colCodigo;
    @FXML private TableColumn<Funcion, String> colPelicula;
    @FXML private TableColumn<Funcion, String> colSala;
    @FXML private TableColumn<Funcion, LocalTime> colHora;
    @FXML private TableColumn<Funcion, Integer> colDisponibles;
    @FXML private TableColumn<Funcion, EstadoFuncion> colEstado;

    private final ObservableList<Funcion> datosFunciones = FXCollections.observableArrayList();
    private Reportes reportes;

    @FXML
    public void initialize() {
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colPelicula.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getPelicula().getNombre()));
        colSala.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getSala().getNombre()));
        colHora.setCellValueFactory(new PropertyValueFactory<>("hora"));
        colDisponibles.setCellValueFactory(new PropertyValueFactory<>("asientosDisponibles"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        tablaFunciones.setItems(datosFunciones);

        cmbFuncion.setConverter(new StringConverter<Funcion>() {
            @Override
            public String toString(Funcion funcion) {
                return funcion == null ? "" : funcion.getCodigo() + " - "
                                              + funcion.getPelicula().getNombre() + " ("
                                              + funcion.getFecha() + " " + funcion.getHora() + ")";
            }

            @Override
            public Funcion fromString(String texto) {
                return null;
            }
        });
        cmbFuncion.valueProperty().addListener((obs, anterior, nueva) -> mostrarOcupacion(nueva));

        dpFecha.valueProperty().addListener((obs, anterior, nueva) -> refrescarFuncionesDeFecha());
    }

    @Override
    protected void alCargarCinema() {
        reportes = new Reportes(cinema);

        lblTotalEntradas.setText(String.valueOf(reportes.totalEntradasVendidas()));
        lblTotalIngresos.setText(formatearPesos(reportes.totalIngresos()));
        lblPeliculaMasVendida.setText(reportes.peliculaMasVendida()
                .map(p -> p.getNombre() + " (" + p.getCodigo() + ")")
                .orElse("Sin ventas activas"));

        cmbFuncion.getItems().setAll(cinema.consultarFunciones());
    }

    @FXML
    private void volverAlMenu() {
        App.cambiarVista("menu-view.fxml");
    }

    private void mostrarOcupacion(Funcion funcion) {
        lblOcupacion.setText(funcion == null
                ? "Seleccione una función."
                : reportes.ocupacionFuncion(funcion.getCodigo()));
    }

    private void refrescarFuncionesDeFecha() {
        if (dpFecha.getValue() == null) {
            datosFunciones.clear();
        } else {
            datosFunciones.setAll(reportes.funcionesPorFecha(dpFecha.getValue()));
        }
    }

    // Formato colombiano: $27.000
    private String formatearPesos(double valor) {
        return String.format(new Locale("es", "CO"), "$%,.0f", valor);
    }
}