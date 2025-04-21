## 🧭 Flujo de evento: Pedido creado ➝ Kafka ➝ Servicio de Pagos

1. **Cliente (API REST)**  
   Envía una solicitud para crear un pedido.

2. **`order-application` (Controller)**  
   Recibe la petición HTTP y la traduce a un `CreateOrderCommand`.

3. **`order-application-service` (Application Service)**
    - Orquesta el flujo: verifica cliente y restaurante.
    - Usa el dominio (`order-domain-core`) para validar y crear el pedido.
    - Llama al `OrderCreatedPaymentRequestMessagePublisher` para publicar un evento.

4. **`order-domain-core`**
    - Valida las reglas de negocio y crea el objeto `OrderCreatedEvent`.

5. **`order-dataaccess`**  
   Guarda el pedido en la base de datos usando un `OrderRepository`.

6. **`order-messaging` (Kafka Publisher)**
    - Mapea el evento `OrderCreatedEvent` a un `PaymentRequestAvroModel`.
    - Lo publica en el **tópico Kafka** configurado (`payment-request-topic-name`).

7. **`Kafka Broker`**
    - Kafka almacena el evento en el tópico.
    - Lo pone disponible para los consumidores suscritos.

8. **`payment-service` (Kafka Listener)**
    - Escucha eventos en el tópico.
    - Procesa el evento `PaymentRequestAvroModel` recibido.
    - Responde con `PaymentResponseAvroModel` en otro tópico.

---

## 🎓 ¿Qué pasa cuando un cliente hace un pedido?

Imaginemos que estás usando una app para pedir comida a domicilio (tipo Glovo o Uber Eats) y pulsas el botón "Hacer pedido". ¿Qué pasa dentro del sistema?

---

### 🎬 Escena 1: El cliente hace un pedido

- El usuario pulsa "Hacer pedido".
- Eso envía una **petición HTTP** a nuestro sistema (al microservicio de pedidos).

---

### 🎬 Escena 2: El sistema recibe la solicitud

- Hay un microservicio llamado **Order Service**.
- Dentro de él hay un componente que actúa como **recepcionista**. Se llama `OrderController`, y es quien recibe todas las peticiones que vienen desde fuera.

---

### 🎬 Escena 3: La solicitud baja a la oficina central

- El `OrderController` le pasa la información a otro componente que **sabe cómo manejar pedidos**. Este es el **servicio de aplicación** (`OrderApplicationService`).
- Este servicio **orquesta** todo lo que hace falta: valida al cliente, valida el restaurante, crea el pedido, y decide si hay que guardar algo o lanzar un mensaje.

---

### 🎬 Escena 4: Se prepara el pedido

- El `OrderApplicationService` utiliza el **núcleo de negocio** del sistema (`order-domain-core`) para asegurarse de que todo esté bien: que los precios sean correctos, que el restaurante esté activo, etc.
- Si todo está bien, crea un objeto llamado `OrderCreatedEvent`. Es como decir: “¡Hey! Este pedido ya está listo para procesarse”.

---

### 🎬 Escena 5: Hay que avisar al servicio de pagos

- Ahora que el pedido se ha creado, necesitamos **avisar al servicio de pagos** para que cobre el dinero.
- Pero **no lo llamamos directamente**. Usamos algo llamado **Kafka**.

---

### 🧠 ¿Qué es Kafka?

Kafka es como un **sistema de mensajería**. Imagina un **buzón compartido** donde un microservicio puede dejar una carta (evento) y otro microservicio puede recogerla más tarde.

---

### 🎬 Escena 6: Se publica el mensaje

- El `order-messaging` tiene una clase especial (`CreateOrderKafkaMessagePublisher`) que **convierte el evento** en un mensaje en formato especial (`PaymentRequestAvroModel`) y lo publica en un **tópico Kafka** (que es como una bandeja de entrada con nombre).

Ejemplo de tópico: `"payment-request-topic"`.

---

### 🎬 Escena 7: Kafka guarda el mensaje

- Kafka **recibe el mensaje** y lo **guarda en el tópico**.
- Ese mensaje está esperando a que **alguien lo escuche**.

---

### 🎬 Escena 8: El servicio de pagos lo recoge

- El **Payment Service** está **suscrito a ese tópico**.
- Tiene una especie de **escucha activa** (un Listener).
- En cuanto detecta que hay un nuevo mensaje, lo **lee**.

---

### 🎬 Escena 9: Empieza el proceso de pago

- El `Payment Service` procesa el pago (cobra al cliente).
- Y si todo va bien o mal, **responderá** con otro mensaje (otro evento), que volverá a Kafka.

---

✅ ¡Y así es cómo un pedido pasa de nuestro sistema al servicio de pagos usando Kafka!  
📦 El pedido se crea → 📩 se publica un mensaje → 🎧 lo recoge el servicio de pagos → 💳 se intenta cobrar.

---

### 🎨 Analogía simple

> **Kafka es como una cinta transportadora de mensajes.**  
> Un microservicio pone un mensaje en la cinta, y otro lo recoge más adelante.