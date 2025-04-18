# 📦 `common` - Módulo base compartido

El módulo `common` sirve como **raíz de reutilización** para componentes esenciales que deben ser compartidos entre múltiples microservicios dentro del sistema de pedidos.

Este módulo se divide en dos submódulos independientes:

- [`common-domain`](./common-domain/readme.md)
- [`common-application`](./common-application/readme.md)

---

## 📂 `common-domain` – Objetos base del modelo de dominio

Este submódulo define **elementos fundamentales del núcleo de negocio** que pueden ser usados por distintos dominios como `order`, `payment`, `restaurant`, etc.

### ✨ Componentes clave

| Clase / Interfaz                | Descripción                                                                 |
|-------------------------------|-----------------------------------------------------------------------------|
| `BaseEntity<T>`               | Clase abstracta para entidades con ID y lógica de igualdad/hash.            |
| `AggregateRoot<T>`           | Marca semántica para raíces de agregados.                                   |
| `BaseId<T>`                  | Objeto de valor para IDs tipados (`OrderId`, `CustomerId`, etc.).           |
| `Money`                      | Objeto de valor inmutable que encapsula lógica monetaria.                   |
| `OrderStatus`, `PaymentStatus`, `OrderApprovalStatus` | Enums que describen estados válidos.                           |
| `StreetAddress`              | Objeto de valor para direcciones postales.                                  |
| `DomainEvent<T>`             | Interfaz marcador para eventos del dominio.                                 |
| `DomainException`            | Excepción base para errores dentro del dominio.                             |

✅ Este módulo garantiza **consistencia conceptual y reutilización** entre bounded contexts.

---

## 📂 `common-application` – Manejador global de excepciones

Este submódulo contiene el **mecanismo centralizado de gestión de errores**, útil para cualquier servicio que implemente controladores HTTP.

### ✨ Componentes clave

| Clase                         | Descripción                                                                 |
|------------------------------|------------------------------------------------------------------------------|
| `GlobalExceptionHandler`     | Controlador global con `@RestControllerAdvice` para capturar excepciones.   |
| `ErrorDto`                   | Objeto de respuesta estructurado para representar errores.                  |

Se encarga de traducir excepciones del dominio o técnicas en respuestas JSON claras y estandarizadas para el consumidor de la API.

---

## 🧠 Beneficios del módulo `common`

- 🔁 **Reutilización de lógica** entre microservicios sin duplicación.
- 🔒 **Seguridad en tipos** gracias a `BaseId` y objetos de valor.
- 🧱 **Separación clara de capas**: el `domain` no depende de frameworks, el `application` sí.
- ⚠️ **Manejo coherente de errores** para toda la arquitectura.

---

¿Quieres que prepare ahora un documento general similar para `order-service` cuando terminemos con sus submódulos?