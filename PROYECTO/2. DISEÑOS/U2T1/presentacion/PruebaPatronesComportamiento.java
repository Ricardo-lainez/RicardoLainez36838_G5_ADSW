package U2T1.presentacion;

import U2T1.logica.ActualizarEstudianteComando;
import U2T1.logica.BusquedaPorIdStrategy;
import U2T1.logica.BusquedaPorNombreStrategy;
import U2T1.logica.BusquedaPorRangoEdadStrategy;
import U2T1.logica.ControlEstudiante;
import U2T1.logica.EliminarEstudianteComando;
import U2T1.logica.InvocadorComandos;
import U2T1.logica.ObservadorAuditoriaEstudiante;
import U2T1.logica.ObservadorNotificacionEstudiante;
import U2T1.logica.RegistrarEstudianteComando;

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
        System.out.println(invocador.ejecutar(new RegistrarEstudianteComando(control, 1, "Ana Torres", 20)));
        System.out.println(invocador.ejecutar(new RegistrarEstudianteComando(control, 2, "Luis Perez", 17)));
        System.out.println(invocador.ejecutar(new RegistrarEstudianteComando(control, 3, "Maria Andrade", 23)));
        System.out.println(invocador.ejecutar(new RegistrarEstudianteComando(control, 4, "", 19)));

        System.out.println("\n=== COMMAND + OBSERVER: Actualizar (RF-02) ===");
        System.out.println(invocador.ejecutar(new ActualizarEstudianteComando(control, 1, "Ana Torres Vega", 21)));

        System.out.println("\n=== COMMAND + OBSERVER: Eliminar (RF-03) ===");
        System.out.println(invocador.ejecutar(new EliminarEstudianteComando(control, 2)));
        System.out.println(invocador.ejecutar(new EliminarEstudianteComando(control, 3)));

        System.out.println("\n=== STRATEGY: Buscar (RF-06) ===");
        control.cambiarEstrategiaBusqueda(new BusquedaPorIdStrategy());
        System.out.println("Estrategia " + control.getNombreEstrategia() + ", criterio '1':");
        control.buscarEstudiantes("1").forEach(e -> System.out.println("   " + e));

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
