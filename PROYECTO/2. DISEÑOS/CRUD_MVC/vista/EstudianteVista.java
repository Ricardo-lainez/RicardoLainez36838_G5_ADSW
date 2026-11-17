package CRUD_MVC.vista;

import CRUD_MVC.modelo.Estudiante;
import java.util.List;
import java.util.Scanner;

/** Vista: solo muestra informacion y lee datos por consola. */
public class EstudianteVista {
    private final Scanner sc = new Scanner(System.in);

    public int mostrarMenu() {
        System.out.println("\n===== CRUD ESTUDIANTES (MVC) =====");
        System.out.println("1. Crear estudiante");
        System.out.println("2. Listar estudiantes");
        System.out.println("3. Buscar por ID");
        System.out.println("4. Actualizar estudiante");
        System.out.println("5. Eliminar estudiante");
        System.out.println("0. Salir");
        return leerEntero("Opcion: ");
    }

    public String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return sc.nextLine().trim();
    }

    public int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un numero entero.");
            }
        }
    }

    public void mostrarEstudiante(Estudiante e) {
        System.out.println(e);
    }

    public void mostrarLista(List<Estudiante> lista) {
        if (lista.isEmpty()) {
            System.out.println("No hay estudiantes registrados.");
            return;
        }
        System.out.println("--- Lista de estudiantes ---");
        lista.forEach(System.out::println);
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }
}
