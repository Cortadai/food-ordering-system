# 📦 Módulo: `order-messaging`

> Este módulo implementa los adaptadores de **entrada (Kafka Listener)** y **salida (Kafka Publisher)** para el microservicio de pedidos (`order-service`), siguiendo el patrón de **puertos y adaptadores** (hexagonal architecture).

---

## 🧩 Estructura de paquetes

```plaintext
order-messaging
├── listener
│   └── kafka
├── publisher
│   └── kafka
└── mapper
```

---

## 👂 Listener Kafka (`listener.kafka`)

Este paquete contiene los **consumidores de mensajes Kafka** que representan los adaptadores inbound del sistema. Conectan los eventos externos (desde otros microservicios) con el dominio.

### Clases principales:

- `CustomerKafkaListener`: recibe eventos de creación de cliente
- `PaymentResponseKafkaListener`: escucha eventos del servicio de pagos
- `RestaurantApprovalResponseKafkaListener`: escucha eventos del servicio de restaurantes

Cada listener:

- Está anotado con `@KafkaListener`
- Deserializa mensajes Avro (`CustomerAvroModel`, `PaymentResponseAvroModel`, etc.)
- Usa `OrderMessagingDataMapper` para traducir a objetos del dominio
- Llama a puertos de entrada definidos en la capa de aplicación como:
  - `CustomerMessageListener`
  - `PaymentResponseMessageListener`
  - `RestaurantApprovalResponseMessageListener`

---

## 📤 Publisher Kafka (`publisher.kafka`)

Este paquete contiene los **publicadores de eventos de dominio** hacia otros servicios vía Kafka.

### Clases principales:

- `OrderPaymentEventKafkaPublisher`: publica eventos de tipo `OrderCreatedEvent`, `OrderCancelledEvent` hacia el servicio de pagos
- `OrderApprovalEventKafkaPublisher`: publica eventos de tipo `OrderPaidEvent` hacia el servicio de restaurantes

Cada publicador:

- Implementa interfaces como `PaymentRequestMessagePublisher` o `RestaurantApprovalRequestMessagePublisher`
- Usa el `OrderMessagingDataMapper` para construir modelos Avro (`PaymentRequestAvroModel`, etc.)
- Publica mensajes usando un `KafkaProducer` genérico

---

## 🔄 Mapeador (`mapper`)

### `OrderMessagingDataMapper`

Responsable de transformar:

- Eventos del dominio → modelos Avro (para publicar)
- Modelos Avro → DTOs del dominio (al consumir)

Este componente garantiza la separación entre modelos internos y externos.

---

## ⚙️ Tecnologías y configuración

- Kafka está integrado con Spring Boot usando `@KafkaListener` y un `KafkaProducer`
- Los nombres de los tópicos, grupos de consumidores y configuración de serialización se definen en:
  - `KafkaConfigData`
  - `KafkaConsumerConfigData`
  - `KafkaProducerConfigData`
- Los mensajes se definen usando **Avro schemas** (ver módulo `kafka-model`)
- Los eventos del dominio se propagan de manera asíncrona a través de **Kafka topics**

---

## 🔁 Integración con la arquitectura hexagonal

Este módulo **implementa puertos definidos en la capa de aplicación**:

- `output.message.publisher` → publicadores Kafka
- `input.message.listener` → listeners Kafka

Así se asegura el **desacoplamiento** entre la lógica de negocio y los mecanismos de mensajería.

---

## 🎯 Propósito

- Conectar el microservicio de pedidos con otros servicios mediante eventos asíncronos
- Asegurar la comunicación fiable y desacoplada entre bounded contexts
- Mantener la lógica de integración externa fuera del dominio de negocio

Este módulo permite que el dominio **permanezca puro**, delegando en adaptadores toda la complejidad de mensajería.
