package bocatto.strategy;

import bocatto.modelo.Pedido;
import bocatto.modelo.Resultado;

/** Strategy concreta 2: digital o transferencia. No hay cambio, pide el numero de comprobante. */
public class PagoDigital implements EstrategiaPago {
    @Override
    public Resultado pagar(Pedido pedido, String dato) {
        if (dato == null || dato.trim().isEmpty()) {
            return new Resultado(false, "Ingrese el numero de comprobante de la transferencia.");
        }
        return new Resultado(true, "Pagado por transferencia. Comprobante: " + dato.trim());
    }

    @Override
    public String getNombre() { return "digital/transferencia"; }
}
