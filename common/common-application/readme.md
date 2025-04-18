# 📦 `common-application` - Módulo de excepciones y manejo global

## 🧭 Propósito

Este módulo contiene clases comunes relacionadas con el manejo de excepciones, compartidas entre distintos servicios de la aplicación. Aquí se ubican componentes como controladores globales de errores y objetos de transferencia de errores (DTOs).

---

## 📂 Estructura

```
common-application
└── src
    └── main
        └── java
            └── com.food.ordering.system.application.exception.handler
                ├── ErrorDto.java
                └── GlobalExceptionHandler.java
```

---

## 🧱 Clases principales

### 🔸 `ErrorDto.java`

- Es un **Data Transfer Object** (DTO) que encapsula información sobre errores.
- Contiene campos como:
    - `code`: código de error
    - `message`: mensaje explicativo
    - `timestamp`: momento del error (en formato `ZonedDateTime`)

👉 Es usado para comunicar errores de forma uniforme al cliente, especialmente desde `GlobalExceptionHandler`.

---

### 🔸 `GlobalExceptionHandler.java`

- Usa `@RestControllerAdvice` de Spring para interceptar y manejar excepciones en toda la aplicación.
- Define `@ExceptionHandler` específicos:
    - Por ejemplo, `OrderNotFoundException` o `DomainException` son capturadas aquí.
- Cada excepción es transformada en un `ResponseEntity<ErrorDto>` con:
    - Código de estado HTTP apropiado (404, 400, etc.)
    - Mensaje explicativo legible para el cliente.

---

## 🎯 Rol del módulo

Este módulo ayuda a **uniformar la gestión de errores** en todos los microservicios que lo incluyan como dependencia. Se alinea con buenas prácticas de RESTful APIs: respuestas claras, detalladas y estructuradas cuando ocurre un fallo.

✅ Puede ser incluido en múltiples servicios sin depender del dominio específico de ninguno de ellos.