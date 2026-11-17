package CRUD_MVC.modelo;

import java.util.ArrayList;
import java.util.List;

/** Modelo: guarda los estudiantes en memoria y genera el ID automaticamente. */
public class EstudianteModelo {
    private final List<Estudiante> estudiantes = new ArrayList<>();
    private int siguienteId = 1;

    public Estudiante crear(String nombre, int edad) {
        Estudiante e = new Estudiante(siguienteId++, nombre, edad);
        estudiantes.add(e);
        return e;
    }

    public List<Estudiante> listar() {
        return new ArrayList<>(estudiantes);
    }

    public Estudiante buscarPorId(int id) {
        for (Estudiante e : estudiantes) {
            if (e.getId() == id) return e;
        }
        return null;
    }

    /** Solo cambia nombre y edad. El ID nunca se modifica. */
    public boolean actualizar(int id, String nombre, int edad) {
        Estudiante e = buscarPorId(id);
        if (e == null) return false;
        e.setNombre(nombre);
        e.setEdad(edad);
        return true;
    }

    public boolean eliminar(int id) {
        return estudiantes.removeIf(e -> e.getId() == id);
    }
}
