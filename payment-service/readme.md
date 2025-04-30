# 💳 Módulo principal: `payment-service`

> Este módulo representa el microservicio completo de **pagos** dentro del sistema de pedidos. Agrupa submódulos Maven especializados en dominio, persistencia, mensajería y composición.

Sigue los principios de **Arquitectura Hexagonal**, **Domain-Driven Design (DDD)** y aplica patrones como **SAGA** y **Transactional Outbox**.

---

## 📦 Submódulos incluidos

| Submódulo                    | Descripción                                                                                  | Rol en arquitectura         |
|-----------------------------|----------------------------------------------------------------------------------------------|-----------------------------|
| `payment-domain`            | Núcleo del dominio + orquestación de casos de uso (`core` + `application-service`)          | Dominio y lógica de negocio |
| `payment-dataaccess`        | Adaptadores de salida JPA, persistencia de pagos, crédito, historial, outbox                | Output Adapter              |
| `payment-messaging`         | Adaptadores Kafka para consumir y publicar eventos entre servicios                          | Input/Output Adapters       |
| `payment-container`         | Arranque Spring Boot, configuración de beans, composición final                             | Composición final           |

---

## 🔁 Flujo típico

```text
[Kafka: payment-request-topic]
        |
        v
PaymentRequestKafkaListener
        |
        v
PaymentRequestMessageListenerImpl
        |
        v
PaymentRequestHelper
        |
        v
payment-domain-core: evalúa crédito y genera evento
        |
        v
Guarda Payment y evento en tabla Outbox
        |
        v
Scheduler → Kafka → payment-response-topic
```

---

## 🔧 Patrón aplicado

Este servicio aplica:

- Arquitectura Hexagonal
- DDD con entidades, agregados y value objects (`Payment`, `CreditEntry`, etc.)
- Event Sourcing parcial mediante `PaymentCompletedEvent`, `PaymentFailedEvent`
- Transactional Outbox para publicación fiable
- Mensajería asíncrona con Kafka + Avro

---

## 🧱 Desglose conceptual

| Componente                       | Rol                                                                                   | Ejemplo                                |
|----------------------------------|----------------------------------------------------------------------------------------|----------------------------------------|
| Entidad                          | Modelo de dominio con reglas internas                                                  | `Payment`, `CreditEntry`               |
| Value Object                     | Tipo inmutable con igualdad estructural                                                | `Money`, `PaymentId`, `TransactionType`|
| Servicio de dominio              | Reglas de negocio que involucran múltiples entidades                                   | `PaymentDomainServiceImpl`             |
| Evento de dominio                | Notificación de algo que ocurrió en el negocio                                         | `PaymentCompletedEvent`                |
| Input Adapter                    | Recibe eventos desde Kafka y activa la lógica del dominio                              | `PaymentRequestKafkaListener`          |
| Output Adapter                   | Publica eventos del dominio a Kafka                                                    | `PaymentEventKafkaPublisher`           |
| Repositorio + Mapper             | Almacena entidades y las transforma para persistencia                                  | `PaymentRepositoryImpl`, `Mapper`      |
| Scheduler                        | Publica periódicamente eventos almacenados en Outbox                                   | `OrderOutboxScheduler`                 |

---

## ⚙️ Empaquetado

- El módulo `payment-container` es el que genera el `.jar` ejecutable.
- Incluye `application.yml`, `BeanConfiguration` y archivos de inicialización (`init-schema.sql`).
- Compatible con despliegue en Docker/Kubernetes.

---

## ✅ Conclusión

`payment-service` es un microservicio orientado a eventos, que actúa como intermediario entre el pedido y los sistemas financieros.

Está diseñado para ser desacoplado, extensible, y alineado con las mejores prácticas de microservicios modernos.
