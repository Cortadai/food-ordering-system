# 🌐 Módulo: `customer-application`

> Este módulo actúa como **adaptador primario (input adapter)** del microservicio de clientes (`customer-service`). Expone una API REST para registrar nuevos clientes y manejar excepciones.

---

## 🧩 Estructura del módulo

```plaintext
com.food.ordering.system.customer.service.application
├── rest
│   └── CustomerController.java
└── handler
    └── CustomerGlobalExceptionHandler.java
```

---

## 🔌 Controlador REST

### `CustomerController`

- Anotado con `@RestController`.
- Expone los endpoints:
    - `POST /customers` → crear un cliente.
- Usa:
    - `CustomerApplicationService` (input port)
    - `CustomerDataMapper` para mapear DTOs

---

## ⚠️ Manejador de excepciones

### `CustomerGlobalExceptionHandler`

- Anotado con `@RestControllerAdvice`.
- Usa `@ExceptionHandler` para capturar y formatear errores.
- Maneja excepciones como `CustomerDomainException`.

---

## ✅ Rol en la arquitectura

- Es la **entrada principal al microservicio** desde el exterior (HTTP).
- Transforma datos de entrada en comandos del dominio.
- Garantiza respuestas coherentes y seguras ante errores.

---

Este módulo forma parte de la arquitectura hexagonal como **adaptador de entrada** y no contiene lógica de negocio.
