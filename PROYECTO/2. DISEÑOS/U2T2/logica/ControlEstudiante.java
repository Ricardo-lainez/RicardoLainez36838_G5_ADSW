package U2T2.logica;

import U2T2.logica.observer.EventoEstudiante;
import U2T2.logica.observer.ObservadorEstudiante;
import U2T2.logica.observer.SujetoEventosEstudiante;
import U2T2.logica.observer.TipoEventoEstudiante;
import U2T2.logica.strategy.BusquedaPorIdStrategy;
import U2T2.logica.strategy.EstrategiaBusquedaEstudiante;
import U2T2.datos.RepositorioEstudiante;
import U2T2.modelo.Estudiante;
import java.util.ArrayList;
import java.util.List;

/**
 * CAPA: Logica de negocio
 * Responsabilidad: validar datos, aplicar las validaciones y coordinar el CRUD.
 * Observer: es el sujeto concreto que notifica los eventos del CRUD.
 * Strategy: es el contexto que usa la estrategia de busqueda actual.
 * Command: es el receptor que ejecuta las operaciones pedidas por los comandos.
 */
public class ControlEstudiante implements SujetoEventosEstudiante {
    private final RepositorioEstudiante repo = new RepositorioEstudiante();
    // lista de los que estan suscritos a los cambios
    private final List<ObservadorEstudiante> observadores = new ArrayList<>();
    // Strategy: el control es el contexto, guarda la estrategia actual (por defecto busca por ID)
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
        // recorre a todos los suscritos y les pasa el evento
        for (ObservadorEstudiante observador : observadores) {
            observador.actualizar(evento);
        }
    }

    // ---------- CRUD ----------
    // RF-01: Registrar estudiante
    public String agregarEstudiante(String idIngresado, String nombre, int edad) {
        // primero limpiamos el ID (espacios y mayusculas)
        String id = normalizarId(idIngresado);
        if (!validarId(id)) {
            return "Error: ID invalido. Use el formato ESPE: L seguido de 8 digitos (ej. L00434011).";
        }
        if (!validarDatos(id, nombre, edad)) {
            return "Error: Datos invalidos. Nombre no puede estar vacio y Edad debe estar entre 15 y 99.";
        }
        if (repo.existeId(id)) {
            return "Error: Ya existe un estudiante con el ID " + id + ".";
        }

        Estudiante estudiante = new Estudiante(id, nombre.trim(), edad);
        repo.guardar(estudiante);  // se guarda y despues se avisa a los observadores
        notificarObservadores(new EventoEstudiante(TipoEventoEstudiante.REGISTRADO, estudiante.copiar(),
                "Se registro " + estudiante.getNombre() + " (ID " + id + ")"));
        return "Agregado exitosamente: " + estudiante.getNombre();
    }

    // RF-04: Consultar/Listar estudiantes
    public List<Estudiante> listarTodos() {
        return repo.listarTodos();
    }

    // RF-02: Actualizar estudiante
    public String actualizarEstudiante(String idIngresado, String nuevoNombre, int nuevaEdad) {
        String id = normalizarId(idIngresado);
        if (repo.buscarPorId(id) == null) {
            return "Error: No existe un estudiante con el ID " + id + ".";
        }
        if (!validarDatos(id, nuevoNombre, nuevaEdad)) {
            return "Error: Datos invalidos. Nombre no puede estar vacio y Edad debe estar entre 15 y 99.";
        }

        Estudiante actualizado = new Estudiante(id, nuevoNombre.trim(), nuevaEdad);
        repo.actualizar(actualizado);
        notificarObservadores(new EventoEstudiante(TipoEventoEstudiante.ACTUALIZADO, actualizado.copiar(),
                "Se actualizo el ID " + id + " a " + actualizado.getNombre() + ", " + nuevaEdad + " anios"));
        return "Actualizado exitosamente: ID " + id;
    }

    // RF-03: Eliminar estudiante
    public String eliminarEstudiante(String idIngresado) {
        String id = normalizarId(idIngresado);
        Estudiante estudiante = repo.buscarPorId(id);
        if (estudiante == null) {
            return "Error: No existe un estudiante con el ID " + id + ".";
        }

        // copia antes de borrar, para el mensaje y el evento
        Estudiante copia = estudiante.copiar();
        repo.eliminar(id);
        notificarObservadores(new EventoEstudiante(TipoEventoEstudiante.ELIMINADO, copia,
                "Se elimino " + copia.getNombre() + " (ID " + id + ")"));
        return "Eliminado exitosamente: " + copia.getNombre() + " (ID " + id + ")";
    }

    // RF-05: Validar datos obligatorios
    public boolean validarDatos(String id, String nombre, int edad) {
        return validarId(id) && nombre != null && !nombre.trim().isEmpty() && edad >= 15 && edad <= 99;
    }

    /** El ID sigue el formato institucional de la ESPE: L mayuscula y 8 digitos. */
    public boolean validarId(String id) {
        return id != null && id.matches("L\\d{8}");  // L y 8 numeros, ej. L00434011
    }

    /** Quita espacios y pasa a mayuscula, asi "l00434011" se guarda como "L00434011". */
    public String normalizarId(String id) {
        return id == null ? "" : id.trim().toUpperCase();
    }


    // ---------- Strategy ----------
    // RF-06: Buscar estudiantes con criterio intercambiable
    // recibe cualquier clase que cumpla la interfaz, no importa cual
    public void cambiarEstrategiaBusqueda(EstrategiaBusquedaEstudiante estrategia) {
        this.estrategiaBusqueda = estrategia;  // aqui se cambia el algoritmo en tiempo de ejecucion
    }

    // solo para mostrar en pantalla cual se esta usando
    public String getNombreEstrategia() {
        return estrategiaBusqueda.getNombre();
    }

    public List<Estudiante> buscarEstudiantes(String criterio) {
        if (criterio == null || criterio.trim().isEmpty()) {
            return new ArrayList<>();
        }
        // el control no sabe cual estrategia es, solo llama a buscar()
        return estrategiaBusqueda.buscar(repo.listarTodos(), criterio);
    }

    // ---------- Command (operaciones del receptor para deshacer) ----------
    // RF-07: Deshacer la ultima operacion del CRUD
    // los comandos piden una copia antes de cambiar algo, para poder deshacer
    public Estudiante obtenerCopia(String id) {
        Estudiante e = repo.buscarPorId(normalizarId(id));
        return e == null ? null : e.copiar();
    }

    // deshacer un registro = borrar al que se acaba de registrar
    public String revertirRegistro(String idIngresado) {
        String id = normalizarId(idIngresado);
        Estudiante e = repo.buscarPorId(id);
        if (e == null) {
            return "No se pudo deshacer: el estudiante ya no existe.";
        }
        repo.eliminar(id);
        notificarObservadores(new EventoEstudiante(TipoEventoEstudiante.DESHECHO, e,
                "Se deshizo el registro de " + e.getNombre() + " (ID " + id + ")"));
        return "Registro deshecho: " + e.getNombre();
    }

    // deshacer una actualizacion = volver a los datos anteriores
    public String revertirActualizacion(Estudiante anterior) {
        if (anterior == null || repo.buscarPorId(anterior.getId()) == null) {
            return "No se pudo deshacer: el estudiante ya no existe.";
        }
        repo.actualizar(anterior.copiar());
        notificarObservadores(new EventoEstudiante(TipoEventoEstudiante.DESHECHO, anterior.copiar(),
                "Se restauraron los datos del ID " + anterior.getId()));
        return "Actualizacion deshecha: " + anterior;
    }

    public String revertirEliminacion(Estudiante eliminado) {
        if (eliminado == null || repo.existeId(eliminado.getId())) {
            return "No se pudo deshacer: el ID ya esta ocupado.";
        }
        repo.guardar(eliminado.copiar());  // deshacer una eliminacion = volver a guardarlo
        notificarObservadores(new EventoEstudiante(TipoEventoEstudiante.DESHECHO, eliminado.copiar(),
                "Se restauro a " + eliminado.getNombre() + " (ID " + eliminado.getId() + ")"));
        return "Eliminacion deshecha: " + eliminado.getNombre() + " vuelve al registro";
    }
}
