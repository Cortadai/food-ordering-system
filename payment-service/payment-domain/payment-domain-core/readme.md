# 🧠 Módulo: `payment-domain-core`

> Este submódulo representa el **núcleo del dominio** del microservicio de pagos (`payment-service`). Contiene toda la lógica de negocio central, entidades, objetos de valor y eventos del dominio.

---

## 📦 Estructura principal

```plaintext
com.food.ordering.system.payment.service.domain
├── entity
├── event
├── exception
├── service
└── valueobject
```

---

## 🧱 Entidades

- `Payment`: entidad principal que representa una transacción de pago.
- `CreditEntry`: seguimiento del crédito actual disponible del cliente.
- `CreditHistory`: historial de transacciones de crédito del cliente.

---

## 📤 Eventos de dominio

- `PaymentCompletedEvent`: emitido cuando un pago se completa exitosamente.
- `PaymentCancelledEvent`: emitido cuando un pago es cancelado.
- `PaymentFailedEvent`: emitido cuando un pago no se puede procesar.

Todos heredan de `PaymentEvent`, clase base del dominio.

---

## 🧩 Value Objects

- `PaymentId`, `CreditEntryId`, `CreditHistoryId`: IDs con identidad del dominio.
- `TransactionType`: tipo de operación (`CREDIT` o `DEBIT`).

---

## 🧠 Servicios de dominio

- `PaymentDomainService`: interfaz con las reglas de negocio.
- `PaymentDomainServiceImpl`: implementación que evalúa validez de pagos, actualiza crédito, y produce eventos.

---

## 🚨 Excepciones

- `PaymentDomainException`
- `PaymentNotFoundException`

---

## 🎯 Propósito

Este módulo encapsula la lógica de negocio sin dependencias externas (como Spring o bases de datos).  
Puede ser testeado de forma unitaria y es completamente agnóstico a tecnología.
