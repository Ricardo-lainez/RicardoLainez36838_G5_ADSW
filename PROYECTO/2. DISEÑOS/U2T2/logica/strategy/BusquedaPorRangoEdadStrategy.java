package U2T2.logica.strategy;

import U2T2.modelo.Estudiante;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/** Strategy concreta 3 de 3: busca estudiantes dentro de un rango de edad con formato "min-max". */
public class BusquedaPorRangoEdadStrategy implements EstrategiaBusquedaEstudiante {
    @Override
    public List<Estudiante> buscar(List<Estudiante> estudiantes, String criterio) {
        try {
            String[] partes = criterio.trim().split("-");  // "18-25" se separa en 18 y 25
            int min = Integer.parseInt(partes[0].trim());
            int max = Integer.parseInt(partes[1].trim());
            return estudiantes.stream()
                    .filter(e -> e.getEdad() >= min && e.getEdad() <= max)  // edad dentro del rango, incluye los extremos
                    .collect(Collectors.toList());
        } catch (RuntimeException ex) {
            // si escriben mal el rango no se cae, solo no encuentra nada
            return new ArrayList<>();
        }
    }

    @Override
    public String getNombre() { return "por Rango de edad"; }
}
