package U2T2.modelo;

import java.util.Objects;

/**
 * CAPA: Modelo
 * Responsabilidad: representar la entidad estudiante con ID, Nombre y Edad.
 * El ID tiene el formato institucional de la ESPE (por ejemplo L00434011),
 * lo ingresa el usuario y es final: no se puede modificar despues de crear.
 */
public class Estudiante {
    private final String id;  // final: una vez creado el estudiante su ID ya no cambia
    private String nombre;
    private int edad;

    public Estudiante(String id, String nombre, int edad) {
        this.id = id;
        this.nombre = nombre;
        this.edad = edad;
    }

    // solo hay get para el ID, no tiene set a proposito
    public String getId() { return id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }

    /** Copia usada por Command para guardar el estado anterior antes de un cambio. */
    public Estudiante copiar() {
        return new Estudiante(id, nombre, edad);
    }

    @Override
    public String toString() {
        return "Estudiante [ID=" + id + ", Nombre=" + nombre + ", Edad=" + edad + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Estudiante that = (Estudiante) o;
        return Objects.equals(id, that.id) && edad == that.edad && Objects.equals(nombre, that.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, edad);
    }
}
