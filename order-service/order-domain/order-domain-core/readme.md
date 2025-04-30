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
- Coordina las reglas de negocio del agregado de pedido.
- Contiene una colección de `OrderItem`s.
- Expone métodos como `validateOrder`, `initializeOrder`, `pay`, `approve`, `initCancel`, `cancel`.

### 🧩 `OrderItem`
- Tiene referencia a `Product`.
- Mantiene `price`, `quantity` y `subTotal`.

### 🍔 `Product`
- Contiene `productId`, `name`, `price`.
- Se usa en `OrderItem` para validar precios reales desde `Restaurant`.

### 👤 `Customer` / `Restaurant`
- Entidades con `id`, `products` y `active` (en `Restaurant`).
- Se usan para validar la existencia antes de crear una orden.

---

## 📁 `valueobject`

Contiene objetos de valor reutilizables, que:
- No tienen identidad propia
- Son inmutables

Incluye:

- `Money`: lógica monetaria (add, subtract, multiply, isGreaterThan...)
- `OrderItemId`, `TrackingId`, etc.
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

## 🧠 Patrón aplicado

Este módulo sigue principios de **Domain-Driven Design (DDD)** y forma parte de la **arquitectura hexagonal** del sistema.

Incluye:

- Entidades y Agregados con lógica de negocio
- Objetos de valor inmutables
- Servicios de dominio con orquestación entre entidades
- Eventos de dominio para comunicación interna
- Excepciones específicas del dominio

---

## 🧪 Testing y dependencias

Este módulo:

- Es completamente **independiente de infraestructura externa** (no depende de Spring, Hibernate, etc.)
- Es altamente **testeable de forma aislada** mediante pruebas unitarias puras

---

## ✅ Conclusión

`order-domain-core` representa el **núcleo de negocio** del servicio de pedidos. Define las reglas, entidades, eventos y objetos fundamentales del dominio.

Es la capa más estable y protegida, sobre la cual giran todas las demás decisiones del sistema.
