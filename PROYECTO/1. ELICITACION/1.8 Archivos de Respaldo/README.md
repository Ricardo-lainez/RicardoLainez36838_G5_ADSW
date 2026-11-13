# 1.8 Archivos de Respaldo

Fichas de trazabilidad y modelado de los 4 requisitos terminados en los sprints 1 a 4. Para cada requisito se usó un modelo de lenguaje con un prompt de ingeniería que genera el código PlantUML de su diagrama de clases. El prompt original va dentro del código como comentario de metadatos (`/' ... '/`), para que el modelo quede ligado a su requisito.

## Especificación de los requisitos usados

| Requisito funcional | Objetivo principal | Clases actoras |
|---|---|---|
| REQ002 (RF-02) | Registrar el pedido de una mesa numerada con productos, cantidades y observaciones, y enviarlo a cocina en estado pendiente. | Mesero (actor), PantallaPedido, ControlPedido, Pedido, Mesa, Producto, RepositorioPedido |
| REQ004 (RF-04) | Permitir que el cliente haga su pedido desde el celular escaneando el QR de su mesa y eligiendo productos del menú digital. | Cliente (actor), PantallaMenuDigital, ControlPedidoQR, Pedido, Mesa, Producto, Promocion, RepositorioPedido |
| REQ005 (RF-05) | Mostrar al cocinero los pedidos pendientes y permitirle cambiar su estado siguiendo la secuencia definida. | Cocinero (actor), PantallaCocina, ControlPreparacion, Pedido, RepositorioPedido |
| REQ006 (RF-06) | Buscar el pedido de una mesa, mostrar el total y registrar el método de pago para dejarlo pagado. | Cajero (actor), PantallaCaja, ControlCobro, Pedido, Mesa, RepositorioPedido |

## Ficha de trazabilidad y modelado: REQ002 Tomar pedido en mesa

**Requisito atómico:** "El sistema de gestión de pedidos Bocatto deberá permitir al Mesero registrar el pedido de una mesa numerada con sus productos, cantidades y observaciones, validar que tenga al menos un producto, generar su identificador y dejarlo en estado pendiente para que lo vea cocina."

**Artefacto generado:** diagrama de clases de análisis con frontera, controlador, entidades y repositorio (PantallaPedido, ControlPedido, Pedido, Mesa, Producto, RepositorioPedido).

**Metadatos embebidos:** prompt original integrado en la cabecera del archivo fuente mediante etiquetas `/' ... '/` para asegurar la trazabilidad modelo-requisito.

**Trazabilidad:** RF-02 (ERS) → CU 0-1 (Guía 1 y Guía 2) → historia REQ002 (MTZHU_G5_V1.xlsx) → SCRUM-14 en Jira (Sprint 1) → página en Confluence.

