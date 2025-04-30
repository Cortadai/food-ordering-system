# 🧠 Módulo: `restaurant-domain-core`

> Este submódulo representa el **núcleo del dominio** del microservicio de restaurante. Contiene las entidades, reglas de negocio y eventos relacionados con la validación y aprobación de pedidos por parte del restaurante.

---

## 📦 Estructura principal

```plaintext
com.food.ordering.system.restaurant.service.domain
├── entity
├── event
├── exception
├── service
└── valueobject
```

---

## 🧱 Entidades

- `Restaurant`: raíz del agregado.
- `Product`: producto ofrecido por el restaurante.
- `OrderApproval`: aprobación del pedido.
- `OrderDetail`: detalle del pedido a aprobar.

---

## 📤 Eventos de dominio

- `OrderApprovedEvent`: el restaurante aprueba un pedido.
- `OrderRejectedEvent`: el restaurante rechaza un pedido.
- Ambos heredan de `OrderApprovalEvent`.

---

## 🧠 Servicios de dominio

- `RestaurantDomainService`: interfaz para aprobar o rechazar un pedido.
- `RestaurantDomainServiceImpl`: implementación con lógica de negocio pura.

---

## 🧩 Value Objects

- `OrderApprovalId`: identificador de la aprobación del pedido.

---

## 🚨 Excepciones

- `RestaurantDomainException`
- `RestaurantNotFoundException`

---

## 🎯 Propósito

Este módulo encapsula toda la lógica del dominio de restaurantes y puede ser testeado de forma aislada, sin dependencia de tecnología externa.
