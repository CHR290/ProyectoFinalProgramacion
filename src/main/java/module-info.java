module uniquindio.edu.co.proyectofinal {
    requires javafx.controls;
    requires javafx.fxml;

    exports uniquindio.edu.co.proyectofinal;
    exports uniquindio.edu.co.proyectofinal.model;
    exports uniquindio.edu.co.proyectofinal.model.enums;
    exports uniquindio.edu.co.proyectofinal.service;
    exports uniquindio.edu.co.proyectofinal.exception;

    opens uniquindio.edu.co.proyectofinal.controller to javafx.fxml;
}