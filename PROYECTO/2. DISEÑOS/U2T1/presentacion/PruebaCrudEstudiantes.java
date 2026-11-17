package U2T1.presentacion;

import U2T1.logica.ControlEstudiante;

/**
 * CAPA: Presentacion
 * Prueba de consola del CRUD base (RF-01 a RF-05), sin patrones.
 */
public class PruebaCrudEstudiantes {
    public static void main(String[] args) {
        ControlEstudiante control = new ControlEstudiante();

        System.out.println("=== RF-01 Registrar ===");
        System.out.println(control.agregarEstudiante(1, "Ana Torres", 20));
        System.out.println(control.agregarEstudiante(2, "Luis Perez", 17));

        System.out.println("\n=== RF-05 Validar ===");
        System.out.println(control.agregarEstudiante(3, "", 19));
        System.out.println(control.agregarEstudiante(1, "Repetido", 22));

        System.out.println("\n=== RF-04 Consultar ===");
        control.listarTodos().forEach(System.out::println);

        System.out.println("\n=== RF-02 Actualizar ===");
        System.out.println(control.actualizarEstudiante(1, "Ana Torres Actualizada", 21));

        System.out.println("\n=== RF-03 Eliminar ===");
        System.out.println(control.eliminarEstudiante(2));
        System.out.println(control.eliminarEstudiante(1));

        System.out.println("\n=== Lista final ===");
        control.listarTodos().forEach(System.out::println);
    }
}
