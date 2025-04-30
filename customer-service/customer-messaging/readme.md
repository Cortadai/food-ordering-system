# 📦 Módulo: `customer-messaging`

> Este módulo implementa el **adaptador Kafka de salida** para el microservicio de clientes (`customer-service`). Publica eventos del dominio cuando se crea un nuevo cliente.

---

## 🧩 Estructura del módulo

```plaintext
com.food.ordering.system.customer.service.messaging
├── publisher.kafka
└── mapper
```

---

## 📤 Publisher Kafka

### `CustomerCreatedEventKafkaPublisher`

- Publica eventos `CustomerCreatedEvent` hacia Kafka.
- Convierte el evento del dominio en un modelo Avro (`CustomerAvroModel`).
- Implementa el puerto de salida `CustomerMessagePublisher`.

---

## 🔄 Mapper

### `CustomerMessagingDataMapper`

- Convierte:
    - `CustomerCreatedEvent` → `CustomerAvroModel`
- Permite mantener el dominio desacoplado del modelo de mensajería externa.

---

## 🧱 Rol en la arquitectura

- Este módulo actúa como **adaptador secundario (output adapter)** en la arquitectura hexagonal.
- Se integra con Kafka para enviar eventos cuando se registra un nuevo cliente.

---

## ✅ Propósito

Garantiza la publicación de eventos de creación de cliente de forma desacoplada del núcleo del dominio, usando Kafka como canal de comunicación asíncrono.
