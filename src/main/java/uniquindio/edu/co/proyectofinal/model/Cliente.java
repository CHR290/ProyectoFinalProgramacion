package uniquindio.edu.co.proyectofinal.model;

import uniquindio.edu.co.proyectofinal.model.enums.TipoCliente;

public class Cliente {

    private String identificacion;
    private String nombre;
    private TipoCliente tipoCliente;

    public Cliente(String identificacion, String nombre, TipoCliente tipoCliente) {
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.tipoCliente = tipoCliente;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoCliente getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(TipoCliente tipoCliente) {
        this.tipoCliente = tipoCliente;
    }
}
