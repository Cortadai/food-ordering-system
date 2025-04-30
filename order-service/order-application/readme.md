# 📦 Módulo: `order-application`

> Este módulo forma parte del microservicio `order-service` y actúa como **adaptador primario** dentro de la arquitectura hexagonal.

> Expone la API REST del servicio de pedidos y transforma las solicitudes HTTP en comandos del dominio. No contiene lógica de negocio.

---

## 🧩 Estructura de paquetes

```plaintext
com.food.ordering.system.order.service.application
├── exception.handler
└── rest
```

---

## 🌐 `rest`

Contiene el controlador REST principal del microservicio:

### `OrderController`
- Anotado con `@RestController`
- Expone los endpoints:
    - `POST /orders` → Crear un pedido
    - `GET /orders/{trackingId}` → Consultar estado del pedido
- Utiliza:
    - `OrderApplicationService` (puerto de entrada del dominio)
    - `OrderDataMapper` (para transformar entre DTOs y entidades del dominio)

---

## ⚠️ `exception.handler`

### `OrderGlobalExceptionHandler`
- Clase anotada con `@RestControllerAdvice`
- Usa métodos con `@ExceptionHandler` para interceptar excepciones y convertirlas en respuestas HTTP apropiadas
- Permite capturar excepciones del dominio o de validación y dar mensajes legibles al cliente

---

## 🎯 Responsabilidades del módulo

- Servir como **punto de entrada HTTP** del microservicio
- Recibir, validar y transformar las peticiones externas (DTOs)
- Delegar la ejecución a la lógica del dominio mediante puertos de entrada (`OrderApplicationService`)
- Encapsular el manejo de errores de forma global

---

## 🔗 Dependencias con otros módulos

- **Depende de:**
    - `order-domain`: para usar `OrderApplicationService` y sus modelos de dominio
    - `common-domain`: para clases utilitarias o tipos comunes

- **No contiene:**
    - Lógica de negocio
    - Conexiones con bases de datos
    - Eventos ni mensajería

---

## ✅ Conclusión

Este módulo representa la “cara pública” del servicio de pedidos. Expone los endpoints que consumen otros sistemas o clientes, orquestando el paso inicial hacia el dominio.

Sigue los principios de **Clean Architecture** y **Hexagonal Architecture**, manteniéndose delgado, enfocado y desacoplado del núcleo de negocio.
