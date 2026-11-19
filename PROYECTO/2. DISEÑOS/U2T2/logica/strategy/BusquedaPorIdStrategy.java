package U2T2.logica.strategy;

import U2T2.modelo.Estudiante;
import java.util.List;
import java.util.stream.Collectors;

/** Strategy concreta: busca por ID ESPE completo o parcial (ej. "L004" o "434011"). */
public class BusquedaPorIdStrategy implements EstrategiaBusquedaEstudiante {
    @Override
    public List<Estudiante> buscar(List<Estudiante> estudiantes, String criterio) {
        String texto = criterio.trim().toUpperCase();
        return estudiantes.stream()
                // contains: sirve con el ID completo o con una parte
                .filter(e -> e.getId().contains(texto))
                .collect(Collectors.toList());
    }

    @Override
    public String getNombre() { return "por ID"; }
}
