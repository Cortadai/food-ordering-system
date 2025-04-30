# 🗄️ Módulo: `restaurant-dataaccess`

> Este módulo implementa los **adaptadores de salida (Output Adapters)** para el microservicio de restaurante. Se encarga de la persistencia de entidades como `OrderApproval` y la gestión de eventos Outbox.

---

## 🧩 Estructura de paquetes

```plaintext
com.food.ordering.system.restaurant.service.dataaccess
└── restaurant
    ├── adapter
    ├── entity
    ├── repository
    ├── mapper
    └── outbox
        ├── adapter
        ├── entity
        ├── repository
        ├── mapper
        └── exception
```

---

## 🧱 Entidades JPA

- `OrderApprovalEntity`: representa la aprobación de un pedido por parte del restaurante.
- `OrderOutboxEntity`: entidad persistente de eventos para el patrón Outbox.

---

## 🔁 Repositorios JPA

- `OrderApprovalJpaRepository`
- `OrderOutboxJpaRepository`

Proveen acceso a la base de datos mediante Spring Data JPA.

---

## 🔌 Adaptadores

Implementan los puertos de salida definidos en la capa de aplicación:

- `RestaurantRepositoryImpl`
- `OrderApprovalRepositoryImpl`
- `OrderOutboxRepositoryImpl`

Estos adaptadores transforman datos entre el modelo del dominio y las entidades persistentes.

---

## 🔄 Mapeadores

- `RestaurantDataAccessMapper`: transforma entidades del dominio ⇄ JPA.
- `OrderOutboxDataAccessMapper`: convierte entre eventos del dominio y la entidad `OrderOutboxEntity`.

---

## 🚨 Excepciones

- `OrderOutboxNotFoundException`: lanzada si no se encuentra el evento en la tabla outbox.

---

## 🎯 Propósito

- Permitir la persistencia desacoplada de entidades del dominio.
- Gestionar el almacenamiento de eventos `OrderApprovedEvent` o `OrderRejectedEvent` en la outbox.
- Mantener el dominio limpio, sin dependencias de JPA o Spring.

---

Este módulo forma parte de la arquitectura hexagonal como **adaptador secundario** y permite que el dominio persista su estado de manera desacoplada y segura.
