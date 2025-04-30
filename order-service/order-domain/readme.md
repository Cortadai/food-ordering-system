# 🧠 Módulo `order-domain`

> Este módulo actúa como un **contenedor lógico y estructural (POM)** que agrupa la lógica del dominio del microservicio de pedidos.
> Implementa los principios de **Domain-Driven Design (DDD)** y **Arquitectura Hexagonal**, separando el núcleo del dominio y la lógica de aplicación.

---

## 📦 Submódulos incluidos

- [`order-domain-core`](./order-domain-core/readme.md): contiene el **núcleo del dominio**, como entidades, objetos de valor, eventos y servicios de dominio. No tiene dependencias externas.
- [`order-application-service`](./order-application-service/readme.md): implementa los **casos de uso del dominio**. Expone los puertos de entrada (input ports), coordina lógica, mapea datos y orquesta flujos con SAGA y outbox.

---

## 🧱 Arquitectura

Este módulo sigue una estructura basada en **Clean Architecture** y **Hexagonal Architecture**, donde:

- `order-domain-core` representa el **dominio puro**, independiente de frameworks.
- `order-application-service` actúa como una capa de orquestación, coordinando lógica de negocio y conectándose con el resto del sistema mediante **puertos y adaptadores**.

---

## 📁 Detalle de submódulos

### 🔹 `order-domain-core`

Contiene los bloques fundamentales del modelo de dominio:

#### Entidades (`entity`)
- `Order`: raíz del agregado. Coordina `OrderItem`, `TrackingId`, `OrderStatus`, etc.
- `OrderItem`, `Product`, `Customer`, `Restaurant`

#### Objetos de valor (`valueobject`)
- `OrderItemId`, `StreetAddress`, `TrackingId`, `Money`, `OrderStatus`, etc.

#### Eventos de dominio (`event`)
- `OrderCreatedEvent`, `OrderPaidEvent`, `OrderCancelledEvent`

#### Excepciones (`exception`)
- `OrderDomainException`, `OrderNotFoundException`

#### Servicios de dominio (`service`)
- `OrderDomainService` y su implementación
- Encapsulan reglas de negocio que afectan a múltiples entidades

### 🔹 `order-application-service`

Expone los **casos de uso** y coordina la lógica de negocio usando el dominio:

#### DTOs (`dto`)
- `CreateOrderCommand`, `CreateOrderResponse`
- `TrackOrderQuery`, `TrackOrderResponse`
- `CustomerModel`, `PaymentResponse`, `RestaurantApprovalResponse`

#### Mapper
- `OrderDataMapper`: convierte entre DTOs y entidades

#### Puertos (`ports`)
- **Input:**
    - `OrderApplicationService`
    - Listeners de Kafka: `CustomerMessageListener`, `PaymentResponseMessageListener`, `RestaurantApprovalResponseMessageListener`
- **Output:**
    - Repositorios: `OrderRepository`, `CustomerRepository`, `RestaurantRepository`
    - Publicadores: `PaymentRequestMessagePublisher`, `RestaurantApprovalRequestMessagePublisher`

#### SAGA y Outbox
- Coordinadores: `OrderApprovalSaga`, `OrderPaymentSaga`
- Modelos de outbox: `OrderApprovalOutboxMessage`, `OrderPaymentOutboxMessage`
- Schedulers: limpieza y publicación periódica de eventos desde la outbox

#### Implementaciones clave
- `OrderApplicationServiceImpl`
- `OrderCreateCommandHandler`, `OrderTrackCommandHandler`
- `OrderCreateHelper` (gestión transaccional de persistencia + eventos)

---

## 🔄 Flujo de creación de pedido

1. Se recibe `CreateOrderCommand` desde un controlador o mensaje.
2. `OrderApplicationServiceImpl` lo procesa usando `OrderCreateCommandHandler`.
3. Se validan cliente y restaurante.
4. Se transforma el comando en entidad `Order`.
5. Se llama a `OrderDomainService.validateAndInitiateOrder(...)`.
6. Se persiste la orden.
7. Se guarda un mensaje en outbox y se publica evento de creación.

---

## ✅ Conclusión

Este módulo encapsula completamente la **lógica de negocio** del dominio de pedidos. Aísla el conocimiento del dominio del resto del sistema y facilita pruebas, mantenibilidad y evolución futura.

Es un claro ejemplo de cómo aplicar **DDD + Clean Architecture + SAGA + Outbox** de forma estructurada en un sistema de microservicios.
