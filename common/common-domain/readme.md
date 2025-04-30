# 🧠 Módulo: `common-domain`

> Este módulo contiene **entidades base**, **objetos de valor reutilizables** y **eventos de dominio genéricos** para todos los microservicios.

---

## 📦 Contenido principal

- `BaseEntity`, `AggregateRoot`: superclases para entidades de dominio.
- `DomainEvent<T>`: interfaz genérica para representar eventos del dominio.
- `DomainEventPublisher<T>`: interfaz para publicar eventos.
- `DomainException`: excepción base común.

---

## 🧩 Objetos de valor (Value Objects)

Estos objetos son compartidos entre microservicios:

- `Money`: cantidad monetaria con validaciones
- `BaseId<T>`: ID genérico
- `OrderId`, `CustomerId`, `RestaurantId`, `ProductId`, etc.
- Enums:
  - `OrderStatus`, `OrderApprovalStatus`
  - `PaymentStatus`, `PaymentOrderStatus`
  - `RestaurantOrderStatus`

---

## 📚 Archivos de Documentación

Este módulo contiene dos archivos Markdown útiles:

- [`aggregates-conceptos.md`](./aggregates-conceptos.md): Explicación de conceptos como Aggregate, Root, Entidad, ValueObject con ejemplos y analogías.
- [`aggregates-implementacion.md`](./aggregates-implementacion.md): Explica la implementación técnica en código Java de los conceptos anteriores.

---

## 🎯 Propósito

- Reutilización de tipos comunes en todos los dominios
- Estandarización de entidades y eventos
- Fundamento del modelo de dominio en cada microservicio