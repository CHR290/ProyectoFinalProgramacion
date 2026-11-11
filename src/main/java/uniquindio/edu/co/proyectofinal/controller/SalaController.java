package uniquindio.edu.co.proyectofinal.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import uniquindio.edu.co.proyectofinal.App;
import uniquindio.edu.co.proyectofinal.exception.DomainException;
import uniquindio.edu.co.proyectofinal.model.Sala;
import uniquindio.edu.co.proyectofinal.model.enums.EstadoSala;

public class SalaController extends ControladorBase {

    @FXML private TableView<Sala> tablaSalas;
    @FXML private TableColumn<Sala, String> colCodigo;
    @FXML private TableColumn<Sala, String> colNombre;
    @FXML private TableColumn<Sala, Integer> colCapacidad;
    @FXML private TableColumn<Sala, EstadoSala> colEstado;

    @FXML private TextField txtNombre;
    @FXML private TextField txtCapacidad;
    @FXML private Button btnHabilitar;
    @FXML private Button btnDeshabilitar;
    @FXML private Label lblMensaje;

    private final ObservableList<Sala> datos = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCapacidad.setCellValueFactory(new PropertyValueFactory<>("capacidad"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        tablaSalas.setItems(datos);
        tablaSalas.getSelectionModel().selectedItemProperty()
                .addListener((obs, anterior, seleccionada) -> {
                    cargarFormulario(seleccionada);
                    actualizarBotones(seleccionada);
                });
    }

    @Override
    protected void alCargarCinema() {
        refrescarTabla();
    }

    @FXML
    private void registrarSala() {
        try {
            Sala nueva = cinema.registrarSala(leerNombre(), leerCapacidad());
            refrescarTabla();
            limpiarFormulario();
            mostrarMensaje("Sala " + nueva.getCodigo() + " registrada.");
        } catch (DomainException | IllegalArgumentException e) {
            App.mostrarError("No se pudo registrar", e.getMessage());
        }
    }

    @FXML
    private void modificarSala() {
        Sala seleccionada = tablaSalas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarMensaje("Seleccione una sala de la tabla.");
            return;
        }
        try {
            cinema.modificarSala(seleccionada.getCodigo(), leerNombre(), leerCapacidad());
            refrescarTabla();
            mostrarMensaje("Sala " + seleccionada.getCodigo() + " modificada.");
        } catch (DomainException | IllegalArgumentException e) {
            App.mostrarError("No se pudo modificar", e.getMessage());
        }
    }

    @FXML
    private void habilitarSala() {
        Sala seleccionada = tablaSalas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarMensaje("Seleccione una sala de la tabla.");
            return;
        }
        try {
            cinema.habilitarSala(seleccionada.getCodigo());
            refrescarTabla();
            mostrarMensaje("Sala " + seleccionada.getCodigo() + " habilitada.");
        } catch (DomainException e) {
            App.mostrarError("No se pudo habilitar", e.getMessage());
        }
    }

    @FXML
    private void deshabilitarSala() {
        Sala seleccionada = tablaSalas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarMensaje("Seleccione una sala de la tabla.");
            return;
        }
        try {
            cinema.deshabilitarSala(seleccionada.getCodigo());
            refrescarTabla();
            mostrarMensaje("Sala " + seleccionada.getCodigo() + " deshabilitada.");
        } catch (DomainException e) {
            App.mostrarError("No se pudo deshabilitar", e.getMessage());
        }
    }

    @FXML
    private void limpiarFormulario() {
        txtNombre.clear();
        txtCapacidad.clear();
        tablaSalas.getSelectionModel().clearSelection();
    }

    @FXML
    private void irAPeliculas() {
        App.cambiarVista("admin-view.fxml");
    }

    @FXML
    private void volverAlMenu() {
        App.cambiarVista("menu-view.fxml");
    }

    private void refrescarTabla() {
        datos.setAll(cinema.consultarSalas());
        actualizarBotones(tablaSalas.getSelectionModel().getSelectedItem());
    }

    private void cargarFormulario(Sala sala) {
        if (sala == null) {
            return;
        }
        txtNombre.setText(sala.getNombre());
        txtCapacidad.setText(String.valueOf(sala.getCapacidad()));
    }

    private void actualizarBotones(Sala seleccionada) {
        boolean hayFila = seleccionada != null;
        btnHabilitar.setDisable(!hayFila || seleccionada.estaDisponible());
        btnDeshabilitar.setDisable(!hayFila || !seleccionada.estaDisponible());
    }

    private void mostrarMensaje(String texto) {
        lblMensaje.setText(texto);
    }

    // ===== Validación de formulario (solo formato) =====

    private String leerNombre() {
        String texto = txtNombre.getText().trim();
        if (texto.isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        return texto;
    }

    private int leerCapacidad() {
        try {
            return Integer.parseInt(txtCapacidad.getText().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("La capacidad debe ser un número entero");
        }
    }
}