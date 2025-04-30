# 📦 Módulo: `payment-application-service`

> Este submódulo implementa los **casos de uso del dominio de pagos**. Actúa como capa de aplicación que orquesta entidades del dominio, repositorios y publicación de eventos.

---

## 🧩 Estructura general

```plaintext
com.food.ordering.system.payment.service.domain
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
└── PaymentRequestHelper.java
```

---

## 🎯 Funciones principales

- Procesar eventos de tipo `PaymentRequest` (por orden creada o cancelada)
- Coordinar la lógica de crédito y validaciones de saldo
- Generar y almacenar eventos outbox (`OrderOutboxMessage`)
- Publicar los eventos de respuesta a través de Kafka

---

## 📥 Puertos de entrada

- `PaymentRequestMessageListener`: interfaz para recibir eventos externos
- `PaymentRequestMessageListenerImpl`: implementación que maneja la solicitud y produce eventos de salida

---

## 📤 Puertos de salida

- `PaymentRepository`, `CreditEntryRepository`, `CreditHistoryRepository`: acceso a datos
- `OrderOutboxRepository`: almacenamiento de eventos de dominio
- `PaymentResponseMessagePublisher`: publica mensajes hacia otros servicios

---

## 🔄 Outbox y Scheduler

- `OrderEventPayload`, `OrderOutboxMessage`: eventos serializados
- `OrderOutboxScheduler`: publica eventos periódicamente
- `OrderOutboxCleanerScheduler`: limpia eventos antiguos
- `OrderOutboxHelper`: lógica auxiliar de publicación

---

## 🔁 Mapper y DTO

- `PaymentDataMapper`: conversión entre DTO y entidades
- `PaymentRequest`: contiene datos de entrada para crear o cancelar un pago

---

## ⚠️ Excepciones

- `PaymentApplicationServiceException`: errores durante la ejecución del caso de uso

---

## ✅ Conclusión

Este módulo representa la **capa de aplicación** en la arquitectura hexagonal. Coordina entidades del dominio y asegura la ejecución de la lógica de negocio junto a la persistencia y publicación de eventos.
