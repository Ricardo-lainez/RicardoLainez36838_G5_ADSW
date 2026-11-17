package U2T2.logica;

import U2T2.modelo.Estudiante;

/** Command concreto: actualizar un estudiante. Guarda los datos anteriores para deshacer. */
public class ActualizarEstudianteComando implements ComandoEstudiante {
    private final ControlEstudiante receptor;
    private final String id;
    private final String nuevoNombre;
    private final int nuevaEdad;
    private Estudiante estadoAnterior;
    private boolean exitoso;

    public ActualizarEstudianteComando(ControlEstudiante receptor, String id, String nuevoNombre, int nuevaEdad) {
        this.receptor = receptor;
        this.id = id;
        this.nuevoNombre = nuevoNombre;
        this.nuevaEdad = nuevaEdad;
    }

    @Override
    public String ejecutar() {
        estadoAnterior = receptor.obtenerCopia(id);
        String resultado = receptor.actualizarEstudiante(id, nuevoNombre, nuevaEdad);
        exitoso = !resultado.startsWith("Error");
        return resultado;
    }

    @Override
    public String deshacer() {
        return receptor.revertirActualizacion(estadoAnterior);
    }

    @Override
    public boolean fueExitoso() { return exitoso; }

    @Override
    public String getDescripcion() { return "Actualizar ID " + id; }
}
