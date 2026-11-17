package U2T1.controlador;

import U2T1.modelo.Estudiante;
import U2T1.modelo.EstudianteModelo;
import java.util.ArrayList;
import java.util.List;

/**
 * CONTROLADOR: recibe las acciones de la vista, valida los datos,
 * aplica las reglas y llama al modelo. Devuelve un mensaje con el resultado.
 */
public class EstudianteControlador {
    private final EstudianteModelo modelo;

    public EstudianteControlador(EstudianteModelo modelo) {
        this.modelo = modelo;
    }

    // RF-01: Registrar estudiante
    public String registrar(String idIngresado, String nombre, String edadTexto) {
        String id = normalizarId(idIngresado);
        if (!validarId(id)) {
            return "Error: ID invalido. Use el formato ESPE: L seguido de 8 digitos (ej. L00434011).";
        }
        if (modelo.existeId(id)) {
            return "Error: Ya existe un estudiante con el ID " + id + ".";
        }
        Integer edad = convertirEdad(edadTexto);
        if (!validarDatos(nombre, edad)) {
            return "Error: Nombre obligatorio y Edad entre 15 y 99.";
        }
        modelo.guardar(new Estudiante(id, nombre.trim(), edad));
        return "Registrado: " + nombre.trim() + " (" + id + ")";
    }

    // RF-04: Consultar estudiantes
    public List<Estudiante> listar() {
        return modelo.listarTodos();
    }

    // RF-04: Buscar por ID ESPE (completo o parcial)
    public List<Estudiante> buscarPorId(String texto) {
        String criterio = normalizarId(texto);
        List<Estudiante> resultado = new ArrayList<>();
        for (Estudiante e : modelo.listarTodos()) {
            if (e.getId().contains(criterio)) resultado.add(e);
        }
        return resultado;
    }

    // RF-02: Actualizar estudiante (el ID no cambia)
    public String actualizar(String idIngresado, String nombre, String edadTexto) {
        String id = normalizarId(idIngresado);
        if (!modelo.existeId(id)) {
            return "Error: No existe un estudiante con el ID " + id + ".";
        }
        Integer edad = convertirEdad(edadTexto);
        if (!validarDatos(nombre, edad)) {
            return "Error: Nombre obligatorio y Edad entre 15 y 99.";
        }
        modelo.actualizar(id, nombre.trim(), edad);
        return "Actualizado: " + id + " (el ID se mantiene)";
    }

    // RF-03: Eliminar estudiante
    public String eliminar(String idIngresado) {
        String id = normalizarId(idIngresado);
        Estudiante e = modelo.buscarPorId(id);
        if (e == null) {
            return "Error: No existe un estudiante con el ID " + id + ".";
        }
        modelo.eliminar(id);
        return "Eliminado: " + e.getNombre() + " (" + id + ")";
    }

    // RF-05: Validaciones
    public boolean validarId(String id) {
        return id != null && id.matches("L\\d{8}");
    }

    public String normalizarId(String id) {
        return id == null ? "" : id.trim().toUpperCase();
    }

    private boolean validarDatos(String nombre, Integer edad) {
        return nombre != null && !nombre.trim().isEmpty() && edad != null && edad >= 15 && edad <= 99;
    }

    private Integer convertirEdad(String texto) {
        try {
            return Integer.parseInt(texto.trim());
        } catch (RuntimeException e) {
            return null;
        }
    }
}
