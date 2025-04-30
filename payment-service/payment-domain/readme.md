# 🧠 Módulo: `payment-domain`

> Este módulo actúa como **contenedor lógico (tipo `pom`)** para la lógica de dominio y aplicación del microservicio de pagos (`payment-service`).

Agrupa los submódulos `payment-domain-core` y `payment-application-service`, siguiendo los principios de **arquitectura hexagonal** y **DDD (Domain-Driven Design)**.

---

## 📦 Submódulos incluidos

| Submódulo                     | Descripción                                                                                 |
|------------------------------|---------------------------------------------------------------------------------------------|
| `payment-domain-core`        | Contiene las **entidades del dominio**, reglas de negocio, eventos y objetos de valor      |
| `payment-application-service`| Implementa los **casos de uso**, coordinación de lógica y publicación de eventos (outbox)   |

---

## 🧩 Relación de responsabilidades

```text
[payment-domain-core]
    ├── Payment
    ├── CreditEntry
    ├── CreditHistory
    ├── Eventos: PaymentCompletedEvent, ...
    └── Servicio de dominio: PaymentDomainService

[payment-application-service]
    ├── Listener: PaymentRequestMessageListenerImpl
    ├── Coordinador: PaymentRequestHelper
    ├── DTOs, Mappers
    ├── Publicadores Kafka
    └── Outbox: OrderOutboxMessage, Schedulers
```

---

## 🧱 Flujo típico

```text
1. Llega evento desde Kafka: PaymentRequest
2. Listener lo procesa y llama a PaymentRequestHelper
3. Se consulta el crédito del cliente
4. Se crea o rechaza un Payment
5. Se guarda el Payment y un mensaje en la outbox
6. El scheduler publica el evento de respuesta al siguiente servicio
```

---

## 🎯 Objetivo

Este módulo consolida toda la lógica de dominio y orquestación relacionada con pagos, de forma desacoplada de los detalles tecnológicos.

Permite:

- Reutilización de modelos y lógica
- Separación estricta entre dominio y aplicación
- Integración sencilla con infraestructura y adaptadores

---

## ✅ Conclusión

`payment-domain` encapsula los conceptos clave del microservicio de pagos:
- Validación de crédito
- Gestión del ciclo de vida de un pago
- Generación y publicación de eventos

Es completamente testeable, modular y orientado al dominio.
