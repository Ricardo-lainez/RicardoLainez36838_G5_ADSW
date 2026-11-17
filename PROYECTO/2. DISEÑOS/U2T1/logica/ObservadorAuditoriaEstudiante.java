package U2T1.logica;

import java.util.ArrayList;
import java.util.List;

/** Observer concreto: guarda cada evento del CRUD en una bitacora. */
public class ObservadorAuditoriaEstudiante implements ObservadorEstudiante {
    private final List<String> bitacora = new ArrayList<>();

    @Override
    public void actualizar(EventoEstudiante evento) {
        bitacora.add("[" + evento.getTipo() + "] " + evento.getMensaje());
    }

    public List<String> getBitacora() {
        return new ArrayList<>(bitacora);
    }
}
