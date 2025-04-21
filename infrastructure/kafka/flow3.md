## 🎓 ¿Qué pasa cuando el pedido ha sido pagado y hay que avisar al restaurante?

Una vez que el cliente ha pagado y todo está correcto, ahora toca **avisar al restaurante** para que apruebe o rechace el pedido.  
Kafka sigue siendo esa **estación de trenes de mensajes** entre los servicios.

---

### 🎬 Escena 1: El Order Service crea el evento

- Tras recibir la respuesta del pago con estado `COMPLETED`, el `Order Service`:
    - Crea un evento `OrderPaidEvent`.

- Ese evento indica que el pedido está listo para enviarse al restaurante y necesita su aprobación.

---

### 🎬 Escena 2: Se publica en un tópico de Kafka

- El evento se convierte en un mensaje Kafka (`RestaurantApprovalRequestAvroModel`) gracias al `OrderMessagingDataMapper`.
- Se publica en el tópico:
  ```plaintext
  restaurant-approval-request-topic
  ```

- Este mensaje contiene información como el ID del pedido, los productos, y la hora.

---

### 🎬 Escena 3: El Restaurant Service lo escucha

- El `restaurant-service` tiene un `@KafkaListener` activo que escucha este tópico.
- Cuando llega el mensaje, lo convierte a un objeto de dominio (`RestaurantApprovalRequest`).

---

### 🎬 Escena 4: El restaurante decide

- El `Restaurant Service` valida si puede aceptar ese pedido:
    - ¿Está activo?
    - ¿Tiene todos los productos?
    - ¿Los precios son correctos?

- En base a eso:
    - ✅ Aprueba el pedido (`OrderApprovalStatus.APPROVED`)
    - ❌ Lo rechaza (`OrderApprovalStatus.REJECTED`), indicando por qué.

---

## 🎓 ¿Qué pasa cuando el restaurante responde?

Ahora el restaurante ha decidido y hay que **responder al `Order Service`**.

---

### 🎬 Escena 5: Se publica la respuesta en otro tópico

- El `restaurant-service` publica su respuesta en otro tópico de Kafka:
  ```plaintext
  order-service.restaurant-approval-response-topic-name
  ```

- El mensaje se envía como `RestaurantApprovalResponseAvroModel`.

---

### 🎬 Escena 6: El Order Service lo escucha

- En `order-messaging.listener.kafka`, hay un listener llamado:
  ```java
  RestaurantApprovalResponseKafkaListener
  ```

- Esta clase escucha el tópico de respuesta y recibe los mensajes de aprobación o rechazo del restaurante.

---

### 🎬 Escena 7: Se procesa el mensaje

- El listener:
    - Convierte el mensaje Avro a un `RestaurantApprovalResponse` de dominio.
    - Llama a `orderApproved()` o `orderRejected()` en el servicio de dominio.

---

### 🎬 Escena 8: Se actualiza el estado del pedido

- Si fue aprobado: el pedido pasa a estado `APPROVED`.
- Si fue rechazado: el pedido pasa a `CANCELLED`, y se anotan los motivos.

---

### 🎯 Resultado final

- ✅ Si el restaurante acepta: el pedido avanza hacia la preparación.
- ❌ Si lo rechaza: se cancela y se registra el motivo.

---

### 🧠 En resumen

- El `Order Service` envía el pedido pagado al `Restaurant Service` usando Kafka.
- El `Restaurant Service` responde con aprobación o rechazo.
- El `Order Service` actualiza el estado del pedido en consecuencia.

---

### 🎨 Analogía visual simple

> Imagina otra vez a 🧑‍💼 **Oficina de Pedidos** y 🧑‍🍳 **Cocina del Restaurante**.
>
> 🧾 El pedido pagado llega a la cocina (Kafka entrega la comanda).  
> 🍳 La cocina revisa: ¿tengo los ingredientes? ¿puedo preparar esto?  
> ✅ Si sí, responde: “¡Vamos allá!”. ❌ Si no, dice: “No se puede”.  
> 🧑‍💼 La oficina de pedidos recibe esa respuesta y actúa en consecuencia.