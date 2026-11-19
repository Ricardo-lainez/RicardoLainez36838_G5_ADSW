package U2T2.logica.observer;

/** Observer: contrato comun de todos los observadores del CRUD. */
public interface ObservadorEstudiante {
    void actualizar(EventoEstudiante evento);
}
