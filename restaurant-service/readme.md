# 🍽️ Módulo principal: `restaurant-service`

> Este módulo representa el microservicio completo de **restaurantes** dentro del sistema de pedidos. Abarca validación y aprobación de pedidos desde el punto de vista del restaurante.

Está construido sobre principios de **Arquitectura Hexagonal**, **Domain-Driven Design (DDD)**, y aplica el patrón **Transactional Outbox** para eventos.

---

## 📦 Submódulos incluidos

| Submódulo                       | Descripción                                                                 |
|---------------------------------|-----------------------------------------------------------------------------|
| `restaurant-domain`             | Lógica de negocio + orquestación de casos de uso (`core` + `application`)  |
| `restaurant-dataaccess`         | Adaptadores JPA, persistencia de aprobaciones y eventos outbox             |
| `restaurant-messaging`          | Kafka Listener y Publisher para recibir y emitir eventos                   |
| `restaurant-container`          | Arranque con Spring Boot, configuración y empaquetado final                |

---

## 🔁 Flujo típico

```text
[Kafka: order-paid-event]
        |
        v
RestaurantApprovalRequestKafkaListener
        |
        v
RestaurantApprovalRequestMessageListenerImpl
        |
        v
RestaurantApprovalRequestHelper
        |
        v
Validación: restaurante + productos
        |
        v
Aprobación/Rechazo + evento
        |
        v
Guardar en Outbox
        |
        v
Scheduler publica evento a Kafka
```

---

## 🧠 Elementos clave

| Componente                         | Ejemplo                                              |
|------------------------------------|------------------------------------------------------|
| Entidades del dominio              | `Restaurant`, `Product`, `OrderApproval`             |
| Servicio de dominio                | `RestaurantDomainServiceImpl`                        |
| Evento de dominio                  | `OrderApprovedEvent`, `OrderRejectedEvent`           |
| Puerto de entrada                  | `RestaurantApprovalRequestMessageListener`           |
| Puerto de salida                   | `RestaurantApprovalResponseMessagePublisher`         |
| Adaptador de entrada (Kafka)      | `RestaurantApprovalRequestKafkaListener`             |
| Adaptador de salida (Kafka)       | `RestaurantApprovalEventKafkaPublisher`              |
| Persistencia + Mapper             | `OrderApprovalRepositoryImpl`, `RestaurantDataAccessMapper` |
| Evento Outbox + Scheduler         | `OrderOutboxMessage`, `OrderOutboxScheduler`         |

---

## 🔧 Empaquetado y despliegue

- El módulo `restaurant-container` genera el `.jar` ejecutable.
- Incluye:
    - `application.yml` para configuración
    - `BeanConfiguration` para ensamblaje de beans
    - Scripts como `init-schema.sql`

---

## ✅ Conclusión

`restaurant-service` gestiona el ciclo de aprobación de pedidos, garantiza consistencia con eventos y está completamente desacoplado de tecnología externa.

Es un microservicio orientado a eventos, seguro, modular y alineado con las mejores prácticas modernas.
