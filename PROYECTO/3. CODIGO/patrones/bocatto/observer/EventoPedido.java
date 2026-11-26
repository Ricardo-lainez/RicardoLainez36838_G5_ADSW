package bocatto.observer;

import bocatto.modelo.Pedido;

/** Lo que recibe cada observador: que paso y con que pedido. */
public class EventoPedido {
    private final TipoEventoPedido tipo;
    private final Pedido pedido;

    public EventoPedido(TipoEventoPedido tipo, Pedido pedido) {
        this.tipo = tipo;
        this.pedido = pedido;
    }

    public TipoEventoPedido getTipo() { return tipo; }
    public Pedido getPedido() { return pedido; }
}
