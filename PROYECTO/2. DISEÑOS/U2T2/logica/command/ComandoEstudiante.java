package U2T2.logica.command;

/** Command: contrato de cada operacion del CRUD encapsulada como objeto. */
public interface ComandoEstudiante {
    String ejecutar();  // hace la operacion
    String deshacer();  // la revierte
    boolean fueExitoso();  // si fallo no se guarda en el historial
    String getDescripcion();
}
