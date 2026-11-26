package bocatto.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Observer: sujeto. Reemplaza las flechas "notifica" de la Guia 2.
 * Los controles ya no conocen a las pantallas, solo publican el evento aqui.
 */
public class CentralNotificaciones {
    private final List<ObservadorPedido> observadores = new ArrayList<>();

    public void suscribir(ObservadorPedido o) { observadores.add(o); }

    public void desuscribir(ObservadorPedido o) { observadores.remove(o); }

    public void publicar(EventoPedido evento) {
        // avisa a todos los suscritos, cada uno filtra lo que le interesa
        for (ObservadorPedido o : observadores) {
            o.actualizar(evento);
        }
    }
}
