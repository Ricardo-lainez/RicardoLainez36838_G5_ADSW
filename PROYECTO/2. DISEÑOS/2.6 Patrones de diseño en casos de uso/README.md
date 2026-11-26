# 2.6 Patrones de diseño en los casos de uso de nivel 0

Se aplicaron dos patrones de comportamiento (Observer y Strategy) sobre las clases de análisis de la Guía 2. Se eligieron estos dos porque resuelven problemas que ya aparecen en los casos de uso. Command no se aplicó, y abajo se explica por qué.

| Archivo | Patrón | Caso de uso |
|---|---|---|
| CU04_Observer_EstadoPedido.png / .puml | Observer | CU 0-4 Gestionar preparación del pedido, conectado con CU 0-1, CU 0-2, CU 0-3, CU 0-5 y CU 0-6 |
| CU05_Strategy_MetodoPago.png / .puml | Strategy | CU 0-5 Cobrar pedido en la caja |

La implementación en Java está en `PROYECTO/3. CODIGO/patrones`.

## Observer en CU 0-4 Gestionar preparación del pedido

**Problema en la Guía 2.** Los controles avisan directamente a las pantallas. `ControlPedido`, `ControlPedidoQR` y `ControlPedidoTelefonico` tienen una dependencia "notifica" hacia `PantallaCocina`, y cuando el cocinero cambia el estado de un pedido, el cliente (CU 0-6) y el cajero (CU 0-5) también necesitan enterarse. Cada pantalla nueva obligaría a cambiar los controles.

**Solución.** Se agrega `CentralNotificaciones` como sujeto. Los controles solo publican un `EventoPedido` (`NUEVO_PEDIDO` o `CAMBIO_ESTADO`) y las pantallas que implementan `ObservadorPedido` reciben el aviso.

| Rol | Clase |
|---|---|
| Sujeto | `CentralNotificaciones` |
| Observador (contrato) | `ObservadorPedido` |
| Observadores concretos | `PantallaCocina` (CU 0-4), `PantallaEstadoPedido` (CU 0-6), `PantallaCaja` (CU 0-5) |
| Publican eventos | `ControlPedido` (CU 0-1, igual que CU 0-2 y CU 0-3) y `ControlPreparacion` (CU 0-4) |

**Ventaja.** Los controles ya no conocen a las pantallas. Si mañana se agrega, por ejemplo, un aviso para el mesero, se crea otro observador y se suscribe, sin tocar `ControlPreparacion`. Con un solo cambio de estado se actualizan el cliente y la caja.

**Desventaja.** Todos los observadores reciben todos los avisos y cada uno tiene que filtrar lo que le interesa (la caja solo reacciona cuando el pedido queda listo). Además, el orden en que se avisa depende del orden de suscripción y es más difícil seguir el flujo leyendo el código, porque el control no dice a quién avisa.

## Strategy en CU 0-5 Cobrar pedido en la caja

**Problema en la Guía 2.** El cajero puede cobrar en efectivo o por transferencia, y cada método se valida distinto: el efectivo revisa que el valor alcance y calcula el cambio, y la transferencia no tiene cambio pero exige el número de comprobante. Con un `if` por método dentro de `ControlCobro`, cada método nuevo (por ejemplo tarjeta) haría crecer ese método.

**Solución.** `ControlCobro` es el contexto y guarda una `EstrategiaPago`. El botón que presiona el cajero en `PantallaCaja` decide cuál se usa.

| Rol | Clase |
|---|---|
| Contexto | `ControlCobro` |
| Estrategia (contrato) | `EstrategiaPago` |
| Estrategias concretas | `PagoEfectivo`, `PagoDigital` |

**Ventaja.** La regla del caso de uso (solo se cobra un pedido listo) se escribe una sola vez en `ControlCobro`, y cada método de pago tiene su propia validación en su clase. Agregar un método nuevo es crear una clase.

**Desventaja.** Hay más clases para algo que hoy son solo dos métodos de pago, y la pantalla tiene que conocer las estrategias para elegir cuál crear. Si el restaurante nunca agrega otro método de pago, el patrón aporta poco.

## Por qué no se aplicó Command

Command sirve para deshacer o guardar un historial de operaciones. En los casos de uso de Bocatto el estado del pedido solo avanza (pendiente, en preparación, listo, listo para entregar) y un cobro registrado no debe revertirse desde la caja. Agregar deshacer iría contra esas reglas, así que el patrón no se justifica en este proyecto.

## Cómo ejecutar la prueba

Desde PowerShell, en la carpeta `PROYECTO/3. CODIGO/patrones`:

```powershell
javac -d out (Get-ChildItem "bocatto" -Recurse -Filter *.java | Select-Object -ExpandProperty FullName)
java -cp out bocatto.DemoPatronesBocatto
```
