package U2T2.datos;

import U2T2.modelo.Estudiante;
import java.util.ArrayList;
import java.util.List;

/**
 * CAPA: Datos
 * Responsabilidad: almacenar y gestionar la coleccion de estudiantes en memoria.
 * No aplica reglas de negocio ni validaciones.
 */
public class RepositorioEstudiante {
    private final List<Estudiante> estudiantes = new ArrayList<>();

    public boolean existeId(String id) {
        return estudiantes.stream().anyMatch(e -> e.getId().equals(id));
    }

    public void guardar(Estudiante estudiante) {
        estudiantes.add(estudiante);
    }

    public Estudiante buscarPorId(String id) {
        return estudiantes.stream().filter(e -> e.getId().equals(id)).findFirst().orElse(null);
    }

    public void actualizar(Estudiante estudiante) {
        Estudiante existente = buscarPorId(estudiante.getId());
        if (existente != null) {
            existente.setNombre(estudiante.getNombre());
            existente.setEdad(estudiante.getEdad());
        }
    }

    public void eliminar(String id) {
        estudiantes.removeIf(e -> e.getId().equals(id));
    }

    public List<Estudiante> listarTodos() {
        return new ArrayList<>(estudiantes);
    }
}
