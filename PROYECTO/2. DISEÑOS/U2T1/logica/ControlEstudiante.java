package U2T1.logica;

import U2T1.datos.RepositorioEstudiante;
import U2T1.modelo.Estudiante;
import java.util.ArrayList;
import java.util.List;

/**
 * CAPA: Logica de negocio
 * Responsabilidad: validar datos, aplicar reglas academicas y coordinar el CRUD.
 * Observer: es el sujeto concreto que notifica los eventos del CRUD.
 * Strategy: es el contexto que usa la estrategia de busqueda actual.
 * Command: es el receptor que ejecuta las operaciones pedidas por los comandos.
 */
public class ControlEstudiante implements SujetoEventosEstudiante {
    private final RepositorioEstudiante repo = new RepositorioEstudiante();
    private final List<ObservadorEstudiante> observadores = new ArrayList<>();
    private EstrategiaBusquedaEstudiante estrategiaBusqueda = new BusquedaPorIdStrategy();

    // ---------- Observer ----------
    @Override
    public void agregarObservador(ObservadorEstudiante observador) {
        observadores.add(observador);
    }

    @Override
    public void quitarObservador(ObservadorEstudiante observador) {
        observadores.remove(observador);
    }

    @Override
    public void notificarObservadores(EventoEstudiante evento) {
        for (ObservadorEstudiante observador : observadores) {
            observador.actualizar(evento);
        }
    }

    // ---------- CRUD ----------
    // RF-01: Registrar estudiante
    public String agregarEstudiante(int id, String nombre, int edad) {
        if (!validarDatos(id, nombre, edad)) {
            return "Error: Datos invalidos. ID y Edad deben ser mayores a 0, Nombre no puede estar vacio.";
        }
        if (repo.existeId(id)) {
            return "Error: Ya existe un estudiante con el ID " + id + ".";
        }

        Estudiante estudiante = new Estudiante(id, nombre.trim(), edad);
        repo.guardar(estudiante);
        notificarObservadores(new EventoEstudiante(TipoEventoEstudiante.REGISTRADO, estudiante.copiar(),
                "Se registro " + estudiante.getNombre() + " (ID " + id + ")"));
        return "Agregado exitosamente: " + estudiante.getNombre();
    }

    // RF-04: Consultar/Listar estudiantes
    public List<Estudiante> listarTodos() {
        return repo.listarTodos();
    }

    // RF-02: Actualizar estudiante
    public String actualizarEstudiante(int id, String nuevoNombre, int nuevaEdad) {
        if (repo.buscarPorId(id) == null) {
            return "Error: No existe un estudiante con el ID " + id + ".";
        }
        if (!validarDatos(id, nuevoNombre, nuevaEdad)) {
            return "Error: Datos invalidos para la actualizacion.";
        }

        Estudiante actualizado = new Estudiante(id, nuevoNombre.trim(), nuevaEdad);
        repo.actualizar(actualizado);
        notificarObservadores(new EventoEstudiante(TipoEventoEstudiante.ACTUALIZADO, actualizado.copiar(),
                "Se actualizo el ID " + id + " a " + actualizado.getNombre() + ", " + nuevaEdad + " anios"));
        return "Actualizado exitosamente: ID " + id;
    }

    // RF-03: Eliminar estudiante
    public String eliminarEstudiante(int id) {
        Estudiante estudiante = repo.buscarPorId(id);
        if (estudiante == null) {
            return "Error: No existe un estudiante con el ID " + id + ".";
        }
        if (!cumpleReglaEliminacion(estudiante)) {
            return "Error: Regla academica - no se puede eliminar a '"
                    + estudiante.getNombre() + "' porque es menor de 18 anios (Edad: "
                    + estudiante.getEdad() + ").";
        }

        Estudiante copia = estudiante.copiar();
        repo.eliminar(id);
        notificarObservadores(new EventoEstudiante(TipoEventoEstudiante.ELIMINADO, copia,
                "Se elimino " + copia.getNombre() + " (ID " + id + ")"));
        return "Eliminado exitosamente: " + copia.getNombre() + " (ID " + id + ")";
    }

    // RF-05: Validar datos obligatorios
    public boolean validarDatos(int id, String nombre, int edad) {
        return id > 0 && nombre != null && !nombre.trim().isEmpty() && edad > 0;
    }

    private boolean cumpleReglaEliminacion(Estudiante estudiante) {
        return estudiante.getEdad() >= 18;
    }

    // ---------- Strategy ----------
    // RF-06: Buscar estudiantes con criterio intercambiable
    public void cambiarEstrategiaBusqueda(EstrategiaBusquedaEstudiante estrategia) {
        this.estrategiaBusqueda = estrategia;
    }

    public String getNombreEstrategia() {
        return estrategiaBusqueda.getNombre();
    }

    public List<Estudiante> buscarEstudiantes(String criterio) {
        if (criterio == null || criterio.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return estrategiaBusqueda.buscar(repo.listarTodos(), criterio);
    }

    // ---------- Command (operaciones del receptor para deshacer) ----------
    // RF-07: Deshacer la ultima operacion del CRUD
    public Estudiante obtenerCopia(int id) {
        Estudiante e = repo.buscarPorId(id);
        return e == null ? null : e.copiar();
    }

    String revertirRegistro(int id) {
        Estudiante e = repo.buscarPorId(id);
        if (e == null) {
            return "No se pudo deshacer: el estudiante ya no existe.";
        }
        repo.eliminar(id);
        notificarObservadores(new EventoEstudiante(TipoEventoEstudiante.DESHECHO, e,
                "Se deshizo el registro de " + e.getNombre() + " (ID " + id + ")"));
        return "Registro deshecho: " + e.getNombre();
    }

    String revertirActualizacion(Estudiante anterior) {
        if (anterior == null || repo.buscarPorId(anterior.getId()) == null) {
            return "No se pudo deshacer: el estudiante ya no existe.";
        }
        repo.actualizar(anterior.copiar());
        notificarObservadores(new EventoEstudiante(TipoEventoEstudiante.DESHECHO, anterior.copiar(),
                "Se restauraron los datos del ID " + anterior.getId()));
        return "Actualizacion deshecha: " + anterior;
    }

    String revertirEliminacion(Estudiante eliminado) {
        if (eliminado == null || repo.existeId(eliminado.getId())) {
            return "No se pudo deshacer: el ID ya esta ocupado.";
        }
        repo.guardar(eliminado.copiar());
        notificarObservadores(new EventoEstudiante(TipoEventoEstudiante.DESHECHO, eliminado.copiar(),
                "Se restauro a " + eliminado.getNombre() + " (ID " + eliminado.getId() + ")"));
        return "Eliminacion deshecha: " + eliminado.getNombre() + " vuelve al registro";
    }
}
