# 🧠 Módulo: `restaurant-domain`

> Este módulo agrupa la lógica del dominio y la capa de aplicación del microservicio de restaurante (`restaurant-service`). Se divide en dos submódulos: `restaurant-domain-core` y `restaurant-application-service`.

Sigue los principios de **arquitectura hexagonal** y **Domain-Driven Design (DDD)**.

---

## 📦 Submódulos incluidos

| Submódulo                        | Descripción                                                                 |
|----------------------------------|-----------------------------------------------------------------------------|
| `restaurant-domain-core`         | Entidades, objetos de valor, lógica de negocio y eventos del dominio       |
| `restaurant-application-service` | Casos de uso, coordinación de eventos, orquestación y publicación de eventos|

---

## 🧩 Relación de responsabilidades

```text
[restaurant-domain-core]
    ├── Restaurant (agregado)
    ├── Product
    ├── OrderApproval + OrderDetail
    ├── Eventos: OrderApprovedEvent, OrderRejectedEvent
    └── Servicio de dominio: RestaurantDomainService

[restaurant-application-service]
    ├── Listener: RestaurantApprovalRequestMessageListenerImpl
    ├── Coordinador: RestaurantApprovalRequestHelper
    ├── DTOs y Mapper
    ├── Repositorios y Publicadores
    └── Outbox: OrderOutboxMessage + Scheduler
```

---

## 🔁 Flujo típico

```text
1. Kafka: evento OrderCreated
2. Listener → Helper
3. Validación: restaurante activo, productos correctos
4. Aprobación o rechazo del pedido
5. Almacena estado y evento en outbox
6. Scheduler publica respuesta a Kafka
```

---

## 🎯 Objetivo

Este módulo encapsula toda la lógica relacionada con **la aprobación de pedidos por parte del restaurante**, incluyendo:

- Validación de restaurante y productos
- Gestión del estado del pedido
- Emisión de eventos de aprobación o rechazo

---

## ✅ Conclusión

El `restaurant-domain` representa la lógica principal del microservicio de restaurantes y su interacción dentro del sistema de pedidos. Está diseñado para ser modular, testable y desacoplado de la infraestructura.
