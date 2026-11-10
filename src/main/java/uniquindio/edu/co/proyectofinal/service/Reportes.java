package uniquindio.edu.co.proyectofinal.service;

public class Reportes {

    private Cinema cinema;

    public Reportes(Cinema cinema) {
        this.cinema = cinema;
    }

    public Cinema getCine() {
        return cinema;
    }

    public void setCine(Cinema cinema) {
        this.cinema = cinema;
    }
}
