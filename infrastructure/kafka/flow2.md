Ahora vamos con la **segunda parte del viaje**: cuando el **servicio de pagos responde** de vuelta al **servicio de pedidos** para decir si el cobro fue **exitoso o fallido**.

---

## 🎓 ¿Qué pasa cuando el pago ha sido procesado?

Imaginemos que Kafka es como **una estación de tren de mensajes**. Ya vimos cómo el **Order Service** sube un mensaje al tren diciendo "He creado un pedido, por favor cóbralo".  
Ese tren llegó a la **estación de pagos**, donde el **Payment Service** se subió, procesó el pago, y ahora tiene que **responder**.

---

### 🎬 Escena 1: El servicio de pagos responde

- El `Payment Service` procesa el pago.
- Al terminar, crea un **evento de respuesta**:
  - Si el pago fue **exitoso**, crea un evento `PaymentResponse` con estado `COMPLETED`.
  - Si el pago **falló**, el estado será `CANCELLED` o `FAILED`.

- Este evento también se convierte en un **mensaje Kafka** (en formato Avro).

---

### 🎬 Escena 2: Se publica en un nuevo tópico

- El `Payment Service` publica el evento en otro tópico de Kafka, como:
  ```plaintext
  order-service.payment-response-topic-name
  ```

- Este tópico está **escuchado por el Order Service**.  
  Piensa que es como si el `Order Service` tuviera un **radar** encendido escuchando constantemente por si llega una respuesta.

---

### 🎬 Escena 3: El Order Service lo escucha

- Dentro de `order-messaging`, en el paquete `listener.kafka`, hay una clase llamada:

  ```java
  PaymentResponseKafkaListener
  ```

- Esta clase está marcada con `@KafkaListener`, lo que le permite **escuchar los mensajes** del tópico de respuesta de pagos.

---

### 🎬 Escena 4: Se procesa el mensaje

- Cuando llega un `PaymentResponseAvroModel`, el `PaymentResponseKafkaListener`:
  - Lo convierte a un `PaymentResponse` de dominio con ayuda de un **mapper**.
  - Y llama a uno de estos métodos:
    - `paymentCompleted()` si el estado es `COMPLETED`.
    - `paymentCancelled()` si es `CANCELLED` o `FAILED`.

---

### 🎬 Escena 5: El Order Service actualiza el pedido

- Internamente, ese método (`paymentCompleted` o `paymentCancelled`) va al **servicio de aplicación**, que a su vez:
  - Consulta el pedido por su `trackingId`.
  - Cambia su estado a `PAID` o `CANCELLED`.
  - Puede publicar nuevos eventos si hace falta (por ejemplo, si fue pagado, se avisa al restaurante).

---

### 🎯 Resultado final

- Si todo va bien: ✅ el pedido queda pagado y listo para ser aprobado por el restaurante.
- Si el pago falla: ❌ el pedido se cancela y se guarda el motivo.

---

### 🧠 En resumen

- El **Payment Service** **responde** publicando un mensaje Kafka.
- El **Order Service** **escucha** ese mensaje con un listener.
- Procesa el resultado del pago y **actualiza el estado del pedido**.

---

### 🎨 Analogía visual simple

> Imagina dos oficinas:  
> 🧑‍💼 Oficina de pedidos y 🧑‍💼 oficina de pagos.  
> En medio hay un sistema de **mensajes en papel (Kafka)**.  
> Cuando se crea un pedido, se manda un papel a pagos.  
> Pagos responde con otro papel diciendo si se ha cobrado o no.  
> Pedidos lo recoge y actúa según esa respuesta.

---

¿Te gustaría que ahora lo reflejemos en un esquema visual como el anterior, pero para este flujo de **respuesta del servicio de pagos**?