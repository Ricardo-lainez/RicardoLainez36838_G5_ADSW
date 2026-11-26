package bocatto.control;

import bocatto.modelo.Pedido;
import bocatto.modelo.Resultado;
import bocatto.observer.CentralNotificaciones;
import bocatto.observer.EventoPedido;
import bocatto.observer.TipoEventoPedido;
import java.util.Arrays;
import java.util.List;

/** Control de CU 0-4: cambia el estado en orden y publica el cambio (Observer). */
public class ControlPreparacion {
    private static final List<String> SECUENCIA =
            Arrays.asList("pendiente", "en preparacion", "listo", "listo para entregar");
    private final CentralNotificaciones central;

    public ControlPreparacion(CentralNotificaciones central) {
        this.central = central;
    }

    public Resultado cambiarEstado(Pedido pedido, String nuevoEstado) {
        if (!validarSecuencia(pedido.getEstado(), nuevoEstado)) {
            return new Resultado(false, "No se puede pasar de '" + pedido.getEstado() + "' a '" + nuevoEstado + "'.");
        }
        pedido.setEstado(nuevoEstado);
        // un solo aviso llega al cliente, a la caja y a quien se suscriba despues
        central.publicar(new EventoPedido(TipoEventoPedido.CAMBIO_ESTADO, pedido));
        return new Resultado(true, "Pedido #" + pedido.getId() + " ahora esta " + nuevoEstado);
    }

    public boolean validarSecuencia(String actual, String nuevo) {
        // solo se avanza un paso a la vez, nunca se salta ni se regresa
        return SECUENCIA.indexOf(nuevo) == SECUENCIA.indexOf(actual) + 1;
    }
}
