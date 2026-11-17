package U2T2.logica;

/** Observer: contrato del sujeto que registra, quita y notifica observadores. */
public interface SujetoEventosEstudiante {
    void agregarObservador(ObservadorEstudiante observador);
    void quitarObservador(ObservadorEstudiante observador);
    void notificarObservadores(EventoEstudiante evento);
}
