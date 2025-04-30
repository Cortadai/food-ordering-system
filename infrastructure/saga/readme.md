# 🔄 Módulo: `saga`

> Este módulo proporciona componentes reutilizables para implementar el **patrón SAGA** distribuido dentro del sistema de microservicios.

Sigue el enfoque de **coreografía de eventos** y está diseñado para ser usado por microservicios como `order-service`, `payment-service`, `restaurant-service`.

---

## 📦 Componentes principales

### `SagaStep<T>`

- Interfaz genérica para definir un **paso de la SAGA**.
- Métodos típicos:
    - `process(T data)`
    - `rollback(T data)`
- Implementada por cada servicio para orquestar su parte del proceso.

---

### `SagaStatus` (Enum)

Define los estados posibles de una SAGA distribuida:

- `STARTED`
- `PROCESSING`
- `COMPENSATING`
- `COMPENSATED`
- `COMPLETED`
- `FAILED`

Esto permite controlar y trazar el estado de la orquestación entre microservicios.

---

### `SagaConstants`

- Contiene constantes comunes relacionadas con nombres de SAGA o eventos.
- Por ejemplo: `ORDER_SAGA_NAME`, etc.

---

## 🎯 Propósito

Este módulo actúa como una **infraestructura transversal** que permite a los microservicios:

- Implementar SAGA sin duplicar lógica
- Mantener estados de ejecución
- Coordinar acciones distribuidas y compensaciones

---

## ✅ Integración

Usado en:
- `order-application-service` (como coordinador principal)
- `payment-service` y `restaurant-service` (como participantes)
