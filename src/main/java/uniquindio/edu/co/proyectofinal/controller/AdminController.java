package uniquindio.edu.co.proyectofinal.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import uniquindio.edu.co.proyectofinal.App;
import uniquindio.edu.co.proyectofinal.exception.DomainException;
import uniquindio.edu.co.proyectofinal.model.Pelicula;
import uniquindio.edu.co.proyectofinal.model.enums.Clasificacion;
import uniquindio.edu.co.proyectofinal.model.enums.EstadoPelicula;

public class AdminController extends ControladorBase {

    @FXML private TableView<Pelicula> tablaPeliculas;
    @FXML private TableColumn<Pelicula, String> colCodigo;
    @FXML private TableColumn<Pelicula, String> colNombre;
    @FXML private TableColumn<Pelicula, String> colGenero;
    @FXML private TableColumn<Pelicula, Integer> colDuracion;
    @FXML private TableColumn<Pelicula, Clasificacion> colClasificacion;
    @FXML private TableColumn<Pelicula, EstadoPelicula> colEstado;

    @FXML private TextField txtNombre;
    @FXML private TextField txtGenero;
    @FXML private TextField txtDuracion;
    @FXML private ComboBox<Clasificacion> cmbClasificacion;
    @FXML private Button btnHabilitar;
    @FXML private Button btnDeshabilitar;
    @FXML private Label lblMensaje;

    private final ObservableList<Pelicula> datos = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colGenero.setCellValueFactory(new PropertyValueFactory<>("genero"));
        colDuracion.setCellValueFactory(new PropertyValueFactory<>("duracionMinutos"));
        colClasificacion.setCellValueFactory(new PropertyValueFactory<>("clasificacion"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        tablaPeliculas.setItems(datos);
        tablaPeliculas.getSelectionModel().selectedItemProperty()
                .addListener((obs, anterior, seleccionada) -> {
                    cargarFormulario(seleccionada);
                    actualizarBotones(seleccionada);
                });

        cmbClasificacion.getItems().setAll(Clasificacion.values());
    }

    @Override
    protected void alCargarCinema() {
        refrescarTabla();
    }

    @FXML
    private void registrarPelicula() {
        try {
            Pelicula nueva = cinema.registrarPelicula(
                    leerNombre(), leerGenero(), leerDuracion(), leerClasificacion());
            refrescarTabla();
            limpiarFormulario();
            mostrarMensaje("Película " + nueva.getCodigo() + " registrada.");
        } catch (DomainException | IllegalArgumentException e) {
            App.mostrarError("No se pudo registrar", e.getMessage());
        }
    }

    @FXML
    private void modificarPelicula() {
        Pelicula seleccionada = tablaPeliculas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarMensaje("Seleccione una película de la tabla.");
            return;
        }
        try {
            cinema.modificarPelicula(seleccionada.getCodigo(),
                    leerNombre(), leerGenero(), leerDuracion(), leerClasificacion());
            refrescarTabla();
            mostrarMensaje("Película " + seleccionada.getCodigo() + " modificada.");
        } catch (DomainException | IllegalArgumentException e) {
            App.mostrarError("No se pudo modificar", e.getMessage());
        }
    }

    @FXML
    private void habilitarPelicula() {
        Pelicula seleccionada = tablaPeliculas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarMensaje("Seleccione una película de la tabla.");
            return;
        }
        try {
            cinema.habilitarPelicula(seleccionada.getCodigo());
            refrescarTabla();
            mostrarMensaje("Película " + seleccionada.getCodigo() + " habilitada.");
        } catch (DomainException e) {
            App.mostrarError("No se pudo habilitar", e.getMessage());
        }
    }

    @FXML
    private void deshabilitarPelicula() {
        Pelicula seleccionada = tablaPeliculas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarMensaje("Seleccione una película de la tabla.");
            return;
        }
        try {
            cinema.deshabilitarPelicula(seleccionada.getCodigo());
            refrescarTabla();
            mostrarMensaje("Película " + seleccionada.getCodigo() + " deshabilitada.");
        } catch (DomainException e) {
            App.mostrarError("No se pudo deshabilitar", e.getMessage());
        }
    }

    @FXML
    private void limpiarFormulario() {
        txtNombre.clear();
        txtGenero.clear();
        txtDuracion.clear();
        cmbClasificacion.setValue(null);
        tablaPeliculas.getSelectionModel().clearSelection();
    }

    @FXML
    private void volverAlMenu() {
        App.cambiarVista("menu-view.fxml");
    }

    @FXML
    private void irASalas() {
        App.cambiarVista("sala-view.fxml");
    }

    @FXML
    private void irAFunciones() {
        App.cambiarVista("funcion-view.fxml");
    }

    private void refrescarTabla() {
        datos.setAll(cinema.consultarPeliculas());
        actualizarBotones(tablaPeliculas.getSelectionModel().getSelectedItem());
    }

    private void cargarFormulario(Pelicula pelicula) {
        if (pelicula == null) {
            return;
        }
        txtNombre.setText(pelicula.getNombre());
        txtGenero.setText(pelicula.getGenero());
        txtDuracion.setText(String.valueOf(pelicula.getDuracionMinutos()));
        cmbClasificacion.setValue(pelicula.getClasificacion());
    }

    private void actualizarBotones(Pelicula seleccionada) {
        boolean hayFila = seleccionada != null;
        btnHabilitar.setDisable(!hayFila || seleccionada.estaDisponible());
        btnDeshabilitar.setDisable(!hayFila || !seleccionada.estaDisponible());
    }

    private void mostrarMensaje(String texto) {
        lblMensaje.setText(texto);
    }

    private String leerNombre() {
        String texto = txtNombre.getText().trim();
        if (texto.isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        return texto;
    }

    private String leerGenero() {
        String texto = txtGenero.getText().trim();
        if (texto.isEmpty()) {
            throw new IllegalArgumentException("El género es obligatorio");
        }
        return texto;
    }

    private int leerDuracion() {
        try {
            return Integer.parseInt(txtDuracion.getText().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("La duración debe ser un número entero");
        }
    }

    private Clasificacion leerClasificacion() {
        Clasificacion clasificacion = cmbClasificacion.getValue();
        if (clasificacion == null) {
            throw new IllegalArgumentException("Seleccione una clasificación");
        }
        return clasificacion;
    }
}
