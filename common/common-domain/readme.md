# 📦 `common-domain` - Módulo de clases reutilizables del dominio

## 🧭 Propósito

El módulo `common-domain` forma parte del paquete `common` del sistema y contiene **abstracciones y clases reutilizables** del dominio, diseñadas para ser compartidas entre distintos microservicios. Proporciona bloques base para trabajar con entidades, identificadores, objetos de valor, excepciones y eventos de dominio, facilitando una implementación consistente y limpia de DDD (Domain-Driven Design).

---

## 📁 Estructura

```plaintext
common-domain/
├── pom.xml
└── src/
    └── main/
        ├── java/
        │   └── com/food/ordering/system/domain/
        │       ├── entity/
        │       │   ├── AggregateRoot.java
        │       │   └── BaseEntity.java
        │       ├── events/
        │       │   ├── DomainEvent.java
        │       │   └── publisher/DomainEventPublisher.java
        │       ├── exception/
        │       │   └── DomainException.java
        │       └── valueobject/
        │           ├── BaseId.java
        │           ├── CustomerId.java
        │           ├── Money.java
        │           ├── OrderApprovalStatus.java
        │           ├── OrderId.java
        │           ├── OrderStatus.java
        │           ├── PaymentStatus.java
        │           ├── ProductId.java
        │           └── RestaurantId.java
```

---

## 🔍 Componentes principales

### 🧩 Entidades Base

- `BaseEntity<T>`  
  Clase abstracta genérica para cualquier entidad con `id`, y que define `equals` y `hashCode` basados en esa identidad.

- `AggregateRoot<T>`  
  Extiende `BaseEntity`. Sirve como **marcador semántico** para distinguir entidades raíz de agregados (Aggregate Roots).

---

### 🆔 Identificadores tipados (`valueobject`)

Todas las clases que extienden `BaseId<T>`, como `OrderId`, `ProductId`, etc., encapsulan `UUID` o `Long` para evitar errores al intercambiar IDs.

```java
public class OrderId extends BaseId<UUID> { ... }
```

---

### 💵 Objeto de valor `Money`

Clase inmutable que representa dinero con lógica de suma, resta, multiplicación, comparación, y manejo de escala/precisión usando `BigDecimal`.

---

### 📜 Enumeraciones

- `OrderStatus`: `PENDING`, `PAID`, `APPROVED`, `CANCELLING`, `CANCELLED`
- `PaymentStatus`: `COMPLETED`, `CANCELLED`, `FAILED`
- `OrderApprovalStatus`: `APPROVED`, `REJECTED`

---

### ⚠️ Excepciones

- `DomainException`: clase base para lanzar errores del dominio.
  Se extiende en módulos específicos para construir excepciones contextualizadas (`OrderDomainException`, etc.).

---

### 📬 Eventos de Dominio

- `DomainEvent<T>`: interfaz marcador.
- `DomainEventPublisher<T>`: interfaz genérica para publicar eventos de dominio, implementada en capas externas.

---

### 📚 Archivos de Documentación

Este módulo contiene dos archivos Markdown útiles:

- [`aggregates-conceptos.md`](./aggregates-conceptos.md): Explicación de conceptos como Aggregate, Root, Entidad, ValueObject con ejemplos y analogías.
- [`aggregates-implementacion.md`](./aggregates-implementacion.md): Explica la implementación técnica en código Java de los conceptos anteriores.

---

#### ✅ Conclusión

Este módulo es esencial para lograr consistencia y claridad en todos los microservicios. Al encapsular conceptos de DDD reutilizables y sin dependencias de frameworks, **promueve un dominio rico, expresivo y limpio**.