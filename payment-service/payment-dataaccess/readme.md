# 📦 Módulo: `payment-dataaccess`

> Este módulo implementa los **adaptadores de salida** (Output Adapters) del microservicio de pagos. Se encarga del acceso a la base de datos mediante JPA y del mapeo entre entidades del dominio y entidades persistentes.

---

## 🧩 Estructura de paquetes

```plaintext
com.food.ordering.system.payment.service.dataaccess
├── payment
│   ├── adapter
│   ├── entity
│   ├── mapper
│   ├── repository
│   └── exception
├── creditentry
│   └── ...
├── credithistory
│   └── ...
└── outbox
    └── ...
```

---

## 🧱 Entidades JPA

Estas clases representan las tablas de la base de datos:

- `PaymentEntity`
- `CreditEntryEntity`
- `CreditHistoryEntity`
- `OrderOutboxEntity`

---

## 🔁 Repositorios JPA

Interfaces que extienden `JpaRepository` y permiten acceso a base de datos:

- `PaymentJpaRepository`
- `CreditEntryJpaRepository`
- `CreditHistoryJpaRepository`
- `OrderOutboxJpaRepository`

---

## 🔌 Adaptadores (Implementación de puertos de salida)

Implementan los puertos definidos en `payment-application-service`:

- `PaymentRepositoryImpl`
- `CreditEntryRepositoryImpl`
- `CreditHistoryRepositoryImpl`
- `OrderOutboxRepositoryImpl`

---

## 🔄 Mapeadores

Transforman entre entidades del dominio y entidades JPA:

- `PaymentDataAccessMapper`
- `CreditEntryDataAccessMapper`
- `CreditHistoryDataAccessMapper`
- `OrderOutboxDataMapper`

---

## 🚨 Excepciones

Capturan errores específicos de la capa de persistencia:

- `PaymentDataaccessException`
- `CreditEntryDataaccessException`
- `CreditHistoryDataaccessException`
- `OrderOutboxNotFoundException`

---

## 🎯 Propósito

Este módulo conecta el dominio de pagos con la infraestructura de persistencia, sin acoplar el núcleo del negocio a tecnologías externas. Permite:

- Acceder a la base de datos de forma segura y desacoplada
- Persistir y recuperar entidades y eventos outbox
- Adaptar entre el modelo del dominio y el modelo relacional

---

Este es un módulo clave en la arquitectura hexagonal, al actuar como **adaptador secundario** de salida.
