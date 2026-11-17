package U2T2.logica;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Command: invocador. Ejecuta los comandos, guarda el historial de los exitosos
 * y permite deshacer la ultima operacion.
 */
public class InvocadorComandos {
    private final Deque<ComandoEstudiante> historial = new ArrayDeque<>();

    public String ejecutar(ComandoEstudiante comando) {
        String resultado = comando.ejecutar();
        if (comando.fueExitoso()) {
            historial.push(comando);
        }
        return resultado;
    }

    public String deshacerUltimo() {
        if (historial.isEmpty()) {
            return "No hay operaciones para deshacer.";
        }
        ComandoEstudiante comando = historial.pop();
        return "Deshacer [" + comando.getDescripcion() + "]: " + comando.deshacer();
    }

    public List<String> verHistorial() {
        List<String> lista = new ArrayList<>();
        historial.descendingIterator().forEachRemaining(c -> lista.add(c.getDescripcion()));
        return lista;
    }
}
