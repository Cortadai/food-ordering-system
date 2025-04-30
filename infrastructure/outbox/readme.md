# 📬 Módulo: `outbox`

> Este módulo proporciona una infraestructura reutilizable para implementar el patrón **Transactional Outbox** en los microservicios del sistema.

Permite la publicación confiable y desacoplada de eventos de dominio, asegurando la consistencia eventual.

---

## 📦 Componentes principales

### `OutboxScheduler`

- Interfaz que define cómo debe funcionar un **scheduler** de eventos.
- Debe ser implementada por cada microservicio que publique eventos desde una tabla outbox.
- Método típico: `processOutboxMessage()`

---

### `OutboxStatus` (Enum)

Representa los posibles estados de un mensaje en la tabla outbox:

- `STARTED`: el mensaje se ha generado pero no publicado.
- `COMPLETED`: el mensaje ha sido publicado correctamente.
- `FAILED`: la publicación del mensaje ha fallado.

---

### `SchedulerConfig`

- Clase de configuración que puede habilitar y parametrizar el uso de tareas programadas (`@Scheduled`) en los microservicios.
- Permite activar o desactivar los schedulers de publicación de eventos por propiedad de configuración.

---

## 🎯 Propósito

Este módulo estandariza la forma en que los microservicios:

- Publican eventos de dominio de forma confiable
- Limpian mensajes procesados
- Manejan reintentos y fallos

---

## ✅ Usado en:

- `order-service`: `OrderOutboxScheduler`
- `payment-service`: `PaymentOutboxScheduler`
- `restaurant-service`: `RestaurantApprovalOutboxScheduler`

---

Este módulo forma parte de la **infraestructura compartida** en un sistema orientado a eventos.
