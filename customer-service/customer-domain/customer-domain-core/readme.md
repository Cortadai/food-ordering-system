# 🧠 Módulo: `customer-domain-core`

> Este submódulo contiene la **lógica central del dominio de clientes**. Define la entidad `Customer`, sus reglas de negocio y eventos asociados.

---

## 📦 Estructura principal

```plaintext
com.food.ordering.system.customer.service.domain
├── entity
├── event
├── exception
└── service
```

---

## 🧱 Entidad

- `Customer`: entidad principal del dominio de clientes. Tiene identidad y validaciones internas.

---

## 📤 Evento de dominio

- `CustomerCreatedEvent`: se emite cuando se crea correctamente un cliente.

---

## 🧠 Servicio de dominio

- `CustomerDomainService`: interfaz que define reglas de negocio.
- `CustomerDomainServiceImpl`: implementación concreta.

---

## 🚨 Excepciones

- `CustomerDomainException`: para errores relacionados con reglas de cliente.

---

## 🎯 Propósito

Este módulo es independiente de tecnología externa y encapsula la lógica pura del dominio. Puede ser testeado de forma completamente aislada.
