# 🧠 order-domain-core

> Este submódulo contiene **la lógica del dominio de negocio** del microservicio de pedidos.
Se basa en **DDD (Domain-Driven Design)** y contiene las entidades, agregados, objetos de valor, servicios de dominio y eventos.

---

## 📦 Estructura de paquetes

```text
order-domain-core
└── src
    └── main
        └── java
            └── com.food.ordering.system.order.service.domain
                ├── entity
                ├── valueobject
                ├── event
                ├── exception
                └── service
```

---

## 📁 `entity`

Contiene **entidades del dominio** y **raíz de agregado**:

- `Order` → Root Aggregate
- `OrderItem`, `Product` → entidades hijas
- `Customer`, `Restaurant` → entidades agregadas externas

### 🌟 `Order`
- Es la **Aggregate Root**.
- Contiene una colección de `OrderItem`s.
- Coordina las reglas de negocio.
- Expone métodos como `validateOrder`, `initializeOrder`, `pay`, `approve`, `initCancel`, `cancel`.

### 🧩 `OrderItem`
- Tiene referencia a `Product`.
- Mantiene `price`, `quantity` y `subTotal`.

### 🍔 `Product`
- Contiene `productId`, `name`, `price`.
- Se usa en `OrderItem` para validar precios reales desde `Restaurant`.

### 👤 `Customer` / `Restaurant`
- Entidades simples con `id`, `products` y `active` (en `Restaurant`).
- Se usan para validar la existencia antes de crear una orden.

---

## 📁 `valueobject`

Contiene objetos de valor reutilizables, que:
- No tienen identidad propia
- Son inmutables

Incluye:

- `Money`: lógica monetaria (add, subtract, multiply, isGreaterThan...)
- `OrderId`, `CustomerId`, `ProductId`, `RestaurantId`, `TrackingId`, etc.
- `StreetAddress`: para direcciones postales (usado en `Order`)
- `OrderStatus`, `OrderApprovalStatus`, `PaymentStatus`: enums para estado

---

## 📁 `event`

Contiene eventos de dominio:

- `OrderCreatedEvent`
- `OrderPaidEvent`
- `OrderCancelledEvent`

Todos extienden la clase abstracta `OrderEvent`, que implementa la interfaz genérica `DomainEvent<Order>`.

---

## 📁 `exception`

Excepciones específicas del dominio:

- `OrderDomainException`: para errores relacionados con lógica de negocio del pedido
- `OrderNotFoundException`: usado cuando el tracking ID no corresponde a ningún pedido existente

---

## 📁 `service`

Contiene **la interfaz y su implementación del servicio de dominio**:

### 🧠 `OrderDomainService`
- Define métodos como:
    - `validateAndInitiateOrder`
    - `payOrder`
    - `approveOrder`
    - `cancelOrder`
    - `cancelOrderPayment`

### ⚙️ `OrderDomainServiceImpl`
- Implementa la lógica basada en entidades y eventos del dominio
- Crea y devuelve eventos, pero **no los publica**

---

## 🔁 Flujo típico: Crear pedido

1. `OrderApplicationService` llama a `OrderDomainService.validateAndInitiateOrder()`
2. Se valida:
    - Cliente existe
    - Restaurante existe y está activo
    - Precios de los productos son correctos
    - El total coincide con suma de subtotales
3. Se inicializa la orden (ID, trackingId, status = PENDING)
4. Se retorna `OrderCreatedEvent`
5. El servicio de aplicación publica el evento (más adelante, con Outbox/Saga)

---

## ✅ Conclusión

Este módulo contiene **el corazón de la lógica de negocio** del servicio de pedidos. Todo pasa por aquí antes de que algo sea persistido, publicado o validado. Está completamente libre de dependencias externas a Spring (excepto el log dentro de la implementación del dominio).