## 🧭 Estructura general del módulo `order-service`

Este módulo sigue la **Arquitectura Hexagonal** y se divide en:

### 1. **DTOs (`dto`)**
Son los objetos que viajan entre el mundo exterior (clientes, APIs, mensajería) y el interior del dominio.

- `create` → para crear un pedido:
    - `CreateOrderCommand` (input)
    - `CreateOrderResponse` (output)
    - `OrderItem` (input)
    - `OrderAddress` (input)

- `track` → para consultar el estado de un pedido:
    - `TrackOrderQuery`
    - `TrackOrderResponse`

- `message` → para mensajes entrantes desde otros servicios:
    - `PaymentResponse`
    - `RestaurantApprovalResponse`

---

### 2. **Value Objects compartidos** (en el módulo `common-domain`)
Se han creado dos nuevos enums reutilizables:

- `PaymentStatus { COMPLETED, CANCELLED, FAILED }`
- `OrderApprovalStatus { APPROVED, REJECTED }`

---

### 3. **Data Mapper**
Paquete: `mapper`

- Clase `OrderDataMapper`: convierte entre DTOs y entidades del dominio (`Order`, `OrderItem`, etc.)

---

### 4. **Hexagonal Architecture Ports**

#### 🡢 *Input Ports* (interfaces que define el dominio para ser usadas desde el exterior)

Paquete: `ports.input`

- `OrderApplicationService`
    - `createOrder(CreateOrderCommand)`
    - `trackOrder(TrackOrderQuery)`

- `PaymentResponseMessageListener`
    - `paymentCompleted(PaymentResponse)`
    - `paymentCancelled(PaymentResponse)`

- `RestaurantApprovalResponseMessageListener`
    - `orderApproved(RestaurantApprovalResponse)`
    - `orderRejected(RestaurantApprovalResponse)`

---

#### 🡠 *Output Ports* (interfaces que el dominio necesita implementar hacia el exterior)

Paquete: `ports.output`

- `repository`
    - `OrderRepository`
    - `RestaurantRepository`
    - `CustomerRepository`

- `message.publisher`
    - `OrderCreatedPaymentRequestMessagePublisher`
    - `OrderCancelledPaymentRequestMessagePublisher`
    - `OrderPaidRestaurantRequestMessagePublisher`

---

### 5. **Interfaces de publicación de eventos**

En `common-domain.events.publisher`:

```java
public interface DomainEventPublisher<T extends DomainEvent> {
    void publish(T domainEvent);
}
```

Luego se crean subinterfaces específicas, que simplemente extienden esta interfaz con tipos concretos como `OrderCreatedEvent`, `OrderCancelledEvent`...

---

Perfecto, vamos a ir desarrollando esas cuatro preguntas en notas bien estructuradas. Aquí tienes la primera versión del resumen para ayudarte a asentar los conceptos:

---

## 🧩 Comprendiendo la arquitectura del módulo `order-service`

### 1️⃣ ¿Qué es un **input port** y cómo se usa?

Un **input port** (puerto de entrada) es una **interfaz pública** expuesta por la aplicación para permitir que agentes externos (como controladores HTTP o listeners de eventos) **interactúen con la lógica de negocio**.

- En este módulo, los input ports están definidos en `order.service.domain.ports.input`.
- Ejemplos:
    - `OrderApplicationService`: expuesto a controladores REST. Tiene métodos como `createOrder()` y `trackOrder()`.
    - `PaymentResponseMessageListener`: invocado por eventos de respuesta del servicio de pagos.
    - `RestaurantApprovalResponseMessageListener`: invocado por eventos de aprobación del restaurante.

> 💡 Piensa en ellos como **interfaces de comunicación autorizadas hacia la lógica del sistema**.

---

### 2️⃣ ¿Qué rol juega cada **repositorio**?

Los **puertos de salida** (output ports) en el paquete `order.service.domain.ports.output.repository` representan las necesidades que tiene la aplicación para **acceder a datos persistentes**.

Son interfaces que luego implementarán los adaptadores de infraestructura (usando JPA, JDBC, etc).

- `OrderRepository`:
    - `save(Order order)`: guarda una orden.
    - `findByTrackingId(UUID trackingId)`: recupera una orden por ID de seguimiento.
- `RestaurantRepository`:
    - `findRestaurantInformation(Restaurant restaurant)`: busca los datos del restaurante y sus productos (por ID).
- `CustomerRepository`:
    - `findCustomer(UUID customerId)`: comprueba si el cliente existe.

> 💡 El dominio **solicita información** a través de estas interfaces, sin saber cómo están implementadas.

---

### 3️⃣ ¿Cómo fluyen los datos desde un cliente hasta que llegan al dominio?

1. 🧍 El **cliente externo** (Postman, Front-End, etc.) realiza una solicitud HTTP, por ejemplo, `POST /orders`.

2. 📦 Un **controlador REST** (aún por crear) convierte la solicitud HTTP en un `CreateOrderCommand` (DTO).

3. 🧠 El DTO se **valida** automáticamente gracias a anotaciones como `@NotNull` y `@Valid`.

4. 🔌 El controlador **invoca un input port**, como `OrderApplicationService.createOrder()`.

5. 🎯 La implementación del input port:
    - Usa el **OrderDataMapper** para convertir el `CreateOrderCommand` en entidades del dominio (`Order`, `OrderItem`, etc.).
    - Llama al `OrderDomainService` para ejecutar la lógica de negocio.
    - Usa los **repositorios** para consultar o guardar datos.
    - Devuelve un `CreateOrderResponse` como DTO al cliente.

---

### 4️⃣ ¿Cómo se activan los eventos desde la aplicación?

- El **dominio** genera los eventos (`OrderCreatedEvent`, `OrderPaidEvent`, etc.), pero **no los dispara** directamente.
- El **servicio de aplicación** es quien recibe el evento como retorno del `OrderDomainService`.
- Luego usa un **publisher** (de `order.service.domain.ports.output.message.publisher`) para publicarlo:

```java
orderCreatedPaymentRequestMessagePublisher.publish(orderCreatedEvent);
```

Cada publisher implementa la interfaz genérica `DomainEventPublisher<T>`, lo que permite desacoplar la publicación real (Kafka, RabbitMQ, etc.).

> 💡 Así aseguramos que primero se guarda el estado en base de datos y luego se publica el evento, evitando inconsistencias.

---