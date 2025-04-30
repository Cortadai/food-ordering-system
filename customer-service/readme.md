# 👤 Módulo principal: `customer-service`

> Este módulo representa el microservicio de **clientes** dentro del sistema de pedidos. Permite la creación de nuevos clientes y la publicación de eventos asociados.

Está compuesto siguiendo principios de **Domain-Driven Design (DDD)**, **Arquitectura Hexagonal** y **event-driven architecture**.

---

## 📦 Submódulos incluidos

| Submódulo                     | Descripción                                                                 |
|-------------------------------|-----------------------------------------------------------------------------|
| `customer-domain`             | Lógica de negocio y casos de uso del dominio de clientes                   |
| `customer-dataaccess`         | Adaptadores JPA para persistencia                                          |
| `customer-messaging`          | Publicador Kafka para eventos `CustomerCreated`                            |
| `customer-application`        | Controlador REST y manejo de excepciones                                   |
| `customer-container`          | Composición y arranque con Spring Boot                                     |

---

## 🔁 Flujo típico

```text
[POST /customers]
      |
      v
CustomerController (REST)
      |
      v
CustomerApplicationServiceImpl
      |
      v
CustomerCreateCommandHandler
      |
      v
CustomerDomainService → Customer + CustomerCreatedEvent
      |
      v
CustomerRepositoryImpl.save()
      |
      v
CustomerCreatedEventKafkaPublisher
```

---

## 🧠 Componentes clave

| Tipo                     | Clase / Interfaz                                 |
|--------------------------|--------------------------------------------------|
| Entidad del dominio      | `Customer`                                       |
| Servicio de dominio      | `CustomerDomainServiceImpl`                      |
| Evento de dominio        | `CustomerCreatedEvent`                           |
| Caso de uso              | `CustomerCreateCommandHandler`                   |
| Puerto de entrada        | `CustomerApplicationService`                     |
| Puerto de salida         | `CustomerMessagePublisher`, `CustomerRepository` |
| Adaptador REST           | `CustomerController`                             |
| Adaptador Kafka          | `CustomerCreatedEventKafkaPublisher`             |
| Mapper                   | `CustomerDataMapper`, `CustomerMessagingDataMapper` |

---

## 🧱 Patrón aplicado

- Clean Architecture (capas independientes)
- Hexagonal Architecture (puertos + adaptadores)
- Event-Driven (evento de cliente creado → Kafka)

---

## ✅ Conclusión

`customer-service` es un microservicio simple, autónomo y altamente desacoplado. Gestiona clientes de forma robusta y los integra con el resto del sistema mediante eventos Kafka.
