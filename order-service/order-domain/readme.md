# Módulo `order-domain`

> El módulo `order-domain` representa la lógica del dominio del microservicio de pedidos. Este módulo se divide en dos submódulos que implementan los principios de DDD y Arquitectura Hexagonal:

- [`order-domain-core`](./order-domain-core/readme.md): contiene la lógica central del dominio, como entidades, objetos de valor, eventos y servicios de dominio.
- [`order-application-service`](./order-application-service/readme.md): implementa los casos de uso del dominio. Es la puerta de entrada a la lógica de negocio desde fuera (input ports).

---

## `order-domain-core`

Este submódulo representa el corazón del dominio. Todo su código está libre de dependencias externas o frameworks como Spring.

### Paquetes principales

#### `entity`
- **Order**: Raíz del agregado. Coordina los `OrderItems`, el `TrackingId`, `OrderStatus`, y `StreetAddress`.
- **OrderItem**: Entidad que representa un artículo de pedido.
- **Customer**: Entidad básica para verificar existencia del cliente.
- **Restaurant**: Entidad usada para validar disponibilidad de productos y estado.
- **Product**: Entidad asociada a cada `OrderItem` para validar precios y nombres.

#### `valueobject`
- `OrderItemId`, `StreetAddress`, `TrackingId`: objetos de valor inmutables.

#### `event`
- `OrderCreatedEvent`, `OrderPaidEvent`, `OrderCancelledEvent`: eventos de dominio generados por la entidad `Order`.
- `OrderEvent`: clase base abstracta que encapsula `Order` y `createdAt`.

#### `exception`
- `OrderDomainException`, `OrderNotFoundException`: excepciones propias del dominio.

#### `OrderDomainService`
Interfaz que define los casos de uso del dominio:
- `validateAndInitiateOrder(...)`
- `payOrder(...)`
- `approveOrder(...)`
- `cancelOrderPayment(...)`
- `cancelOrder(...)`

`OrderDomainServiceImpl` implementa esta interfaz y orquesta los agregados para ejecutar las reglas de negocio.

---

## `order-application-service`

Este submódulo representa la capa de aplicación. Es responsable de:
- Orquestar la ejecución de los servicios de dominio.
- Transformar DTOs en entidades del dominio.
- Aplicar validaciones externas (Bean Validation).
- Ejecutar operaciones transaccionales.

### Paquetes principales

#### `dto`
- `CreateOrderCommand`, `CreateOrderResponse`
- `TrackOrderQuery`, `TrackOrderResponse`
- `PaymentResponse`, `RestaurantApprovalResponse`

#### `mapper`
- `OrderDataMapper`: convierte entre DTOs y entidades del dominio.

#### `ports`
- `input.service.OrderApplicationService`: interfaz de caso de uso.
- `input.message.listener.payment.PaymentResponseMessageListener`
- `input.message.listener.restaurantapproval.RestaurantApprovalResponseMessageListener`
- `output.repository.OrderRepository`, `CustomerRepository`, `RestaurantRepository`
- `output.message.publisher.*`: publicadores de eventos para servicios externos

#### Implementaciones
- `OrderApplicationServiceImpl`: implementa `OrderApplicationService`, usa handlers para separar lógica.
- `OrderCreateCommandHandler`, `OrderTrackCommandHandler`: coordinan flujo de datos, usan servicios, mapeadores y repositorios.
- `OrderCreateHelper`: extrae la parte transaccional del caso de uso para garantizar que el evento se dispare solo si se guarda en DB.
- `ApplicationDomainEventPublisher`: alternativa para publicar eventos al finalizar transacción.

#### Listeners
- `PaymentResponseMessageListenerImpl`: escucha eventos de pago.
- `RestaurantApprovalResponseMessageListenerImpl`: escucha eventos de aprobación de restaurante.

---

## Resumen de flujo de creación de orden

1. Cliente envía `CreateOrderCommand`.
2. `OrderApplicationServiceImpl` delega en `OrderCreateCommandHandler`.
3. Se validan cliente y restaurante mediante sus repositorios.
4. Se transforma el comando en entidad `Order`.
5. Se llama a `OrderDomainService.validateAndInitiateOrder(...)`, que devuelve un `OrderCreatedEvent`.
6. Se guarda el pedido.
7. Se publica el evento (con `OrderCreatedPaymentRequestMessagePublisher`).

---

Este módulo encapsula completamente la lógica del dominio del pedido y sigue fielmente los principios de Clean Architecture y DDD, separando responsabilidades, respetando la inmutabilidad y garantizando consistencia.

