package U2T1.pruebas;

import U2T1.controlador.EstudianteControlador;
import U2T1.modelo.EstudianteModelo;

/** Prueba por consola de las operaciones del CRUD (RF-01 a RF-05). */
public class PruebaCrudEstudiantes {
    public static void main(String[] args) {
        EstudianteControlador c = new EstudianteControlador(new EstudianteModelo());

        System.out.println("=== RF-01 Registrar ===");
        System.out.println(c.registrar("L00434011", "Marco Padilla", "21"));
        System.out.println(c.registrar("l00434012", "Ricardo Lainez", "22"));

        System.out.println("\n=== RF-05 Validar ===");
        System.out.println(c.registrar("12345", "ID Malo", "20"));
        System.out.println(c.registrar("L00434011", "Repetido", "20"));
        System.out.println(c.registrar("L00434013", "", "20"));

        System.out.println("\n=== RF-04 Consultar ===");
        c.listar().forEach(System.out::println);

        System.out.println("\n=== RF-02 Actualizar ===");
        System.out.println(c.actualizar("L00434011", "Marco Adrian Padilla", "22"));

        System.out.println("\n=== RF-04 Buscar por ID ===");
        c.buscarPorId("434012").forEach(System.out::println);

        System.out.println("\n=== RF-03 Eliminar ===");
        System.out.println(c.eliminar("L00434012"));

        System.out.println("\n=== Lista final ===");
        c.listar().forEach(System.out::println);
    }
}
