package uniquindio.edu.co.proyectofinal.model.enums;

public enum TipoEntrada {
    GENERAL(18000, null),
    ESTUDIANTE(15000, TipoCliente.ESTUDIANTE),
    NIÑO(10000, TipoCliente.NINO),
    ADULTO_MAYOR(12000, TipoCliente.ADULTO_MAYOR);

    private final double precio;
    private final TipoCliente perfilRequerido;

    TipoEntrada(double precio, TipoCliente perfilRequerido) {
        this.precio = precio;
        this.perfilRequerido = perfilRequerido;
    }

    public double getPrecio() {
        return precio;
    }

    public TipoCliente getPerfilRequerido() {
        return perfilRequerido;
    }

    public boolean permitidaPara(TipoCliente cliente) {
        return perfilRequerido == null || perfilRequerido == cliente;
    }
}