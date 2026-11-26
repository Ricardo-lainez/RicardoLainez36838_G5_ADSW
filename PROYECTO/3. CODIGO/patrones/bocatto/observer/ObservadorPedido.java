package bocatto.observer;

/** Observer: contrato de toda pantalla que quiere enterarse de los cambios de un pedido. */
public interface ObservadorPedido {
    void actualizar(EventoPedido evento);  // cada pantalla decide que hacer con el aviso
}
