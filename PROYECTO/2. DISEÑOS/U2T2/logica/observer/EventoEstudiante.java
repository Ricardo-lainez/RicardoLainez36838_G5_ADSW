package U2T2.logica.observer;

import U2T2.modelo.Estudiante;

/** Observer: datos que reciben los observadores cuando ocurre un evento del CRUD. */
public class EventoEstudiante {
    // que paso: registrado, actualizado, eliminado o deshecho
    private final TipoEventoEstudiante tipo;
    private final Estudiante estudiante;
    private final String mensaje;

    public EventoEstudiante(TipoEventoEstudiante tipo, Estudiante estudiante, String mensaje) {
        this.tipo = tipo;
        this.estudiante = estudiante;
        this.mensaje = mensaje;
    }

    public TipoEventoEstudiante getTipo() { return tipo; }
    public Estudiante getEstudiante() { return estudiante; }
    public String getMensaje() { return mensaje; }
}
