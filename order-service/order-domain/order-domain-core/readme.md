# 🧱 Módulo de Dominio de Pedidos (`order-service-domain-core`)

Este documento resume todo el trabajo realizado hasta ahora en la implementación del **dominio de pedidos** dentro de una arquitectura limpia y hexagonal basada en **DDD**.

---

## Tabla de contenidos

- [Diseño general de la entidad Order](#diseño-general-de-la-entidad-order)
- [Value Objects implementados](#value-objects-implementados)
- [Entidades hijas de Order](#entidades-hijas-de-order)
- [Estados del pedido y máquina de estados](#estados-del-pedido-y-máquina-de-estados)
- [Eventos de dominio](#eventos-de-dominio)
- [Servicio de dominio (`OrderDomainService`)](#servicio-de-dominio-orderdomainservice)
- [Resumen final del módulo](#resumen-final-del-módulo)

---

## Diseño general de la entidad `Order`

- `Order` es la **Aggregate Root** del agregado de pedidos.
- Hereda de `AggregateRoot<OrderId>`.
- Sus campos principales incluyen:
    - `CustomerId`, `RestaurantId` → value objects
    - `StreetAddress` → objeto de valor definido localmente
    - `List<OrderItem>` → entidades hijas
    - `TrackingId`, `OrderStatus`, `Money price`, `List<String> failureMessages`
- Implementa una serie de **métodos públicos** que representan operaciones del dominio:
    - `initializeOrder()`
    - `validateOrder()`
    - `pay()`, `approve()`, `initCancel()`, `cancel()`
    - `updateFailureMessages()`

---

## Value Objects implementados

- `Money` (desde `common-domain`): incluye lógica para sumar, restar, comparar, multiplicar.
- `StreetAddress` (local):
    - Campos: `UUID id`, `String street`, `postalCode`, `city`
    - Igualdad definida **sin usar el id**, ya que es solo para persistencia.
- `TrackingId`, `OrderId`, `CustomerId`, `RestaurantId`, `OrderItemId`, `ProductId`: objetos de tipo seguro que extienden `BaseId<T>`.

---

## Entidades hijas de `Order`

### `OrderItem`

- Hereda de `BaseEntity<OrderItemId>`
- Campos:
    - `OrderId orderId`
    - `Product product`
    - `int quantity`
    - `Money price`, `Money subTotal`
- Métodos importantes:
    - `isPriceValid()`: valida que el precio del item sea correcto en base al producto
    - `initializeOrderItem(OrderId, OrderItemId)`

### `Product`

- Hereda de `BaseEntity<ProductId>`
- Campos: `String name`, `Money price`
- Método: `updateWithConfirmedNameAndPrice(String, Money)`

### Otras entidades

- `Customer` y `Restaurant`: entidades simples para verificar existencia y estado.
    - `Restaurant` incluye lista de productos y un flag `active`

---

## Estados del pedido y máquina de estados

- `OrderStatus` es un enum: `PENDING`, `PAID`, `APPROVED`, `CANCELLING`, `CANCELLED`

Los métodos en `Order` gestionan las transiciones de estado según una **máquina de estados simple**:

```text
PENDING → PAID → APPROVED
            ↓         ↓
        CANCELLING    (fin)
            ↓
        CANCELLED
```

Cada método valida el estado previo antes de realizar la transición.

---

## Eventos de dominio

Se crean 3 clases de eventos:

- `OrderCreatedEvent`
- `OrderPaidEvent`
- `OrderCancelledEvent`

Todos extienden de `OrderEvent`, que implementa `DomainEvent<Order>` con campos comunes:

```java
public abstract class OrderEvent implements DomainEvent<Order> {
    private final Order order;
    private final ZonedDateTime createdAt;
    // constructor y getters
}
```

---

## Servicio de dominio `OrderDomainService`

Interfaz y su implementación:

```java
public interface OrderDomainService {
    OrderCreatedEvent validateAndInitiateOrder(Order order, Restaurant restaurant);
    OrderPaidEvent payOrder(Order order);
    void approveOrder(Order order);
    OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages);
    void cancelOrder(Order order, List<String> failureMessages);
}
```

La implementación `OrderDomainServiceImpl`:

- Valida que el restaurante esté activo.
- Compara el precio recibido con el precio real del producto.
- Llama a métodos del agregado `Order` para ejecutar la lógica.
- Crea eventos pero **NO los lanza**: eso se hará desde el servicio de aplicación.

```java
log.info("Order is initiated for order id: {}", order.getId().getValue());
```

👉 Se recomienda **no disparar eventos dentro del dominio** hasta que la persistencia sea exitosa.

---

## Resumen final del módulo

Este módulo contiene todo el **dominio puro** del servicio de pedidos:

- Entidades, objetos de valor y reglas de negocio encapsuladas
- Máquina de estados con transiciones seguras
- Eventos de dominio preparados para propagar cambios
- Servicio de dominio que representa los **casos de uso** desde una perspectiva de DDD

🎯 Este módulo **no conoce ni depende de ningún framework**. Es completamente testable e independiente.