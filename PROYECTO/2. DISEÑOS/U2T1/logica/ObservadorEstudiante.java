package U2T1.logica;

/** Observer: contrato comun de todos los observadores del CRUD. */
public interface ObservadorEstudiante {
    void actualizar(EventoEstudiante evento);
}
