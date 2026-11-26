package bocatto.modelo;

/** Resultado de una operacion: si salio bien y el mensaje para la pantalla. */
public class Resultado {
    private final boolean exito;
    private final String mensaje;

    public Resultado(boolean exito, String mensaje) {
        this.exito = exito;
        this.mensaje = mensaje;
    }

    public boolean isExito() { return exito; }
    public String getMensaje() { return mensaje; }

    @Override
    public String toString() { return (exito ? "OK: " : "ERROR: ") + mensaje; }
}
