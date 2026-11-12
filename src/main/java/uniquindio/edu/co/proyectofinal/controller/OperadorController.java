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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;
import uniquindio.edu.co.proyectofinal.App;
import uniquindio.edu.co.proyectofinal.exception.DomainException;
import uniquindio.edu.co.proyectofinal.model.Asiento;
import uniquindio.edu.co.proyectofinal.model.Cliente;
import uniquindio.edu.co.proyectofinal.model.Funcion;
import uniquindio.edu.co.proyectofinal.model.Pelicula;
import uniquindio.edu.co.proyectofinal.model.Venta;
import uniquindio.edu.co.proyectofinal.model.enums.Clasificacion;
import uniquindio.edu.co.proyectofinal.model.enums.EstadoFuncion;
import uniquindio.edu.co.proyectofinal.model.enums.TipoCliente;
import uniquindio.edu.co.proyectofinal.model.enums.TipoEntrada;
import uniquindio.edu.co.proyectofinal.service.Reportes;

import java.util.Arrays;
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

    // ===== Pestaña de funciones y venta =====
    @FXML private DatePicker dpFiltro;
    @FXML private TableView<Funcion> tablaFunciones;
    @FXML private TableColumn<Funcion, String> colCodigo;
    @FXML private TableColumn<Funcion, String> colPelicula;
    @FXML private TableColumn<Funcion, String> colSala;
    @FXML private TableColumn<Funcion, LocalDate> colFecha;
    @FXML private TableColumn<Funcion, LocalTime> colHora;
    @FXML private TableColumn<Funcion, Double> colValor;
    @FXML private TableColumn<Funcion, Integer> colDisponibles;
    @FXML private TableColumn<Funcion, EstadoFuncion> colEstado;
    @FXML private GridPane mapaAsientos;
    @FXML private Label lblResumen;

    @FXML private Label lblAsiento;
    @FXML private TextField txtIdentificacion;
    @FXML private Label lblCliente;
    @FXML private TextField txtNombreCliente;
    @FXML private ComboBox<TipoCliente> cmbTipoCliente;
    @FXML private ComboBox<TipoEntrada> cmbTipoEntrada;
    @FXML private Label lblMensajeVenta;
    @FXML private TextArea txtEntrada;

    // ===== Pestaña de cancelación =====
    @FXML private TextField txtCodigoVenta;
    @FXML private TextArea txtDetalleVenta;
    @FXML private Label lblMensajeCancelacion;

    private final ObservableList<Pelicula> datosPeliculas = FXCollections.observableArrayList();
    private final ObservableList<Funcion> datosFunciones = FXCollections.observableArrayList();
    private Reportes reportes;
    private Asiento asientoSeleccionado;

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
        colValor.setCellValueFactory(new PropertyValueFactory<>("valorBase"));
        colDisponibles.setCellValueFactory(new PropertyValueFactory<>("asientosDisponibles"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        tablaFunciones.setItems(datosFunciones);

        tablaFunciones.getSelectionModel().selectedItemProperty().addListener((obs, anterior, seleccionada) -> {
            asientoSeleccionado = null;
            actualizarLblAsiento();
            mostrarMapa(seleccionada);
        });
        dpFiltro.valueProperty().addListener((obs, anterior, nueva) -> refrescarFunciones());

        cmbTipoCliente.getItems().setAll(TipoCliente.values());
        cmbTipoEntrada.setConverter(new StringConverter<TipoEntrada>() {
            @Override
            public String toString(TipoEntrada tipo) {
                return tipo == null ? "" : tipo.name() + " - "
                                           + Math.round(tipo.getFactor() * 100) + "% del valor base";
            }

            @Override
            public TipoEntrada fromString(String texto) {
                return null;
            }
        });
        cmbTipoCliente.setValue(TipoCliente.GENERAL);
        cmbTipoCliente.valueProperty().addListener((obs, anterior, nuevo) -> actualizarTiposEntrada());
        actualizarTiposEntrada();

        // Si cambia la identificación, los datos del cliente se reinician
        txtIdentificacion.textProperty().addListener((obs, anterior, nueva) -> reiniciarDatosCliente());
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
    private void verificarCliente() {
        try {
            String identificacion = leerIdentificacion();
            if (cinema.existeCliente(identificacion)) {
                Cliente cliente = cinema.buscarCliente(identificacion);
                txtNombreCliente.setText(cliente.getNombre());
                cmbTipoCliente.setValue(cliente.getTipoCliente());
                habilitarDatosCliente(false);
                lblCliente.setText("Cliente registrado: " + cliente.getNombre());
            } else {
                txtNombreCliente.clear();
                cmbTipoCliente.setValue(TipoCliente.GENERAL);
                habilitarDatosCliente(true);
                lblCliente.setText("Cliente nuevo: complete nombre y tipo.");
            }
        } catch (IllegalArgumentException e) {
            App.mostrarError("Identificación inválida", e.getMessage());
        }
    }

    @FXML
    private void venderEntrada() {
        try {
            Funcion funcion = leerFuncion();
            Asiento asiento = leerAsiento();
            String identificacion = leerIdentificacion();

            if (!cinema.existeCliente(identificacion)) {
                cinema.registrarCliente(identificacion, leerNombreCliente(), leerTipoCliente());
            }

            Venta venta = cinema.venderEntrada(funcion.getCodigo(), identificacion,
                    leerTipoEntrada(), asiento.getCodigo());

            txtEntrada.setText(venta.generarEntrada());
            lblMensajeVenta.setText("Venta " + venta.getCodigo() + " realizada.");
            asientoSeleccionado = null;
            actualizarLblAsiento();
            refrescarFunciones();
        } catch (DomainException | IllegalArgumentException e) {
            App.mostrarError("No se pudo vender la entrada", e.getMessage());
        }
    }

    @FXML
    private void buscarVenta() {
        try {
            mostrarDetalleVenta(cinema.buscarVenta(leerCodigoVenta()));
        } catch (DomainException | IllegalArgumentException e) {
            App.mostrarError("No se encontró la venta", e.getMessage());
        }
    }

    @FXML
    private void cancelarVenta() {
        try {
            String codigo = leerCodigoVenta();
            cinema.cancelarVenta(codigo);
            mostrarDetalleVenta(cinema.buscarVenta(codigo));
            refrescarFunciones();
            lblMensajeCancelacion.setText("Venta " + codigo + " cancelada. El asiento quedó disponible.");
        } catch (DomainException | IllegalArgumentException e) {
            App.mostrarError("No se pudo cancelar la venta", e.getMessage());
        }
    }

    @FXML
    private void volverAlMenu() {
        App.cambiarVista("menu-view.fxml");
    }

    // ===== Refresco de datos y mapa =====

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
            boton.setStyle(estiloAsiento(asiento, asiento == asientoSeleccionado));
            boton.setDisable(!asiento.estaDisponible());
            boton.setOnAction(evento -> seleccionarAsiento(asiento));
            mapaAsientos.add(boton, asiento.getNumero() - 1, asiento.getFila() - 'A');
        }

        lblResumen.setText(reportes.ocupacionFuncion(funcion.getCodigo()));
    }

    private void seleccionarAsiento(Asiento asiento) {
        asientoSeleccionado = asiento;
        actualizarLblAsiento();
        mostrarMapa(tablaFunciones.getSelectionModel().getSelectedItem());
    }

    private String estiloAsiento(Asiento asiento, boolean seleccionado) {
        String color = asiento.estaDisponible() ? "#2ecc71" : "#e74c3c";
        String borde = seleccionado ? " -fx-border-color: #111111; -fx-border-width: 3;" : "";
        return "-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-size: 11px;" + borde;
    }

    private void actualizarLblAsiento() {
        lblAsiento.setText(asientoSeleccionado == null
                ? "Ningún asiento seleccionado"
                : "Asiento seleccionado: " + asientoSeleccionado.getCodigo());
    }

    // ===== Datos del cliente en el formulario de venta =====

    private void reiniciarDatosCliente() {
        txtNombreCliente.clear();
        cmbTipoCliente.setValue(TipoCliente.GENERAL);
        habilitarDatosCliente(true);
        lblCliente.setText("Verifique la identificación del cliente.");
    }

    private void habilitarDatosCliente(boolean nuevo) {
        txtNombreCliente.setDisable(!nuevo);
        cmbTipoCliente.setDisable(!nuevo);
    }

    private void actualizarTiposEntrada() {
        TipoCliente cliente = cmbTipoCliente.getValue();
        if (cliente == null) {
            return;
        }
        cmbTipoEntrada.getItems().setAll(Arrays.stream(TipoEntrada.values())
                .filter(tipo -> tipo.permitidaPara(cliente))
                .collect(Collectors.toList()));
        cmbTipoEntrada.setValue(TipoEntrada.GENERAL);
    }

    private void mostrarDetalleVenta(Venta venta) {
        txtDetalleVenta.setText(venta.generarEntrada() + "\nEstado: " + venta.getEstado());
    }

    // ===== Validación de formulario (solo formato) =====

    private Funcion leerFuncion() {
        Funcion funcion = tablaFunciones.getSelectionModel().getSelectedItem();
        if (funcion == null) {
            throw new IllegalArgumentException("Seleccione una función en la tabla");
        }
        return funcion;
    }

    private Asiento leerAsiento() {
        if (asientoSeleccionado == null) {
            throw new IllegalArgumentException("Seleccione un asiento disponible en el mapa");
        }
        return asientoSeleccionado;
    }

    private String leerIdentificacion() {
        String texto = txtIdentificacion.getText().trim();
        if (texto.isEmpty()) {
            throw new IllegalArgumentException("La identificación del cliente es obligatoria");
        }
        return texto;
    }

    private String leerNombreCliente() {
        String texto = txtNombreCliente.getText().trim();
        if (texto.isEmpty()) {
            throw new IllegalArgumentException("El nombre del cliente nuevo es obligatorio");
        }
        return texto;
    }

    private TipoCliente leerTipoCliente() {
        TipoCliente tipo = cmbTipoCliente.getValue();
        if (tipo == null) {
            throw new IllegalArgumentException("Seleccione el tipo de cliente");
        }
        return tipo;
    }

    private TipoEntrada leerTipoEntrada() {
        TipoEntrada tipo = cmbTipoEntrada.getValue();
        if (tipo == null) {
            throw new IllegalArgumentException("Seleccione el tipo de entrada");
        }
        return tipo;
    }

    private String leerCodigoVenta() {
        String texto = txtCodigoVenta.getText().trim();
        if (texto.isEmpty()) {
            throw new IllegalArgumentException("Escriba el código de la venta");
        }
        return texto;
    }
}