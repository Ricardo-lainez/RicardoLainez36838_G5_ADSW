# Patrones de diseño en los casos de uso de Bocatto

Código de los dos patrones aplicados a los casos de uso de nivel 0. La explicación de cada patrón, su ventaja y su desventaja está en `PROYECTO/2. DISEÑOS/2.6 Patrones de diseño en casos de uso`.

| Paquete | Contenido |
|---|---|
| `bocatto.modelo` | `Pedido` y `Resultado` |
| `bocatto.observer` | `CentralNotificaciones` (sujeto), `ObservadorPedido`, `EventoPedido`, `TipoEventoPedido` y las pantallas `PantallaCocina`, `PantallaEstadoPedido` y `PantallaCaja` |
| `bocatto.strategy` | `EstrategiaPago`, `PagoEfectivo` y `PagoDigital` |
| `bocatto.control` | `ControlPedido` (CU 0-1), `ControlPreparacion` (CU 0-4) y `ControlCobro` (CU 0-5) |
| `bocatto` | `DemoPatronesBocatto`, que recorre un pedido desde que se registra hasta que se cobra |

## Ejecución

```powershell
javac -d out (Get-ChildItem "bocatto" -Recurse -Filter *.java | Select-Object -ExpandProperty FullName)
java -cp out bocatto.DemoPatronesBocatto
```

## Salida obtenida

```text
=== CU 0-1 Tomar pedido en mesa (publica NUEVO_PEDIDO) ===
   [Cocina] llego un pedido nuevo: Pedido #0045 (mesa 4, $19.00, pendiente)
   [Cocina] llego un pedido nuevo: Pedido #0046 (mesa 7, $12.50, pendiente)

=== CU 0-4 Gestionar preparacion (publica CAMBIO_ESTADO) ===
   [Cliente, mesa 4] tu pedido esta: en preparacion
OK: Pedido #0045 ahora esta en preparacion
   [Cliente, mesa 4] tu pedido esta: listo
   [Caja] el pedido #0045 ya se puede cobrar
OK: Pedido #0045 ahora esta listo
ERROR: No se puede pasar de 'pendiente' a 'listo'.

=== CU 0-5 Cobrar pedido (Strategy) ===
Efectivo con $15: ERROR: El valor recibido no alcanza para el total.
Efectivo con $20: OK: Pagado en efectivo. Cambio: $1.00
Pedido #0045 (mesa 4, $19.00, pagado)
   [Cliente, mesa 7] tu pedido esta: en preparacion
OK: Pedido #0046 ahora esta en preparacion
   [Cliente, mesa 7] tu pedido esta: listo
   [Caja] el pedido #0046 ya se puede cobrar
OK: Pedido #0046 ahora esta listo
Digital sin comprobante: ERROR: Ingrese el numero de comprobante de la transferencia.
Digital con comprobante: OK: Pagado por transferencia. Comprobante: TRX-88123
Pedido #0046 (mesa 7, $12.50, pagado)
```
