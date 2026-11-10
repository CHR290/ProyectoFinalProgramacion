package uniquindio.edu.co.proyectofinal.controller;
import uniquindio.edu.co.proyectofinal.service.Cinema;

public abstract class ControladorBase {

    protected Cinema cinema;

    public void setCinema(Cinema cinema) {
        this.cinema = cinema;
        alCargarCinema();
    }

    protected void alCargarCinema() {
    }
}
