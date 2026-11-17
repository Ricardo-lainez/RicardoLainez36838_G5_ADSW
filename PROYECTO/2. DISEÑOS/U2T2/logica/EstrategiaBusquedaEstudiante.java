package U2T2.logica;

import U2T2.modelo.Estudiante;
import java.util.List;

/** Strategy: interfaz comun de los algoritmos de busqueda. */
public interface EstrategiaBusquedaEstudiante {
    List<Estudiante> buscar(List<Estudiante> estudiantes, String criterio);
    String getNombre();
}
