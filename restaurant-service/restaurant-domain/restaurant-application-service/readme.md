# 📦 Módulo: `restaurant-application-service`

> Este módulo implementa los **casos de uso del restaurante**. Orquesta la lógica de aprobación de pedidos, la validación del restaurante y la respuesta a través de eventos.

---

## 🧩 Estructura general

```plaintext
com.food.ordering.system.restaurant.service.domain
├── config
├── dto
├── exception
├── mapper
├── outbox
│   ├── model
│   └── scheduler
├── ports
│   ├── input.message.listener
│   └── output.{repository,message.publisher}
└── RestaurantApprovalRequestHelper.java
```

---

## 📥 Puertos de entrada

- `RestaurantApprovalRequestMessageListener`: interfaz para recibir solicitudes de aprobación.
- `RestaurantApprovalRequestMessageListenerImpl`: implementación que maneja las solicitudes desde Kafka.

---

## 📤 Puertos de salida

- `RestaurantRepository`: acceso a datos del restaurante.
- `OrderApprovalRepository`: persistencia de la aprobación.
- `OrderOutboxRepository`: almacenamiento de eventos.
- `RestaurantApprovalResponseMessagePublisher`: publica la respuesta de aprobación o rechazo.

---

## 🔄 Outbox y Scheduler

- `OrderOutboxMessage`, `OrderEventPayload`: modelos de evento outbox.
- `OrderOutboxHelper`: lógica auxiliar.
- `OrderOutboxScheduler`: publica los eventos periódicamente.
- `OrderOutboxCleanerScheduler`: elimina eventos antiguos.

---

## 🔁 Mapper y DTO

- `RestaurantDataMapper`: transforma entre DTOs y entidades.
- `RestaurantApprovalRequest`: objeto de entrada desde Kafka.

---

## ⚠️ Excepciones

- `RestaurantApplicationServiceException`: errores propios del servicio de aplicación.

---

## ✅ Conclusión

Este módulo coordina la lógica de validación del restaurante y maneja los eventos de entrada y salida relacionados con la aprobación o rechazo de pedidos.
