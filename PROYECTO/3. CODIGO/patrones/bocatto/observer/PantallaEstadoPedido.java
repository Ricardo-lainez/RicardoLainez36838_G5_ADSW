package bocatto.observer;

/** Observador del CU 0-6: el cliente ve el estado de su pedido sin preguntar al mesero. */
public class PantallaEstadoPedido implements ObservadorPedido {
    @Override
    public void actualizar(EventoPedido evento) {
        if (evento.getTipo() == TipoEventoPedido.CAMBIO_ESTADO) {
            System.out.println("   [Cliente, mesa " + evento.getPedido().getNumeroMesa() + "] tu pedido esta: "
                    + evento.getPedido().getEstado());
        }
    }
}
