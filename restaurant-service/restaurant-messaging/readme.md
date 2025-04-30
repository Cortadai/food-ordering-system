# 📦 Módulo: `restaurant-messaging`

> Este módulo implementa los adaptadores Kafka del microservicio de restaurante. Permite consumir solicitudes de aprobación de pedidos desde Kafka y publicar respuestas.

---

## 🧩 Estructura de paquetes

```plaintext
com.food.ordering.system.restaurant.service.messaging
├── listener.kafka
├── publisher.kafka
└── mapper
```

---

## 👂 Listener Kafka

### `RestaurantApprovalRequestKafkaListener`

- Escucha mensajes del tópico `restaurant-approval-request-topic`.
- Recibe `RestaurantApprovalRequestAvroModel` desde Kafka.
- Convierte el mensaje usando `RestaurantMessagingDataMapper`.
- Llama al puerto de entrada `RestaurantApprovalRequestMessageListener`.

---

## 📤 Publisher Kafka

### `RestaurantApprovalEventKafkaPublisher`

- Publica eventos de aprobación o rechazo del restaurante.
- Convierte eventos del dominio en `RestaurantApprovalResponseAvroModel`.
- Publica hacia el tópico correspondiente.

---

## 🔄 Mapper

### `RestaurantMessagingDataMapper`

- Convierte:
    - Avro → DTO (`RestaurantApprovalRequestAvroModel` → `RestaurantApprovalRequest`)
    - Evento del dominio → Avro (`OrderApprovedEvent` → `RestaurantApprovalResponseAvroModel`)

Permite mantener el dominio desacoplado de la lógica de mensajería.

---

## ⚙️ Tecnologías usadas

- Spring Kafka (`@KafkaListener`)
- Apache Avro para la serialización/deserialización
- Configuración de tópicos y grupos de consumidores en `application.yml`

---

## 🧱 Rol en la arquitectura

- **Adaptador de entrada**: `RestaurantApprovalRequestKafkaListener`
- **Adaptador de salida**: `RestaurantApprovalEventKafkaPublisher`

Este módulo permite la comunicación asíncrona del microservicio de restaurante con el resto del sistema.

---

Forma parte fundamental de la arquitectura hexagonal, conectando el dominio con Kafka sin acoplarse directamente a detalles técnicos.
