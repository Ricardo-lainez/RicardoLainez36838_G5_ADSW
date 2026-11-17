package U2T2.presentacion;

import U2T2.logica.ActualizarEstudianteComando;
import U2T2.logica.BusquedaPorIdStrategy;
import U2T2.logica.BusquedaPorNombreStrategy;
import U2T2.logica.BusquedaPorRangoEdadStrategy;
import U2T2.logica.ControlEstudiante;
import U2T2.logica.EliminarEstudianteComando;
import U2T2.logica.InvocadorComandos;
import U2T2.logica.ObservadorAuditoriaEstudiante;
import U2T2.logica.ObservadorNotificacionEstudiante;
import U2T2.logica.RegistrarEstudianteComando;

/**
 * CAPA: Presentacion
 * Prueba de consola de los patrones de comportamiento Observer, Strategy y Command.
 */
public class PruebaPatronesComportamiento {
    public static void main(String[] args) {
        ControlEstudiante control = new ControlEstudiante();
        InvocadorComandos invocador = new InvocadorComandos();

        // Observer: se suscriben dos observadores al control
        ObservadorAuditoriaEstudiante auditoria = new ObservadorAuditoriaEstudiante();
        ObservadorNotificacionEstudiante notificacion = new ObservadorNotificacionEstudiante();
        control.agregarObservador(auditoria);
        control.agregarObservador(notificacion);

        System.out.println("=== COMMAND + OBSERVER: Registrar (RF-01) ===");
        System.out.println(invocador.ejecutar(new RegistrarEstudianteComando(control, "L00434011", "Ana Torres", 20)));
        System.out.println(invocador.ejecutar(new RegistrarEstudianteComando(control, "L00434012", "Luis Perez", 17)));
        System.out.println(invocador.ejecutar(new RegistrarEstudianteComando(control, "L00434013", "Maria Andrade", 23)));
        System.out.println(invocador.ejecutar(new RegistrarEstudianteComando(control, "L00434014", "", 19)));

        System.out.println("\n=== COMMAND + OBSERVER: Actualizar (RF-02) ===");
        System.out.println(invocador.ejecutar(new ActualizarEstudianteComando(control, "L00434011", "Ana Torres Vega", 21)));

        System.out.println("\n=== COMMAND + OBSERVER: Eliminar (RF-03) ===");
        System.out.println(invocador.ejecutar(new EliminarEstudianteComando(control, "L00434012")));
        System.out.println(invocador.ejecutar(new EliminarEstudianteComando(control, "L00434013")));

        System.out.println("\n=== STRATEGY: Buscar (RF-06) ===");
        control.cambiarEstrategiaBusqueda(new BusquedaPorIdStrategy());
        System.out.println("Estrategia " + control.getNombreEstrategia() + ", criterio 'L00434011':");
        control.buscarEstudiantes("L00434011").forEach(e -> System.out.println("   " + e));

        control.cambiarEstrategiaBusqueda(new BusquedaPorNombreStrategy());
        System.out.println("Estrategia " + control.getNombreEstrategia() + ", criterio 'perez':");
        control.buscarEstudiantes("perez").forEach(e -> System.out.println("   " + e));

        control.cambiarEstrategiaBusqueda(new BusquedaPorRangoEdadStrategy());
        System.out.println("Estrategia " + control.getNombreEstrategia() + ", criterio '18-30':");
        control.buscarEstudiantes("18-30").forEach(e -> System.out.println("   " + e));

        System.out.println("\n=== COMMAND: Historial y deshacer (RF-07) ===");
        System.out.println("Historial: " + invocador.verHistorial());
        System.out.println(invocador.deshacerUltimo());
        System.out.println(invocador.deshacerUltimo());
        System.out.println("Historial: " + invocador.verHistorial());

        System.out.println("\n=== RF-04 Lista final ===");
        control.listarTodos().forEach(System.out::println);

        System.out.println("\n=== OBSERVER: Bitacora de auditoria ===");
        auditoria.getBitacora().forEach(linea -> System.out.println("   " + linea));
    }
}
