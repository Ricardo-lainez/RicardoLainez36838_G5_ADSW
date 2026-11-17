package U2T2.logica;

/** Command: contrato de cada operacion del CRUD encapsulada como objeto. */
public interface ComandoEstudiante {
    String ejecutar();
    String deshacer();
    boolean fueExitoso();
    String getDescripcion();
}
