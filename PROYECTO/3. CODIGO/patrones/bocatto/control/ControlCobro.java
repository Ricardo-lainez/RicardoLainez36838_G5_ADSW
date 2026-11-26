package bocatto.control;

import bocatto.modelo.Pedido;
import bocatto.modelo.Resultado;
import bocatto.strategy.EstrategiaPago;
import bocatto.strategy.PagoEfectivo;

/** Control de CU 0-5: es el contexto de Strategy, cobra con el metodo que elija el cajero. */
public class ControlCobro {
    private EstrategiaPago estrategia = new PagoEfectivo();  // por defecto efectivo

    public void seleccionarMetodoPago(EstrategiaPago estrategia) {
        this.estrategia = estrategia;  // se cambia segun el boton que presione el cajero
    }

    public Resultado registrarPago(Pedido pedido, String dato) {
        // la regla del CU 0-5 no cambia con el metodo: solo se cobra un pedido listo
        if (!"listo".equals(pedido.getEstado()) && !"listo para entregar".equals(pedido.getEstado())) {
            return new Resultado(false, "Solo se puede cobrar un pedido en estado listo.");
        }
        Resultado r = estrategia.pagar(pedido, dato);  // el control no sabe si es efectivo o digital
        if (r.isExito()) {
            pedido.setMetodoPago(estrategia.getNombre());
            pedido.setEstado("pagado");
        }
        return r;
    }
}
