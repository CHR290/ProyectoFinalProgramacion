package uniquindio.edu.co.proyectofinal.model.enums;

public enum TipoEntrada {
    GENERAL(18000),
    ESTUDIANTE(15000),
    NINO(10000),
    ADULTO_MAYOR(12000);

    private double precio;

    TipoEntrada(double precio){
        this.precio = precio;
    }

    public double getPrecio() {
        return precio;
    }
}
