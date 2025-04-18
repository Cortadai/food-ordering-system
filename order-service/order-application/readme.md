# 📦 Módulo: order-application

> Este submódulo forma parte de `order-service` y representa la **capa de aplicación** que conecta el dominio con los adaptadores externos (como APIs REST). Implementa los *input ports* definidos en el dominio y coordina la lógica de negocio.

---

## 📁 Estructura de paquetes

```plaintext
com.food.ordering.system.order.service.application
├── exception.handler
└── rest
```

## 🔥 `exception.handler`
Contiene excepciones específicas del módulo de aplicación, como `ApplicationServiceException`. Son útiles para encapsular errores propios de esta capa antes de que se propaguen a otras capas como infraestructura o presentación.

## 🌐 `rest`
Contiene el controlador REST principal:

- `OrderController`:
    - Anotado con `@RestController`
    - Expone endpoints públicos para:
        - `POST /orders` → Crear un pedido
        - `GET /orders/{trackingId}` → Consultar estado del pedido
    - Usa como dependencias:
        - `OrderApplicationService` (Input Port)
        - `OrderDataMapper` (para mapear DTOs a entidades de dominio)

---

## ⚙️ Responsabilidades del módulo

- Servir como **adaptador de entrada** (en Arquitectura Hexagonal).
- Manejar llamadas HTTP, validarlas y delegarlas a los servicios del dominio.
- Convertir entre **DTOs** y **entidades del dominio**.
- Gestionar excepciones en endpoints.

---

## 📌 Relación con otros módulos

- ✅ Depende de:
    - `order-domain`: para usar el `OrderApplicationService` (input port).
    - `common-domain`: para objetos comunes como `OrderStatus`, `BaseId`, etc.

- ❌ No contiene lógica de negocio directa.

---

## 🧠 Resumen

Este módulo representa la "cara pública" del servicio de pedidos. Aquí llegan las solicitudes externas, se validan, se convierten y se delegan al dominio. Sigue los principios de Clean Architecture, manteniendo la lógica desacoplada de los frameworks (como Spring).