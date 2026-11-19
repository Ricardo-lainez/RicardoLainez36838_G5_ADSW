package U2T1.modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * MODELO: guarda y gestiona la coleccion de estudiantes en memoria.
 * No conoce la vista ni valida datos; eso lo hace el controlador.
 */
public class EstudianteModelo {
    // los datos viven aqui, en memoria
    private final List<Estudiante> estudiantes = new ArrayList<>();

    public boolean existeId(String id) {
        return buscarPorId(id) != null;
    }

    public void guardar(Estudiante estudiante) {
        estudiantes.add(estudiante);
    }

    public Estudiante buscarPorId(String id) {
        for (Estudiante e : estudiantes) {
            if (e.getId().equals(id)) return e;
        }
        return null;
    }

    /** Solo cambia nombre y edad. El ID nunca se modifica. */
    public void actualizar(String id, String nombre, int edad) {
        Estudiante e = buscarPorId(id);
        if (e != null) {
            e.setNombre(nombre);  // solo nombre y edad, el ID se queda igual
            e.setEdad(edad);
        }
    }

    public void eliminar(String id) {
        estudiantes.removeIf(e -> e.getId().equals(id));
    }

    public List<Estudiante> listarTodos() {
        return new ArrayList<>(estudiantes);
    }
}
