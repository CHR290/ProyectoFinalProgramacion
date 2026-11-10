package uniquindio.edu.co.proyectofinal.controller;

import javafx.fxml.FXML;
import uniquindio.edu.co.proyectofinal.App;

public class MenuController extends ControladorBase {

    @FXML
    private void irAOperador() {
        App.cambiarVista("operador-view.fxml");
    }

    @FXML
    private void irAAdministrador() {
        App.cambiarVista("admin-view.fxml");
    }
}
