package U2T2.logica.strategy;

import U2T2.modelo.Estudiante;
import java.util.List;

/**
 * Strategy: interfaz comun de los algoritmos de busqueda.
 * El control solo conoce esta interfaz, no las clases concretas.
 */
public interface EstrategiaBusquedaEstudiante {
    // todas las busquedas tienen este mismo metodo, cada clase lo hace a su manera
    List<Estudiante> buscar(List<Estudiante> estudiantes, String criterio);
    String getNombre();  // nombre corto para mostrarlo en pantalla
}
