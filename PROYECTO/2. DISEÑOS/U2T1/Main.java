package U2T1;

import U2T1.controlador.EstudianteControlador;
import U2T1.modelo.EstudianteModelo;
import U2T1.vista.EstudianteVista;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/** Punto de entrada: crea el Modelo, el Controlador y la Vista, y los conecta. */
public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignorada) {
            // Se usa el aspecto por defecto
        }
        SwingUtilities.invokeLater(() -> {
            // el modelo va dentro del controlador y el controlador dentro de la vista
            EstudianteControlador controlador = new EstudianteControlador(new EstudianteModelo());
            new EstudianteVista(controlador).setVisible(true);
        });
    }
}
