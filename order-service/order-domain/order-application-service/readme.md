# 🧩 order-application-service

> Este submódulo representa **la capa de aplicación del dominio** para el microservicio de pedidos.

> Aquí se encuentran los **input ports**, servicios de aplicación, mapeadores, y adaptadores para eventos de dominio.

---

## 📦 Estructura de paquetes

```text
order-application-service
└── src
    └── main
        └── java
            └── com.food.ordering.system.order.service.domain
                ├── dto
                │   ├── create
                │   ├── track
                │   └── message
                ├── mapper
                ├── port
                │   ├── input
                │   │   ├── service
                │   │   └── message.listener
                │   │       ├── payment
                │   │       └── restaurant
                │   └── output
                │       ├── repository
                │       └── message.publisher
                │           ├── payment
                │           └── restaurant
                ├── config
                └── service
```

---

## 🎯 Responsabilidades clave

- Recibir comandos y queries del cliente (input ports)
- Coordinar validaciones y lógica del dominio
- Manejar transacciones
- Publicar eventos de dominio (pero no crearlos)

---

## 🎒 Paquete `dto`

Contiene clases de transferencia de datos:

- `CreateOrderCommand`, `CreateOrderResponse`
- `TrackOrderQuery`, `TrackOrderResponse`
- `PaymentResponse`, `RestaurantApprovalResponse`

Estas clases usan anotaciones de Lombok y validaciones con `@NotNull`, `@Size`, etc.

---

## 🔁 Paquete `mapper`

### `OrderDataMapper`

Contiene métodos para:

- Convertir DTOs a entidades del dominio (`CreateOrderCommand → Order`)
- Convertir entidades a respuestas (`Order → CreateOrderResponse`, `Order → TrackOrderResponse`)
- Convertir `CreateOrderCommand → Restaurant` con solo productIds (para validación)

---

## 🧪 Paquete `port`

### 📥 `input`

#### `OrderApplicationService`

- Input port principal.
- Define métodos:
  - `createOrder(CreateOrderCommand)`
  - `trackOrder(TrackOrderQuery)`

#### `PaymentResponseMessageListener`
- Define:
  - `paymentCompleted(PaymentResponse)`
  - `paymentCancelled(PaymentResponse)`

#### `RestaurantApprovalResponseMessageListener`
- Define:
  - `orderApproved(RestaurantApprovalResponse)`
  - `orderRejected(RestaurantApprovalResponse)`

### 📤 `output`

#### `repository`

- `OrderRepository`, `CustomerRepository`, `RestaurantRepository`
- Interfaz para acceder a datos desde la infraestructura

#### `message.publisher`

- Publicadores de eventos:
  - `OrderCreatedPaymentRequestMessagePublisher`
  - `OrderCancelledPaymentRequestMessagePublisher`
  - `OrderPaidRestaurantRequestMessagePublisher`

Todos extienden una interfaz genérica `DomainEventPublisher<T>`

---

## 🧠 Paquete `service`

### `OrderApplicationServiceImpl`

- Implementa `OrderApplicationService`
- Llama a `OrderCreateCommandHandler` y `OrderTrackCommandHandler`

### `OrderCreateCommandHandler`

- Orquesta:
  - Validación de existencia de cliente
  - Validación del restaurante
  - Conversión DTO → entidad
  - Llamada a `OrderDomainService`
  - Persistencia
  - Publicación de evento

### `OrderTrackCommandHandler`

- Lógica simple:
  - Buscar orden por `trackingId`
  - Convertir a DTO

### `OrderCreateHelper`

- Extrae la lógica transaccional en un método separado (por limitaciones del proxy de Spring y `@Transactional`)
- Permite asegurar que el evento se publica **solo tras persistencia exitosa**

---

## 🧩 Eventos y transacciones

Se presentan dos enfoques para publicar eventos:

1. **Publicar desde el servicio de aplicación** después de `@Transactional` → más directo
2. **Usar `@TransactionalEventListener`** para reaccionar tras el commit

Se opta por el primer enfoque, delegando publicación a interfaces `DomainEventPublisher<T>`.

---

## ✅ Conclusión

`order-application-service` orquesta la ejecución del caso de uso `createOrder` y `trackOrder`, respetando los principios de Clean Architecture:

- Lógica de negocio en el dominio
- Infraestructura desacoplada por interfaces
- Input/Output Ports bien definidos

Es el lugar donde se define **qué se hace**, pero no **cómo se guarda o publica**. Es el cerebro coordinador del microservicio.