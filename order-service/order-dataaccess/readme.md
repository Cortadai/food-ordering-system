# 📦 Módulo: `order-dataaccess`

> Responsable del acceso a la base de datos del microservicio de pedidos (`order-service`).

Este módulo implementa la **capa de persistencia** siguiendo el patrón de **puertos y adaptadores** (Hexagonal Architecture). Su propósito es convertir las entidades del dominio a representaciones JPA, delegar en repositorios Spring Data JPA, y ofrecer adaptadores que conecten con los puertos definidos en `order-application-service`.

---

## 🧩 Estructura de paquetes

```plaintext
order-dataaccess
├── customer
│   ├── adapter
│   ├── entity
│   ├── mapper
│   └── repository
├── order
│   ├── adapter
│   ├── entity
│   ├── mapper
│   └── repository
├── restaurant
│   ├── adapter
│   ├── mapper
│   └── repository
└── outbox
    ├── payment
    │   ├── adapter
    │   ├── entity
    │   ├── exception
    │   ├── mapper
    │   └── repository
    └── restaurantapproval
        ├── adapter
        ├── entity
        ├── exception
        ├── mapper
        └── repository
```

---

## 🧱 Entidades JPA

Estas clases representan las tablas de la base de datos. Se utilizan en conjunto con Spring Data JPA:

- `CustomerEntity`
- `OrderEntity`, `OrderItemEntity`, `OrderAddressEntity`
- `ApprovalOutboxEntity`, `PaymentOutboxEntity`

---

## 🔁 Repositorios JPA

Interfaces que extienden `JpaRepository`:

- `CustomerJpaRepository`
- `OrderJpaRepository`
- `ApprovalOutboxJpaRepository`
- `PaymentOutboxJpaRepository`

Estas interfaces proporcionan las operaciones básicas CRUD para las entidades persistidas.

---

## 🧩 Adaptadores

Implementaciones concretas de los **puertos de salida** definidos en el dominio:

- `CustomerRepositoryImpl`
- `OrderRepositoryImpl`
- `RestaurantRepositoryImpl`
- `PaymentOutboxRepositoryImpl`
- `ApprovalOutboxRepositoryImpl`

Estos adaptadores encapsulan la lógica de acceso a datos y transforman los modelos a entidades del dominio.

---

## 🔄 Mapeadores

Clases encargadas de convertir entre las entidades del dominio y las entidades JPA persistidas:

- `CustomerDataAccessMapper`
- `OrderDataAccessMapper`
- `RestaurantDataAccessMapper`
- `PaymentOutboxDataAccessMapper`
- `ApprovalOutboxDataAccessMapper`

---

## 🚨 Excepciones

El módulo define sus propias excepciones específicas de la capa de persistencia:

- `PaymentOutboxNotFoundException`
- `ApprovalOutboxNotFoundException`

Permiten propagar errores semánticos sin depender de excepciones de infraestructura.

---

## 📦 Outbox Pattern

Este módulo implementa el **Transactional Outbox Pattern**, que permite almacenar eventos de dominio en una tabla intermedia (`outbox`) para su posterior publicación por un scheduler.

Se usan dos outbox diferenciadas:

- `payment-outbox`: para eventos relacionados con pagos
- `restaurant-approval-outbox`: para eventos relacionados con la aprobación del restaurante

---

## 🎯 Propósito

- Cumplir con los principios de la **arquitectura hexagonal**, manteniendo la lógica de acceso a datos desacoplada del dominio.
- Facilitar el testeo de la lógica del dominio sin depender de la base de datos.
- Proveer mecanismos seguros y resilientes para la persistencia y publicación de eventos asíncronos mediante outbox.

---

Este módulo **no contiene lógica de negocio**, solo responsabilidades relacionadas con la persistencia y adaptación de datos. Es fundamental para garantizar la consistencia de datos y eventos en el microservicio `order-service`.
