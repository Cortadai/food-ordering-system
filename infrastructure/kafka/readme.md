# 📡 Kafka Infrastructure Module

Este módulo forma parte de la capa de **infraestructura** del sistema y encapsula todo lo relacionado con la comunicación asincrónica mediante **Apache Kafka**. Se divide en submódulos para mantener responsabilidades bien separadas y permitir reusabilidad.

---

## 🧱 Submódulos

| Submódulo           | Rol en la arquitectura                                                    |
|---------------------|---------------------------------------------------------------------------|
| `kafka-model`       | Define los modelos de datos compartidos que se envían/reciben por Kafka. |
| `kafka-consumer`    | Implementa la lógica de consumo de eventos desde Kafka.                   |
| `kafka-producer`    | Implementa la lógica de publicación de eventos hacia Kafka.               |
| `kafka-config`      | Contiene clases de configuración comunes (topic names, props, factories). |

---

## 📦 kafka-model

Define las clases Avro utilizadas para serializar/deserializar los eventos que viajan por Kafka.

- Estructuras como `PaymentRequestAvroModel`, `RestaurantApprovalRequestAvroModel`.
- Compartidas por `kafka-producer` y `kafka-consumer`.
- Facilita la evolución de esquemas mediante el uso de **Avro + Schema Registry**.

✅ **No tiene dependencias con Spring. Es solo modelo.**

---

## 📨 kafka-producer

Implementa adaptadores de salida (`Output Ports`) para publicar eventos como:

- OrderCreated ➝ PaymentRequest.
- OrderPaid ➝ RestaurantApprovalRequest.
- OrderCancelled ➝ PaymentCancelRequest.

🛠 Se conecta con Kafka mediante Spring Kafka.

👉 Usa clases de `kafka-model` y depende de `kafka-config`.

---

## 📥 kafka-consumer

Implementa adaptadores de entrada (`Input Ports`) para escuchar eventos como:

- PaymentResponse.
- RestaurantApprovalResponse.

Al recibirlos, invoca listeners definidos como input ports en el módulo de aplicación (`order-service`).

👉 Usa clases de `kafka-model` y depende de `kafka-config`.

---

## ⚙️ kafka-config

Contiene configuraciones comunes de Kafka, como:

- Properties (`ConsumerFactory`, `ProducerFactory`, `KafkaTemplate`).
- Nombres de topics.
- Serializadores y deserializadores.

📦 Reutilizado tanto por `producer` como por `consumer`.

---

## 🧠 Ventajas del diseño modular

- **Reutilización**: los modelos y configuraciones se comparten entre producer y consumer.
- **Separación clara** de responsabilidades.
- **Independencia del dominio**: esta infraestructura puede evolucionar sin afectar la lógica de negocio.
- **Testabilidad**: puedes testear producción/consumo aislados.