package U2T1.modelo;

/**
 * MODELO: entidad estudiante.
 * El ID tiene el formato institucional de la ESPE (por ejemplo L00434011),
 * lo ingresa el usuario y es final: no se puede modificar despues de crear.
 */
public class Estudiante {
    private final String id;
    private String nombre;
    private int edad;

    public Estudiante(String id, String nombre, int edad) {
        this.id = id;
        this.nombre = nombre;
        this.edad = edad;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }

    @Override
    public String toString() {
        return "Estudiante [ID=" + id + ", Nombre=" + nombre + ", Edad=" + edad + "]";
    }
}
