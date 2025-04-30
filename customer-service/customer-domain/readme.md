# 🧠 Módulo: `customer-domain`

> Este módulo agrupa la lógica de dominio y de aplicación relacionada con los clientes en el sistema. Permite la creación de clientes y la emisión de eventos de dominio.

Está estructurado en dos submódulos: `customer-domain-core` y `customer-application-service`, siguiendo principios de **DDD** y **arquitectura hexagonal**.

---

## 📦 Submódulos incluidos

| Submódulo                       | Descripción                                                   |
|---------------------------------|---------------------------------------------------------------|
| `customer-domain-core`          | Entidad `Customer`, lógica de negocio y eventos del dominio   |
| `customer-application-service`  | Caso de uso: creación de cliente y publicación de eventos     |

---

## 🧩 Relación de responsabilidades

```text
[customer-domain-core]
    ├── Customer (entidad)
    ├── CustomerCreatedEvent
    └── CustomerDomainService

[customer-application-service]
    ├── CustomerApplicationService (input port)
    ├── CustomerCreateCommandHandler
    ├── CustomerMessagePublisher (output port)
    ├── CustomerRepository
    └── Mapper + DTOs (Command + Response)
```

---

## 🔁 Flujo típico

```text
1. Cliente envía un comando para crear un nuevo cliente
2. CustomerApplicationService delega en CustomerCreateCommandHandler
3. Se construye una entidad `Customer` válida
4. Se genera un `CustomerCreatedEvent`
5. Se persiste el cliente y se publica el evento
```

---

## 🎯 Objetivo

Encapsular el comportamiento y reglas de negocio necesarias para gestionar clientes dentro del sistema de pedidos.

---

## ✅ Conclusión

Este módulo separa claramente el **dominio** del cliente de su **coordinación de casos de uso**, manteniendo una estructura limpia, modular y extensible.
