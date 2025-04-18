# 📦 Módulo: `order-dataaccess`

> Responsable de acceder a la base de datos relacional del microservicio de pedidos.

Contiene la capa de **acceso a datos** (Data Access Layer) del microservicio de pedidos. Se encarga de:

- Mapear entidades de dominio a entidades JPA.
- Interactuar con la base de datos usando repositorios Spring Data JPA.
- Encapsular detalles de persistencia mediante adaptadores que implementan los puertos definidos en `order-application-service`.

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
└── restaurant
    ├── adapter
    ├── entity
    ├── exception
    ├── mapper
    └── repository
```

---

## 🧱 Entidades JPA

Estas clases representan las tablas de la base de datos:

- `CustomerEntity`
- `OrderEntity`, `OrderItemEntity`, `OrderAddressEntity`
- `RestaurantEntity`, `RestaurantEntityId`

---

## 🔁 Repositorios JPA

Interfaces para acceder a la base de datos usando Spring Data JPA:

- `CustomerJpaRepository`
- `OrderJpaRepository`
- `RestaurantJpaRepository`

---

## 🔁 Adaptadores

Implementaciones de los puertos de salida definidos en el dominio:

- `CustomerRepositoryImpl`
- `OrderRepositoryImpl`
- `RestaurantRepositoryImpl`

Estos adaptadores son usados por los servicios de aplicación para interactuar con la base de datos.

---

## 🔄 Mapeadores

Transforman entre entidades del dominio y entidades JPA:

- `CustomerDataAccessMapper`
- `OrderDataAccessMapper`
- `RestaurantDataAccessMapper`

---

## 🚨 Excepciones

- `RestaurantDataAccessException`: encapsula errores de acceso a datos relacionados con restaurantes.

---

## 🎯 Propósito

El módulo `order-dataaccess` implementa una capa de persistencia desacoplada del núcleo del dominio. Gracias a la separación por paquetes y responsabilidades:

- Se respeta el patrón de puertos y adaptadores (arquitectura hexagonal).
- Se facilita el testeo del dominio sin necesidad de base de datos.
- Se permite intercambiar la tecnología de persistencia si fuera necesario.