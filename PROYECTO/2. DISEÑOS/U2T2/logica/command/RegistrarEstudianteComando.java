package U2T2.logica.command;

import U2T2.logica.ControlEstudiante;

/** Command concreto: registrar un estudiante. Deshacer lo quita del registro. */
public class RegistrarEstudianteComando implements ComandoEstudiante {
    private final ControlEstudiante receptor;
    private final String id;
    private final String nombre;
    private final int edad;
    private boolean exitoso;

    public RegistrarEstudianteComando(ControlEstudiante receptor, String id, String nombre, int edad) {
        this.receptor = receptor;
        this.id = id;
        this.nombre = nombre;
        this.edad = edad;
    }

    @Override
    public String ejecutar() {
        String resultado = receptor.agregarEstudiante(id, nombre, edad);
        exitoso = !resultado.startsWith("Error");
        return resultado;
    }

    @Override
    public String deshacer() {
        return receptor.revertirRegistro(id);
    }

    @Override
    public boolean fueExitoso() { return exitoso; }

    @Override
    public String getDescripcion() { return "Registrar ID " + id + " (" + nombre + ")"; }
}
