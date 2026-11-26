package bocatto.strategy;

import bocatto.modelo.Pedido;
import bocatto.modelo.Resultado;

/**
 * Strategy: contrato de los metodos de pago del CU 0-5.
 * dato es lo que ingresa el cajero: el valor recibido o el numero de comprobante.
 */
public interface EstrategiaPago {
    Resultado pagar(Pedido pedido, String dato);
    String getNombre();
}
