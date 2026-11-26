package bocatto.observer;

/** Los dos avisos que circulan entre los casos de uso. */
public enum TipoEventoPedido {
    NUEVO_PEDIDO,    // lo publican CU 0-1, CU 0-2 y CU 0-3
    CAMBIO_ESTADO    // lo publica CU 0-4
}
