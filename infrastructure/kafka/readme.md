# 📡 Kafka Infrastructure Module

> Este módulo forma parte de la capa de **infraestructura** del sistema y encapsula todo lo relacionado con la comunicación asincrónica mediante **Apache Kafka**.  
Está dividido en submódulos con responsabilidades bien definidas para facilitar el desacoplamiento, la escalabilidad y la reutilización en toda la arquitectura hexagonal.

---

## 🧱 Submódulos incluidos

| Submódulo           | Descripción funcional                                                    |
|---------------------|---------------------------------------------------------------------------|
| `kafka-model`       | Modelos Avro compartidos entre productores y consumidores Kafka.          |
| `kafka-producer`    | Lógica de publicación de eventos hacia Kafka (adaptador de salida).       |
| `kafka-consumer`    | Lógica de consumo de eventos desde Kafka (adaptador de entrada).          |
| `kafka-config-data` | Configuración centralizada de Kafka (topics, serializers, factories).     |

---

## 📦 kafka-model

Define las clases Avro utilizadas para serializar/deserializar los eventos que viajan por Kafka:

- Ejemplos: `PaymentRequestAvroModel`, `RestaurantApprovalResponseAvroModel`
- Utilizado por `kafka-producer` y `kafka-consumer`
- Compatible con **Schema Registry** para evolución de esquemas

✅ **Sin dependencias con Spring**. Solo contiene modelos serializables.

---

## 📤 kafka-producer

Implementa adaptadores de salida para publicar eventos Kafka desde el dominio:

- OrderCreated ➝ PaymentRequest
- OrderPaid ➝ RestaurantApprovalRequest
- OrderCancelled ➝ PaymentCancelRequest

Utiliza `KafkaTemplate`, es configurable y testable.  
Depende de `kafka-model` y `kafka-config-data`.

---

## 📥 kafka-consumer

Implementa adaptadores de entrada que escuchan eventos como:

- `PaymentResponse`
- `RestaurantApprovalResponse`

Transforma el mensaje entrante y lo delega a servicios del dominio o aplicación.  
Depende de `kafka-model` y `kafka-config-data`.

---

## ⚙️ kafka-config-data

Contiene las propiedades de configuración comunes para Kafka:

- `bootstrapServers`, `groupId`, `autoOffsetReset`, `acks`, serializers...
- Beans compartidos: `KafkaTemplate`, `ProducerFactory`, `ConsumerFactory`
- Estructurado en clases `@ConfigurationProperties` para inyección automática

---

## 🧠 Rol en la arquitectura hexagonal

Este módulo representa la infraestructura de mensajería asíncrona:

```
[ Dominio ] ←→ [ Puertos ] ←→ [ kafka-producer | kafka-consumer ] ←→ [ Apache Kafka ]
```

Permite separar claramente la lógica de negocio de los detalles técnicos del transporte de eventos.

---

## ✅ Ventajas del diseño modular

| Ventaja | Descripción |
|--------|-------------|
| ♻️ Reutilización | Modelos y configuración compartidos entre servicios |
| 🧩 Separación clara | Cada submódulo tiene una única responsabilidad |
| 🚫 Desacoplamiento | El dominio no conoce Kafka ni depende de su API |
| 🧪 Testabilidad | Se puede testear publicación y consumo de forma aislada |
| ☁️ Escalabilidad | Compatible con microservicios distribuidos y patrones como SAGA |

---

## 📦 Compilación

Este módulo actúa como **padre Maven** de sus submódulos, no contiene código fuente propio.  
Para compilar todo:

```bash
mvn clean install
```

---

## 🔗 Recursos recomendados

- [Apache Kafka](https://kafka.apache.org/)
- [Spring Kafka](https://docs.spring.io/spring-kafka/)
- [Schema Registry](https://docs.confluent.io/platform/current/schema-registry/index.html)
