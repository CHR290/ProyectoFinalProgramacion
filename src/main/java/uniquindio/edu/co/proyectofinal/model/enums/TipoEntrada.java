package uniquindio.edu.co.proyectofinal.model.enums;

public enum TipoEntrada {
    GENERAL(1.00, null),
    ESTUDIANTE(0.80, TipoCliente.ESTUDIANTE),
    NINO(0.60, TipoCliente.NINO),
    ADULTO_MAYOR(0.70, TipoCliente.ADULTO_MAYOR);

    private final double factor;
    private final TipoCliente perfilRequerido; // null: cualquier cliente puede comprarla

    TipoEntrada(double factor, TipoCliente perfilRequerido) {
        this.factor = factor;
        this.perfilRequerido = perfilRequerido;
    }

    public double getFactor() {
        return factor;
    }

    public TipoCliente getPerfilRequerido() {
        return perfilRequerido;
    }

    public boolean permitidaPara(TipoCliente cliente) {
        return perfilRequerido == null || perfilRequerido == cliente;
    }

    public double calcularValor(double valorBase) {
        return Math.round(valorBase * factor);
    }
}