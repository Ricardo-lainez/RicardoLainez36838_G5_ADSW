package bocatto;

import bocatto.control.ControlCobro;
import bocatto.control.ControlPedido;
import bocatto.control.ControlPreparacion;
import bocatto.modelo.Pedido;
import bocatto.observer.CentralNotificaciones;
import bocatto.observer.PantallaCaja;
import bocatto.observer.PantallaCocina;
import bocatto.observer.PantallaEstadoPedido;
import bocatto.strategy.PagoDigital;
import bocatto.strategy.PagoEfectivo;

/** Recorre un pedido de principio a fin para ver los dos patrones en los casos de uso. */
public class DemoPatronesBocatto {
    public static void main(String[] args) {
        // Observer: las pantallas se suscriben una sola vez
        CentralNotificaciones central = new CentralNotificaciones();
        central.suscribir(new PantallaCocina());
        central.suscribir(new PantallaEstadoPedido());
        central.suscribir(new PantallaCaja());

        ControlPedido controlPedido = new ControlPedido(central);
        ControlPreparacion controlPreparacion = new ControlPreparacion(central);
        ControlCobro controlCobro = new ControlCobro();

        System.out.println("=== CU 0-1 Tomar pedido en mesa (publica NUEVO_PEDIDO) ===");
        Pedido p1 = controlPedido.registrarPedido("0045", 4, 19.00);
        Pedido p2 = controlPedido.registrarPedido("0046", 7, 12.50);

        System.out.println("\n=== CU 0-4 Gestionar preparacion (publica CAMBIO_ESTADO) ===");
        System.out.println(controlPreparacion.cambiarEstado(p1, "en preparacion"));
        System.out.println(controlPreparacion.cambiarEstado(p1, "listo"));
        System.out.println(controlPreparacion.cambiarEstado(p2, "listo"));  // salta un paso: no se permite

        System.out.println("\n=== CU 0-5 Cobrar pedido (Strategy) ===");
        controlCobro.seleccionarMetodoPago(new PagoEfectivo());
        System.out.println("Efectivo con $15: " + controlCobro.registrarPago(p1, "15"));
        System.out.println("Efectivo con $20: " + controlCobro.registrarPago(p1, "20"));
        System.out.println(p1);

        System.out.println(controlPreparacion.cambiarEstado(p2, "en preparacion"));
        System.out.println(controlPreparacion.cambiarEstado(p2, "listo"));
        controlCobro.seleccionarMetodoPago(new PagoDigital());
        System.out.println("Digital sin comprobante: " + controlCobro.registrarPago(p2, ""));
        System.out.println("Digital con comprobante: " + controlCobro.registrarPago(p2, "TRX-88123"));
        System.out.println(p2);
    }
}
