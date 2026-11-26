package bocatto.observer;

/** Observador del CU 0-5: la caja se entera cuando un pedido ya se puede cobrar. */
public class PantallaCaja implements ObservadorPedido {
    @Override
    public void actualizar(EventoPedido evento) {
        // solo le interesa cuando el pedido queda listo
        if (evento.getTipo() == TipoEventoPedido.CAMBIO_ESTADO && "listo".equals(evento.getPedido().getEstado())) {
            System.out.println("   [Caja] el pedido #" + evento.getPedido().getId() + " ya se puede cobrar");
        }
    }
}
