package CRUD_MVC;

import CRUD_MVC.controlador.EstudianteControlador;
import CRUD_MVC.modelo.EstudianteModelo;
import CRUD_MVC.vista.EstudianteVista;

/** Punto de entrada: arma el MVC y arranca el menu. */
public class Main {
    public static void main(String[] args) {
        new EstudianteControlador(new EstudianteModelo(), new EstudianteVista()).iniciar();
    }
}
