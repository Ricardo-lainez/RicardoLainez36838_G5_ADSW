package U2T2.logica.command;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Command: invocador. Ejecuta los comandos, guarda el historial de los exitosos
 * y permite deshacer la ultima operacion.
 */
public class InvocadorComandos {
    // pila: el ultimo que entra es el primero que se deshace
    private final Deque<ComandoEstudiante> historial = new ArrayDeque<>();

    public String ejecutar(ComandoEstudiante comando) {
        // no sabe si es registrar, actualizar o eliminar, solo ejecuta
        String resultado = comando.ejecutar();
        if (comando.fueExitoso()) {
            historial.push(comando);  // solo guardamos los que salieron bien
        }
        return resultado;
    }

    public String deshacerUltimo() {
        if (historial.isEmpty()) {
            return "No hay operaciones para deshacer.";
        }
        ComandoEstudiante comando = historial.pop();  // saca el ultimo comando y lo deshace
        return "Deshacer [" + comando.getDescripcion() + "]: " + comando.deshacer();
    }

    public List<String> verHistorial() {
        List<String> lista = new ArrayList<>();
        historial.descendingIterator().forEachRemaining(c -> lista.add(c.getDescripcion()));
        return lista;
    }
}
