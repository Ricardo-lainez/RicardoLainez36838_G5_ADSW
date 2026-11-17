package CRUD_MVC.controlador;

import CRUD_MVC.modelo.Estudiante;
import CRUD_MVC.modelo.EstudianteModelo;
import CRUD_MVC.vista.EstudianteVista;

/** Controlador: recibe las opciones de la vista, valida y llama al modelo. */
public class EstudianteControlador {
    private final EstudianteModelo modelo;
    private final EstudianteVista vista;

    public EstudianteControlador(EstudianteModelo modelo, EstudianteVista vista) {
        this.modelo = modelo;
        this.vista = vista;
    }

    public void iniciar() {
        int opcion;
        do {
            opcion = vista.mostrarMenu();
            switch (opcion) {
                case 1: crear(); break;
                case 2: vista.mostrarLista(modelo.listar()); break;
                case 3: buscar(); break;
                case 4: actualizar(); break;
                case 5: eliminar(); break;
                case 0: vista.mostrarMensaje("Fin del programa."); break;
                default: vista.mostrarMensaje("Opcion no valida.");
            }
        } while (opcion != 0);
    }

    private boolean datosValidos(String nombre, int edad) {
        if (nombre.isEmpty()) {
            vista.mostrarMensaje("Error: el nombre no puede estar vacio.");
            return false;
        }
        if (edad <= 0) {
            vista.mostrarMensaje("Error: la edad debe ser mayor a 0.");
            return false;
        }
        return true;
    }

    private void crear() {
        String nombre = vista.leerTexto("Nombre: ");
        int edad = vista.leerEntero("Edad: ");
        if (!datosValidos(nombre, edad)) return;
        Estudiante e = modelo.crear(nombre, edad);
        vista.mostrarMensaje("Estudiante creado con ID " + e.getId() + ".");
    }

    private void buscar() {
        int id = vista.leerEntero("ID: ");
        Estudiante e = modelo.buscarPorId(id);
        if (e == null) vista.mostrarMensaje("No existe un estudiante con ID " + id + ".");
        else vista.mostrarEstudiante(e);
    }

    private void actualizar() {
        int id = vista.leerEntero("ID del estudiante a actualizar: ");
        Estudiante e = modelo.buscarPorId(id);
        if (e == null) {
            vista.mostrarMensaje("No existe un estudiante con ID " + id + ".");
            return;
        }
        vista.mostrarMensaje("Datos actuales: " + e + " (el ID no se puede modificar)");
        String nombre = vista.leerTexto("Nuevo nombre: ");
        int edad = vista.leerEntero("Nueva edad: ");
        if (!datosValidos(nombre, edad)) return;
        modelo.actualizar(id, nombre, edad);
        vista.mostrarMensaje("Estudiante con ID " + id + " actualizado.");
    }

    private void eliminar() {
        int id = vista.leerEntero("ID del estudiante a eliminar: ");
        if (modelo.eliminar(id)) vista.mostrarMensaje("Estudiante con ID " + id + " eliminado.");
        else vista.mostrarMensaje("No existe un estudiante con ID " + id + ".");
    }
}
