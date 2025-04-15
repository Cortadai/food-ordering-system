## 🧠 Notas intermedias: Implementación del Application Service (Order Service)

Estas notas cubren todos los aspectos aprendidos desde que pausamos la edición del documento principal, tras completar el análisis de la estructura inicial del módulo `order-service`.

---

### 🧩 ¿Qué es un input port y cómo se usa?

- Un **input port** es una interfaz que representa los casos de uso o servicios de aplicación.
- Son invocados por los adaptadores primarios (controladores REST, listeners de eventos).
- El dominio no depende de los adaptadores, pero sí define estos contratos para que se cumplan externamente.

Ejemplo:
```java
public interface OrderApplicationService {
    CreateOrderResponse createOrder(@Valid CreateOrderCommand command);
    TrackOrderResponse trackOrder(@Valid TrackOrderQuery query);
}
```


---

### 🗃️ ¿Qué rol juega cada repositorio?

Los **repositorios** permiten que el dominio interactúe con fuentes de datos sin acoplarse a ellas.

- `OrderRepository`: permite guardar y recuperar órdenes por ID o tracking ID.
- `CustomerRepository`: permite buscar clientes por ID.
- `RestaurantRepository`: permite verificar la información actual de un restaurante (productos, precios, etc.).

Estos son **puertos de salida** (output ports) que serán implementados en la infraestructura con tecnologías como JPA.


---

### 🔁 ¿Cómo fluyen los datos desde el cliente hasta el dominio?

1. **Un cliente externo** (frontend o sistema) envía un `CreateOrderCommand` o `TrackOrderQuery`.
2. Llega al **Application Service** (`OrderApplicationServiceImpl`).
3. Este delega en **handlers** (como `OrderCommandHandler`) que se encargan de:
    - Validar cliente y restaurante.
    - Convertir el DTO a entidades de dominio con `OrderDataMapper`.
    - Invocar al `OrderDomainService`.
    - Persistir el resultado usando repositorios.
    - Mapear la entidad a respuesta DTO para devolver al cliente.


---

### 🧨 ¿Cómo se activan los eventos desde la aplicación?

Dos formas vistas en el curso:

#### ✅ Opción 1: Lógica directa
- Se obtiene el `OrderCreatedEvent` del dominio.
- Se guarda el pedido.
- Se publica el evento manualmente tras guardar:
```java
OrderCreatedEvent event = helper.persistOrder(command);
publisher.publish(event);
```

#### 🌱 Opción 2: `@TransactionalEventListener`
- Se publica el evento con `ApplicationEventPublisher`.
- Un listener con esta anotación reacciona **solo si la transacción se completa**:
```java
@TransactionalEventListener
public void handle(OrderCreatedEvent event) {
    publisher.publish(event);
}
```

> ⚠️ Esta opción requiere más configuración pero desacopla mejor las responsabilidades.

---

### 🧩 Nuevas clases importantes

#### `OrderCommandHandler`
- Se encarga de la lógica de `createOrder` y `trackOrder`.
- Delegaba mucho en lógica interna, por eso se refactorizó.

#### `OrderCreateHelper`
- Contiene la lógica de negocio y transacción.
- Inyecta servicios, repositorios, mappers y es responsable de:
    - Validar existencia de cliente y restaurante.
    - Crear el objeto `Order`.
    - Invocar al dominio.
    - Guardar la orden y devolver un evento `OrderCreatedEvent`.

#### `OrderCreatedPaymentRequestMessagePublisher`
- Publica el evento a sistemas externos (como Kafka).
- Es una implementación de `DomainEventPublisher<OrderCreatedEvent>`.

#### `OrderCreatedEventApplicationListener`
- Alternativa basada en Spring para reaccionar a eventos post-transacción.


---

### 🧾 Detalle final: implementación de `trackOrder`

- Se marca como `@Transactional(readOnly = true)`.
- Solo consulta la base de datos usando el trackingId.
- No hay lógica de dominio involucrada.

#### `OrderNotFoundException`
- Se lanza si no se encuentra el pedido.

#### Mapeo
- Se convierte la entidad `Order` a `TrackOrderResponse` vía `OrderDataMapper`.


---

### 🔄 Implementaciones de listeners

Se crean las clases:

- `PaymentResponseMessageListenerImpl`
- `RestaurantApprovalResponseMessageListenerImpl`

Ambas implementan sus respectivos **input ports** para reaccionar a eventos que vendrán de otros microservicios (pagos, restaurantes). Por ahora quedan en blanco y se completarán cuando implementemos la **Saga**.