package CRUD_MVC.modelo;

/** Modelo: entidad estudiante. El ID es final, se asigna al crear y no se modifica. */
public class Estudiante {
    private final int id;
    private String nombre;
    private int edad;

    public Estudiante(int id, String nombre, int edad) {
        this.id = id;
        this.nombre = nombre;
        this.edad = edad;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }

    @Override
    public String toString() {
        return "ID=" + id + " | Nombre=" + nombre + " | Edad=" + edad;
    }
}
