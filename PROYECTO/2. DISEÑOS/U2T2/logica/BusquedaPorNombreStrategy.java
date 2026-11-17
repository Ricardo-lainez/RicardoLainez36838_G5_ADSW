package U2T2.logica;

import U2T2.modelo.Estudiante;
import java.util.List;
import java.util.stream.Collectors;

/** Strategy concreta: busca por coincidencia parcial del nombre, sin importar mayusculas. */
public class BusquedaPorNombreStrategy implements EstrategiaBusquedaEstudiante {
    @Override
    public List<Estudiante> buscar(List<Estudiante> estudiantes, String criterio) {
        String texto = criterio.trim().toLowerCase();
        return estudiantes.stream()
                .filter(e -> e.getNombre().toLowerCase().contains(texto))
                .collect(Collectors.toList());
    }

    @Override
    public String getNombre() { return "por Nombre"; }
}
