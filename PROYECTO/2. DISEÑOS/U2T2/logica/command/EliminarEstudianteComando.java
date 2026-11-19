package U2T2.logica.command;

import U2T2.logica.ControlEstudiante;
import U2T2.modelo.Estudiante;

/** Command concreto: eliminar un estudiante. Guarda una copia para poder restaurarlo. */
public class EliminarEstudianteComando implements ComandoEstudiante {
    private final ControlEstudiante receptor;
    private final String id;
    private Estudiante eliminado;
    private boolean exitoso;

    public EliminarEstudianteComando(ControlEstudiante receptor, String id) {
        this.receptor = receptor;
        this.id = id;
    }

    @Override
    public String ejecutar() {
        // copia del estudiante antes de borrarlo, sin esto no se podria deshacer
        eliminado = receptor.obtenerCopia(id);
        String resultado = receptor.eliminarEstudiante(id);
        exitoso = !resultado.startsWith("Error");
        return resultado;
    }

    @Override
    public String deshacer() {
        return receptor.revertirEliminacion(eliminado);  // lo vuelve a guardar con los mismos datos
    }

    @Override
    public boolean fueExitoso() { return exitoso; }

    @Override
    public String getDescripcion() { return "Eliminar ID " + id; }
}
