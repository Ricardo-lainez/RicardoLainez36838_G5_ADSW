package bocatto.control;

import bocatto.modelo.Pedido;
import bocatto.observer.CentralNotificaciones;
import bocatto.observer.EventoPedido;
import bocatto.observer.TipoEventoPedido;

/** Control de CU 0-1 (en la Guia 2 tambien sirve de modelo para CU 0-2 y CU 0-3). */
public class ControlPedido {
    private final CentralNotificaciones central;

    public ControlPedido(CentralNotificaciones central) {
        this.central = central;
    }

    public Pedido registrarPedido(String id, int numeroMesa, double total) {
        Pedido pedido = new Pedido(id, numeroMesa, total);
        // antes: ControlPedido ..> PantallaCocina. Ahora solo publica y no sabe quien escucha
        central.publicar(new EventoPedido(TipoEventoPedido.NUEVO_PEDIDO, pedido));
        return pedido;
    }
}
