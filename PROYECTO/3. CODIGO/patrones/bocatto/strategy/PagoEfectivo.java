package bocatto.strategy;

import bocatto.modelo.Pedido;
import bocatto.modelo.Resultado;

/** Strategy concreta 1: efectivo. Revisa que alcance y calcula el cambio. */
public class PagoEfectivo implements EstrategiaPago {
    @Override
    public Resultado pagar(Pedido pedido, String dato) {
        double recibido;
        try {
            recibido = Double.parseDouble(dato.trim());
        } catch (RuntimeException e) {
            return new Resultado(false, "Ingrese el valor recibido en numeros.");
        }
        if (recibido < pedido.getTotal()) {
            return new Resultado(false, "El valor recibido no alcanza para el total.");
        }
        double cambio = recibido - pedido.getTotal();
        return new Resultado(true, "Pagado en efectivo. Cambio: $" + String.format("%.2f", cambio));
    }

    @Override
    public String getNombre() { return "efectivo"; }
}