**Archivo de respaldo:** [REQ002_TomarPedidoMesa.txt](https://github.com/Ricardo-lainez/RicardoLainez36838_G5_ADSW/blob/main/PROYECTO/1.%20ELICITACION/1.8%20Archivos%20de%20Respaldo/REQ002_TomarPedidoMesa.txt)

**Diagrama:** [REQ002_TomarPedidoMesa.png](https://github.com/Ricardo-lainez/RicardoLainez36838_G5_ADSW/blob/main/PROYECTO/2.%20DISE%C3%91OS/2.5%20Diagramas%20de%20clases%20por%20requisito/REQ002_TomarPedidoMesa.png)

## Ficha de trazabilidad y modelado: REQ004 Realizar pedido por QR

**Requisito atómico:** "El sistema de gestión de pedidos Bocatto deberá permitir al Cliente escanear el código QR de su mesa, ver el menú digital con precios y promociones vigentes, elegir productos y cantidades y confirmar el pedido, que queda en estado pendiente y se envía a cocina."

**Artefacto generado:** diagrama de clases de análisis con frontera, controlador, entidades y repositorio (PantallaMenuDigital, ControlPedidoQR, Pedido, Mesa, Producto, Promocion, RepositorioPedido).

**Metadatos embebidos:** prompt original integrado en la cabecera del archivo fuente mediante etiquetas `/' ... '/` para asegurar la trazabilidad modelo-requisito.

**Trazabilidad:** RF-04 (ERS) → CU 0-3 (Guía 1 y Guía 2) → historia REQ004 (MTZHU_G5_V1.xlsx) → SCRUM-19 en Jira (Sprint 2) → página en Confluence.

**Archivo de respaldo:** [REQ004_PedidoQR.txt](https://github.com/Ricardo-lainez/RicardoLainez36838_G5_ADSW/blob/main/PROYECTO/1.%20ELICITACION/1.8%20Archivos%20de%20Respaldo/REQ004_PedidoQR.txt)

**Diagrama:** [REQ004_PedidoQR.png](https://github.com/Ricardo-lainez/RicardoLainez36838_G5_ADSW/blob/main/PROYECTO/2.%20DISE%C3%91OS/2.5%20Diagramas%20de%20clases%20por%20requisito/REQ004_PedidoQR.png)

## Ficha de trazabilidad y modelado: REQ005 Gestionar preparación del pedido

**Requisito atómico:** "El sistema de gestión de pedidos Bocatto deberá permitir al Cocinero ver los pedidos pendientes y cambiar su estado solo en la secuencia pendiente, en preparación, listo y listo para entregar, reflejando el nuevo estado para el personal y el cliente."

**Artefacto generado:** diagrama de clases de análisis con frontera, controlador, entidades y repositorio (PantallaCocina, ControlPreparacion, Pedido, RepositorioPedido).

**Metadatos embebidos:** prompt original integrado en la cabecera del archivo fuente mediante etiquetas `/' ... '/` para asegurar la trazabilidad modelo-requisito.

**Trazabilidad:** RF-05 (ERS) → CU 0-4 (Guía 1 y Guía 2) → historia REQ005 (MTZHU_G5_V1.xlsx) → SCRUM-27 en Jira (Sprint 3) → página en Confluence.

**Archivo de respaldo:** [REQ005_PreparacionPedido.txt](https://github.com/Ricardo-lainez/RicardoLainez36838_G5_ADSW/blob/main/PROYECTO/1.%20ELICITACION/1.8%20Archivos%20de%20Respaldo/REQ005_PreparacionPedido.txt)

**Diagrama:** [REQ005_PreparacionPedido.png](https://github.com/Ricardo-lainez/RicardoLainez36838_G5_ADSW/blob/main/PROYECTO/2.%20DISE%C3%91OS/2.5%20Diagramas%20de%20clases%20por%20requisito/REQ005_PreparacionPedido.png)

## Ficha de trazabilidad y modelado: REQ006 Cobrar pedido en la caja

**Requisito atómico:** "El sistema de gestión de pedidos Bocatto deberá permitir al Cajero buscar el pedido de una mesa por su número, verificar el detalle y el total, y registrar el método de pago (efectivo o digital/transferencia) solo si el pedido está listo, dejándolo en estado pagado."

**Artefacto generado:** diagrama de clases de análisis con frontera, controlador, entidades y repositorio (PantallaCaja, ControlCobro, Pedido, Mesa, RepositorioPedido).

**Metadatos embebidos:** prompt original integrado en la cabecera del archivo fuente mediante etiquetas `/' ... '/` para asegurar la trazabilidad modelo-requisito.

**Trazabilidad:** RF-06 (ERS) → CU 0-5 (Guía 1 y Guía 2) → historia REQ006 (MTZHU_G5_V1.xlsx) → SCRUM-31 en Jira (Sprint 4) → página en Confluence.

**Archivo de respaldo:** [REQ006_CobroCaja.txt](https://github.com/Ricardo-lainez/RicardoLainez36838_G5_ADSW/blob/main/PROYECTO/1.%20ELICITACION/1.8%20Archivos%20de%20Respaldo/REQ006_CobroCaja.txt)

**Diagrama:** [REQ006_CobroCaja.png](https://github.com/Ricardo-lainez/RicardoLainez36838_G5_ADSW/blob/main/PROYECTO/2.%20DISE%C3%91OS/2.5%20Diagramas%20de%20clases%20por%20requisito/REQ006_CobroCaja.png)
