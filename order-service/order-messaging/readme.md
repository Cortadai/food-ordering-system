# 📦 Módulo: `order-messaging`

> Implementa los adaptadores de salida responsables de publicar eventos de dominio hacia los tópicos de Kafka.

---

## 🧩 Estructura de paquetes

```plaintext
order-messaging
└── publisher
    └── kafka
└── mapper
└── publisher
    └── kafka
```

---

## 📨 `message.publisher.kafka`

Contiene adaptadores **outbound** que implementan los puertos definidos en `order-application-service` y se encargan de publicar eventos a Kafka.

### ✅ Publicadores de eventos de dominio:

- `CreateOrderKafkaMessagePublisher`
- `CancelOrderKafkaMessagePublisher`
- `PayOrderKafkaPublisher`

Cada clase implementa la interfaz `DomainEventPublisher<T>` y publica eventos como:

- `OrderCreatedEvent`
- `OrderCancelledEvent`
- `OrderPaidEvent`

Usan Kafka para emitir los eventos hacia los servicios de **pago** y **restaurante**.

---

## 🔄 `message.mapper`

Contiene mapeadores que traducen los eventos de dominio a mensajes específicos para Kafka:

- `OrderMessagingDataMapper`

Por ejemplo, convierte un `OrderCreatedEvent` en un `PaymentRequestAvroModel` antes de ser enviado.

---

Gracias por compartir las clases, están muy claras y bien estructuradas. Con esa base, aquí tienes la sección actualizada y ampliada para `listener.kafka` dentro del documento del módulo `order-messaging`:

---

## 👂 `listener.kafka`

Este paquete contiene los **adaptadores inbound de eventos Kafka**, es decir, los **consumidores de mensajes** que vienen de otros servicios (como el de pagos o restaurantes).

### 🎧 Clases principales:

#### ✅ `PaymentResponseKafkaListener`

- Escucha mensajes del tópico `payment-response-topic-name`.
- Usa un `@KafkaListener` para recibir mensajes tipo `PaymentResponseAvroModel`.
- Según el estado del pago (`COMPLETED`, `CANCELLED`, `FAILED`), delega el procesamiento en:
    - `paymentCompleted()` o
    - `paymentCancelled()` del puerto de entrada `PaymentResponseMessageListener`.
- Convierte los mensajes Avro a DTO del dominio con ayuda del `OrderMessagingDataMapper`.

#### ✅ `RestaurantApprovalResponseKafkaListener`

- Escucha mensajes del tópico `restaurant-approval-response-topic-name`.
- Usa un `@KafkaListener` para recibir mensajes tipo `RestaurantApprovalResponseAvroModel`.
- Según el estado de aprobación (`APPROVED`, `REJECTED`), llama a:
    - `orderApproved()` o
    - `orderRejected()` del puerto de entrada `RestaurantApprovalResponseMessageListener`.
- También utiliza `OrderMessagingDataMapper` para adaptar los datos.

### 📌 ¿Qué hacen estos listeners?

- Actúan como **adaptadores secundarios de entrada**, parte del patrón hexagonal.
- **Conectan el mundo externo (Kafka)** con los **servicios de dominio**, **sin que el dominio sepa de Kafka**.
- Se encargan de:
    - **Escuchar** los eventos que vienen de otros microservicios.
    - **Traducirlos** con los mapeadores.
    - **Llamar a los servicios del dominio** para continuar el flujo de negocio.

### ⚙️ Detalles técnicos:

- Los listeners implementan una interfaz genérica `KafkaConsumer<T>`.
- Usan `@KafkaListener` de Spring para la integración automática con Kafka.
- El sistema utiliza propiedades externas (`application.yml`) para configurar:
    - Los IDs de los consumer groups.
    - Los nombres de los tópicos.

---

## 🛠️ Dependencias y herramientas

- Usa `KafkaProducerConfigData` para obtener configuración de tópicos y brokers.
- Utiliza clases generadas por Avro para construir los mensajes Kafka.

---

## 🎯 Propósito

Este módulo asegura que:

- El servicio de pedidos no conoce los detalles técnicos de Kafka.
- La publicación de eventos sigue el patrón de eventos de dominio (Domain Event Publisher).
- La lógica de publicación es completamente desacoplada del núcleo del dominio.