package bocatto.observer;

/** Observador del CU 0-4: el cocinero ve cada pedido nuevo apenas se registra. */
public class PantallaCocina implements ObservadorPedido {
    @Override
    public void actualizar(EventoPedido evento) {
        if (evento.getTipo() == TipoEventoPedido.NUEVO_PEDIDO) {
            System.out.println("   [Cocina] llego un pedido nuevo: " + evento.getPedido());
        }
    }
}
