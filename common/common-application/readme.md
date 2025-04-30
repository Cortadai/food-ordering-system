# 🧩 Módulo: `common-application`

> Este módulo proporciona herramientas comunes para la **capa de aplicación**, especialmente para el manejo unificado de errores.

---

## 📦 Componentes

### 🔸 `ErrorDto.java`

- Estructura estándar para representar errores (mensaje, código, detalles).
- Es un **Data Transfer Object** (DTO) que encapsula información sobre errores.
- Contiene campos como:
  - `code`: código de error
  - `message`: mensaje explicativo
  - `timestamp`: momento del error (en formato `ZonedDateTime`)

👉 Es usado para comunicar errores de forma uniforme al cliente, especialmente desde `GlobalExceptionHandler`.

### 🔸 `GlobalExceptionHandler.java`

- Controlador global de errores HTTP con `@RestControllerAdvice`.
- Usa `@RestControllerAdvice` de Spring para interceptar y manejar excepciones en toda la aplicación.
- Define `@ExceptionHandler` específicos:
  - Por ejemplo, `OrderNotFoundException` o `DomainException` son capturadas aquí.
- Cada excepción es transformada en un `ResponseEntity<ErrorDto>` con:
  - Código de estado HTTP apropiado (404, 400, etc.)
  - Mensaje explicativo legible para el cliente.

---

## 🎯 Propósito

- Ofrecer un sistema de manejo de errores común para todos los controladores REST.
- Facilitar una experiencia de desarrollo homogénea y coherente entre microservicios.
