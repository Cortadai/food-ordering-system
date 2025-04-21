## ✅ 1. ¿Qué es Kafka?

Apache Kafka es una **plataforma de mensajería distribuida** diseñada para manejar flujos de datos en tiempo real con alta disponibilidad, escalabilidad y durabilidad.

- Kafka **guarda los mensajes en tópicos** (topics).
- Cada tópico está **particionado** (para distribuir carga entre nodos).
- Los **productores** publican mensajes.
- Los **consumidores** los leen.
- Los mensajes no se eliminan tras ser leídos (por defecto), se guardan por un tiempo configurable.

---

## ✅ 2. ¿Qué partes tiene un sistema Kafka?

- **Broker**: Nodo de Kafka que recibe y almacena los mensajes.
- **Tópico**: Categoría donde se publican los mensajes (ej: `payment-response-topic`).
- **Productor (Publisher)**: Publica mensajes en un tópico.
- **Consumidor (Listener)**: Escucha un tópico y procesa los mensajes.
- **Schema Registry (opcional)**: Almacena esquemas Avro para validar mensajes.

---

## ✅ 3. ¿Cómo se organiza Kafka en tu proyecto?

Tu proyecto usa Kafka con una estructura clara:

```
infrastructure
└── kafka
    ├── kafka-config
    ├── kafka-model
    ├── kafka-producer
    └── kafka-consumer
```

- **kafka-config**: define configuración común (`KafkaConfigData`, propiedades del broker, tópicos, grupos…)
- **kafka-model**: modelos Avro generados (mensajes binarios compactos y validados)
- **kafka-producer**: clases utilitarias para producir mensajes.
- **kafka-consumer**: interfaz base para recibir mensajes (usada por listeners)

---

## ✅ 4. Kafka en el proyecto Food Ordering System

>Este sistema implementa una arquitectura de microservicios basada en eventos usando **Apache Kafka** como mecanismo de comunicación asincrónica entre servicios (Order, Payment, Restaurant).

### 🎯 Propósito de Kafka en este sistema

- Garantizar **desacoplamiento** entre microservicios.
- Permitir **comunicación asincrónica** entre bounded contexts.
- Facilitar **eventual consistency** y resiliencia.

### 🧩 Módulos relacionados con Kafka

| Módulo Maven                      | Rol principal                                      |
|----------------------------------|----------------------------------------------------|
| `kafka-config-data`              | Carga configuración de Kafka desde properties.     |
| `kafka-producer`                 | Define configuración y helper para publicar.       |
| `kafka-consumer`                 | Define configuración e interfaz para listeners.     |
| `kafka-model`                    | Clases generadas con **Avro** para mensajes Kafka. |
| `order-messaging`                | Adaptadores OUT que publican eventos a Kafka.      |
| `listener.kafka` (order-service) | Adaptadores IN que reciben eventos Kafka.          |
| `docker-compose/kafka-*.yaml`    | Infraestructura Kafka para desarrollo local.       |

---

### 🔄 ¿Cómo fluye un evento?

#### Ejemplo: Creación de un pedido

1. El cliente hace una petición HTTP ➝ `order-application` (REST Controller).
2. Llama al `OrderApplicationService` que valida y crea el `Order` en el dominio.
3. El dominio genera un `OrderCreatedEvent`.
4. El evento se publica desde `order-messaging.publisher.kafka.CreateOrderKafkaMessagePublisher`.
5. Este publisher usa un `KafkaProducer` que:
    - Mapea el evento a `PaymentRequestAvroModel`.
    - Lo publica en el **tópico de pago**.
6. El **servicio de pago** tiene un listener en `payment-service.listener.kafka`, que:
    - Deserializa el mensaje.
    - Ejecuta su lógica de negocio.
    - Devuelve una respuesta (`PaymentResponseAvroModel`) a un tópico de respuesta.
7. El listener `PaymentResponseKafkaListener` en `order-messaging.listener.kafka` lo recibe.
8. Invoca `PaymentResponseMessageListener` para continuar la saga.

Este patrón se repite para la interacción con **RestaurantService** también.

---

### 🛠️ Producción y Consumo

#### 🔼 Publicadores (Producers)
Implementan `DomainEventPublisher<T>`:

- `CreateOrderKafkaMessagePublisher`
- `CancelOrderKafkaMessagePublisher`
- `PayOrderKafkaPublisher`

#### 🔽 Listeners (Consumers)
Implementan `KafkaConsumer<T>` y escuchan tópicos:

- `PaymentResponseKafkaListener`
- `RestaurantApprovalResponseKafkaListener`

Ambos mapean los modelos Avro recibidos a DTOs de dominio usando:

- `OrderMessagingDataMapper`

---

### 📬 Esquema de eventos con Avro

- Todos los eventos Kafka usan clases generadas a partir de esquemas **Avro**.
- Esto asegura **compatibilidad** y **validación** de estructura de los mensajes.
- Los modelos Avro viven en el módulo `kafka-model`.

---

### 🧪 Ventajas de esta integración con Kafka

- Se respeta el principio **"Don’t talk to strangers"**: cada servicio se comunica solo mediante eventos.
- Permite **procesamiento paralelo**, **reintentos**, y **persistencia** del historial de eventos.
- Apoya patrones como **Saga**, **Outbox**, y **Event-Driven Architecture**.

---

### 🔧 Infraestructura local

En el directorio `infrastructure/docker-compose` hay varios YAMLs para levantar Kafka:

- `kafka-cluster.yaml`: Zookeeper + Kafka.
- `kafka-init.yaml`: crea tópicos al inicio.
- `volumes/kafka`: almacén persistente.

Estos se usan para pruebas locales con todos los servicios conectados entre sí.

---

### ✅ Conclusión

Kafka es la **columna vertebral de comunicación** en esta arquitectura de microservicios. A través de eventos, asegura desacoplamiento, resiliencia y coherencia eventual entre contextos como Pedido, Pago y Restaurante.