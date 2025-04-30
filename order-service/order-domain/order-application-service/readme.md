# 🧩 order-application-service

> Este submódulo representa **la capa de aplicación del dominio** para el microservicio de pedidos.

> Aquí se encuentran los **input ports**, servicios de aplicación, mapeadores, lógica de orquestación con SAGA y adaptadores para eventos de dominio.

---

## 📦 Estructura de paquetes

```text
order-application-service
└── src
    └── main
        └── java
            └── com.food.ordering.system.order.service.domain
                ├── config
                ├── dto
                │   ├── create
                │   ├── track
                │   └── message
                ├── mapper
                ├── outbox
                │   ├── model
                │   │   ├── approval
                │   │   └── payment
                │   └── scheduler
                │       ├── approval
                │       └── payment
                ├── ports
                │   ├── input
                │   │   ├── service
                │   │   └── message.listener
                │   │       ├── customer
                │   │       ├── payment
                │   │       └── restaurantapproval
                │   └── output
                │       ├── repository
                │       └── message.publisher
                │           ├── payment
                │           └── restaurantapproval
                └── service
```

---

## 🎯 Responsabilidades clave

- Recibir comandos y queries del cliente (input ports)
- Coordinar validaciones y lógica del dominio
- Orquestar flujos de negocio mediante SAGA
- Manejar transacciones con persistencia segura
- Publicar eventos de dominio (pero no crearlos)
- Controlar la programación y limpieza de outbox

---

## 📁 dto

Clases de transferencia de datos (Data Transfer Objects):

- `CreateOrderCommand`, `CreateOrderResponse`
- `TrackOrderQuery`, `TrackOrderResponse`
- `CustomerModel`, `PaymentResponse`, `RestaurantApprovalResponse`
- `OrderItem`, `OrderAddress`

Utilizan anotaciones de Lombok y validaciones con `javax.validation`.

---

## 🔁 mapper

### `OrderDataMapper`

Encargado de transformar:

- DTO → entidades del dominio (`CreateOrderCommand → Order`)
- Entidades → DTOs de respuesta
- DTO → entidades de validación (`Restaurant`, `Customer`, etc.)

---

## 🔌 ports

### 📥 input

- `OrderApplicationService`: expone casos de uso como `createOrder()` y `trackOrder()`
- `CustomerMessageListener`, `PaymentResponseMessageListener`, `RestaurantApprovalResponseMessageListener`: reciben eventos de otros servicios

### 📤 output

- `OrderRepository`, `CustomerRepository`, `RestaurantRepository`: abstraen el acceso a datos
- `PaymentRequestMessagePublisher`, `RestaurantApprovalRequestMessagePublisher`: publican eventos a Kafka u otra mensajería

---

## ⚙️ service

### `OrderApplicationServiceImpl`

- Implementa los métodos definidos por el input port
- Llama a `OrderCreateCommandHandler`, `OrderTrackCommandHandler`

### `OrderCreateCommandHandler`

- Orquesta el caso de uso `createOrder`:
  - Validación de cliente y restaurante
  - Transformación DTO → entidad
  - Llamada a `OrderDomainService`
  - Persistencia de orden y mensaje outbox
  - Devolución del evento de orden creada

### `OrderTrackCommandHandler`

- Busca una orden por su `trackingId` y construye una respuesta

### `OrderCreateHelper`

- Encapsula lógica transaccional separada para evitar problemas con Spring y `@Transactional`

---

## 🔄 SAGA y Outbox

### SAGA Coordinators

- `OrderApprovalSaga`: gestiona la respuesta del restaurante a la orden
- `OrderPaymentSaga`: gestiona la respuesta del servicio de pagos

Ambos coordinan el estado de la orden y gestionan la transición a través de pasos intermedios.

### Outbox & Scheduler

- Modelos de outbox: `OrderApprovalOutboxMessage`, `OrderPaymentOutboxMessage`
- Schedulers: clases como `PaymentOutboxScheduler`, `RestaurantApprovalOutboxScheduler` limpian y publican mensajes periódicamente
- Helpers: encapsulan lógica de acceso y persistencia al outbox

---

## 🧠 Patrón aplicado

Este módulo implementa conceptos de:

- **Arquitectura hexagonal (Ports & Adapters)**
- **Domain-Driven Design (DDD)**
- **SAGA Pattern** para orquestación distribuida
- **Transactional Outbox Pattern** para garantizar consistencia eventual

---

## ✅ Conclusión

`order-application-service` es el coordinador principal del microservicio de pedidos. Se encarga de ejecutar los casos de uso, coordinar el dominio y garantizar que todo se procese de forma robusta, desacoplada y resiliente.

Todo esto sin depender directamente de la infraestructura externa, que queda delegada a los adaptadores.
