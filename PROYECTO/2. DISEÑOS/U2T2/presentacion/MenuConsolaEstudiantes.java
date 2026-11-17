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
import U2T2.modelo.Estudiante;
import java.util.List;
import java.util.Scanner;

/**
 * CAPA: Presentacion
 * Menu interactivo por consola. Solo captura datos y muestra resultados:
 * las operaciones pasan por el invocador de comandos y el control de la logica.
 */
public class MenuConsolaEstudiantes {
    private static final Scanner entrada = new Scanner(System.in);

    public static void main(String[] args) {
        ControlEstudiante control = new ControlEstudiante();
        InvocadorComandos invocador = new InvocadorComandos();
        ObservadorAuditoriaEstudiante auditoria = new ObservadorAuditoriaEstudiante();
        control.agregarObservador(auditoria);
        control.agregarObservador(new ObservadorNotificacionEstudiante());

        int opcion;
        do {
            System.out.println("\n===== CRUD ESTUDIANTES (Observer, Strategy, Command) =====");
            System.out.println("1. Registrar estudiante");
            System.out.println("2. Listar estudiantes");
            System.out.println("3. Actualizar estudiante");
            System.out.println("4. Eliminar estudiante");
            System.out.println("5. Buscar estudiantes (estrategia actual: " + control.getNombreEstrategia() + ")");
            System.out.println("6. Cambiar estrategia de busqueda");
            System.out.println("7. Deshacer ultima operacion");
            System.out.println("8. Ver historial de comandos");
            System.out.println("9. Ver bitacora de auditoria");
            System.out.println("0. Salir");
            opcion = leerEntero("Opcion: ");

            switch (opcion) {
                case 1:
                    String id = leerTexto("ID ESPE (ej. L00434011): ");
                    String nombre = leerTexto("Nombre: ");
                    int edad = leerEntero("Edad: ");
                    System.out.println(invocador.ejecutar(new RegistrarEstudianteComando(control, id, nombre, edad)));
                    break;
                case 2:
                    mostrar(control.listarTodos());
                    break;
                case 3:
                    String idAct = leerTexto("ID a actualizar: ");
                    String nuevoNombre = leerTexto("Nuevo nombre: ");
                    int nuevaEdad = leerEntero("Nueva edad: ");
                    System.out.println(invocador.ejecutar(new ActualizarEstudianteComando(control, idAct, nuevoNombre, nuevaEdad)));
                    break;
                case 4:
                    String idEli = leerTexto("ID a eliminar: ");
                    System.out.println(invocador.ejecutar(new EliminarEstudianteComando(control, idEli)));
                    break;
                case 5:
                    mostrar(control.buscarEstudiantes(leerTexto("Criterio: ")));
                    break;
                case 6:
                    System.out.println("1. Por ID   2. Por Nombre   3. Por Rango de edad (ej. 18-25)");
                    int tipo = leerEntero("Estrategia: ");
                    if (tipo == 1) control.cambiarEstrategiaBusqueda(new BusquedaPorIdStrategy());
                    else if (tipo == 2) control.cambiarEstrategiaBusqueda(new BusquedaPorNombreStrategy());
                    else if (tipo == 3) control.cambiarEstrategiaBusqueda(new BusquedaPorRangoEdadStrategy());
                    System.out.println("Estrategia actual: " + control.getNombreEstrategia());
                    break;
                case 7:
                    System.out.println(invocador.deshacerUltimo());
                    break;
                case 8:
                    System.out.println("Historial: " + invocador.verHistorial());
                    break;
                case 9:
                    auditoria.getBitacora().forEach(linea -> System.out.println("   " + linea));
                    break;
                case 0:
                    System.out.println("Fin del programa.");
                    break;
                default:
                    System.out.println("Opcion no valida.");
            }
        } while (opcion != 0);
    }

    private static void mostrar(List<Estudiante> lista) {
        if (lista.isEmpty()) {
            System.out.println("Sin resultados.");
        } else {
            lista.forEach(System.out::println);
        }
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String linea = entrada.nextLine().trim();
            try {
                return Integer.parseInt(linea);
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un numero entero.");
            }
        }
    }

    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return entrada.nextLine();
    }
}
