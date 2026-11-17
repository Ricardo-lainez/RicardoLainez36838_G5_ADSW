package U2T1.logica;

import U2T1.modelo.Estudiante;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/** Strategy concreta: busca el estudiante con el ID exacto. */
public class BusquedaPorIdStrategy implements EstrategiaBusquedaEstudiante {
    @Override
    public List<Estudiante> buscar(List<Estudiante> estudiantes, String criterio) {
        try {
            int id = Integer.parseInt(criterio.trim());
            return estudiantes.stream().filter(e -> e.getId() == id).collect(Collectors.toList());
        } catch (NumberFormatException ex) {
            return new ArrayList<>();
        }
    }

    @Override
    public String getNombre() { return "por ID"; }
}
