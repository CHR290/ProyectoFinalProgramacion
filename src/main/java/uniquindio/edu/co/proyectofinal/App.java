package uniquindio.edu.co.proyectofinal;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import uniquindio.edu.co.proyectofinal.controller.ControladorBase;
import uniquindio.edu.co.proyectofinal.service.Cinema;
import uniquindio.edu.co.proyectofinal.service.DatosEjemplo;

import java.io.IOException;
import java.net.URL;

public class App extends Application {

    private static final String RUTA_VISTAS = "/uniquindio/edu/co/proyectofinal/view/";
    private static Stage stage;
    private static Cinema cinema;

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        cinema = new Cinema("CINEMAUQ");
        DatosEjemplo.cargar(cinema);

        cambiarVista("menu-view.fxml");
        stage.setTitle("CINEMAUQ");
        stage.show();
    }

    public static void cambiarVista(String nombreFxml) {
        URL url = App.class.getResource(RUTA_VISTAS + nombreFxml);
        if (url == null) {
            mostrarError("Vista no disponible",
                    "No existe la pantalla " + nombreFxml);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(url);
            Parent raiz = loader.load();

            Object controlador = loader.getController();
            if (controlador instanceof ControladorBase) {
                ((ControladorBase) controlador).setCinema(cinema);
            }

            stage.setScene(new Scene(raiz));
        } catch (IOException e) {
            mostrarError("Error de carga",
                    "No se pudo cargar la pantalla " + nombreFxml + ": " + e.getMessage());
        }
    }

    public static void mostrarError(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }
}