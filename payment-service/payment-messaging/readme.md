# 📦 Módulo: `payment-messaging`

> Este módulo implementa los **adaptadores Kafka** para el microservicio de pagos (`payment-service`), tanto para consumir como para publicar mensajes relacionados con transacciones de pago.

---

## 🧩 Estructura de paquetes

```plaintext
com.food.ordering.system.payment.service.messaging
├── listener.kafka
├── publisher.kafka
└── mapper
```

---

## 👂 Listener Kafka

### `PaymentRequestKafkaListener`

- Escucha eventos del tópico `payment-request` (pedido creado o cancelado).
- Usa `@KafkaListener` para recibir mensajes tipo `PaymentRequestAvroModel`.
- Convierte el mensaje con `PaymentMessagingDataMapper`.
- Llama al **puerto de entrada** `PaymentRequestMessageListener` para iniciar la lógica del dominio.

---

## 📤 Publisher Kafka

### `PaymentEventKafkaPublisher`

- Publica eventos del dominio (`PaymentCompletedEvent`, `PaymentFailedEvent`) hacia Kafka.
- Convierte el evento a `PaymentResponseAvroModel` mediante el mapeador.
- Implementa la interfaz `PaymentResponseMessagePublisher` (puerto de salida del dominio).

---

## 🔄 Mapper

### `PaymentMessagingDataMapper`

- Convierte:
    - `PaymentRequestAvroModel` → DTO del dominio (`PaymentRequest`)
    - Eventos del dominio → `PaymentResponseAvroModel`
- Permite mantener el **dominio libre de conocimiento de Avro o Kafka**.

---

## ⚙️ Integración técnica

- Se apoya en clases de configuración como `KafkaConfigData`, `KafkaProducer`, `KafkaConsumer`.
- La serialización y deserialización usa modelos Avro generados automáticamente desde esquemas `.avsc`.

---

## 🧠 Rol en la arquitectura hexagonal

- `PaymentRequestKafkaListener` → **Adaptador primario de entrada**
- `PaymentEventKafkaPublisher` → **Adaptador secundario de salida**

Ambos interactúan con el dominio mediante **puertos definidos en la capa de aplicación**.

---

Este módulo permite que el microservicio de pagos reciba comandos desde otros servicios y publique respuestas de forma asincrónica y desacoplada.
