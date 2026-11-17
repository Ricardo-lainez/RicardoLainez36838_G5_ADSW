package U2T2.logica;

/** Observer concreto: muestra un aviso en consola cada vez que ocurre un evento. */
public class ObservadorNotificacionEstudiante implements ObservadorEstudiante {
    private String ultimaNotificacion = "Sin notificaciones";

    @Override
    public void actualizar(EventoEstudiante evento) {
        String accion = evento.getTipo() == TipoEventoEstudiante.DESHECHO
                ? "operacion deshecha"
                : "estudiante " + evento.getTipo().name().toLowerCase();
        ultimaNotificacion = "Notificacion: " + accion + " -> " + evento.getEstudiante();
        System.out.println("   " + ultimaNotificacion);
    }

    public String getUltimaNotificacion() {
        return ultimaNotificacion;
    }
}
