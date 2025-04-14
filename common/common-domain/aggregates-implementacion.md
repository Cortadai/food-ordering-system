# 💡 Agregados: implementación técnica

[🔧 Ver conceptos clave](./aggregates-conceptos.md)

## Tabla de Contenidos

- [Primera Parte: `BaseEntity` y `AggregateRoot`](#primera-parte-baseentity-y-aggregateroot)
  - [`BaseEntity<T>`](#baseentityt)
  - [`AggregateRoot<T>`](#aggregateroott)
- [Segunda Parte: `BaseId<T>` y Value Objects](#segunda-parte-baseidt-y-value-objects)
  - [`BaseId<T>`](#baseidt)
  - [`Money`](#money)
  - [`StreetAddress`](#streetaddress)
  - [`OrderStatus` (Enum)](#orderstatus-enum)
  - [`DomainEvent<T>`](#domaineventt)
- [¿Qué ganamos con este diseño?](#qué-ganamos-con-este-diseño)

---

### <a id="primera-parte-baseentity-y-aggregateroot"></a>✅ PRIMERA PARTE: `BaseEntity`, `AggregateRoot` (entidades y agregados comunes)

#### 📦 Se crea el módulo Maven `common-domain`
- Es un submódulo de `common`
- Contiene clases que pueden usarse desde varios microservicios:
  - `BaseEntity`, `AggregateRoot`, `BaseId`, `DomainEvent`, etc.
  - Objetos de valor reutilizables como `Money` o `OrderId`

---

### <a id="baseentityt"></a>🧩 `BaseEntity<T>`

Es una **clase abstracta genérica** que representa una entidad con identidad (`id`).

```java
public abstract class BaseEntity<T> {
    private T id;

    public T getId() { return id; }
    public void setId(T id) { this.id = id; }

    @Override
    public boolean equals(Object o) {
        // Igualdad basada en el id
    }

    @Override
    public int hashCode() {
        // HashCode basado en el id
    }
}
```

📌 Las entidades como `Order`, `OrderItem`, `Restaurant` extienden de esta clase.

---

### <a id="aggregateroott"></a>🧩 `AggregateRoot<T>`

Extiende `BaseEntity<T>`. No añade comportamiento nuevo, pero **marca claramente** qué entidad es la raíz del agregado.

```java
public abstract class AggregateRoot<T> extends BaseEntity<T> {
    // Marca semántica
}
```

Permite saber rápidamente que esa clase debe mantener las invariantes y que desde fuera no deben modificarse las entidades hijas directamente.

---

### <a id="segunda-parte-baseidt-y-value-objects"></a>✅ SEGUNDA PARTE: `BaseId<T>` y objetos de valor

#### <a id="baseidt"></a>🔐 `BaseId<T>` → clase genérica para identificadores tipados

```java
public abstract class BaseId<T> {
    private final T value;

    protected BaseId(T value) {
        this.value = value;
    }

    public T getValue() { return value; }

    @Override
    public boolean equals(Object o) { /* usa value */ }
    @Override
    public int hashCode() { /* usa value */ }
}
```

Y se crean clases específicas:

```java
public class OrderId extends BaseId<UUID> {
    public OrderId(UUID value) { super(value); }
}
```

✅ Más legible, más seguro, evita errores con identificadores genéricos sin contexto.

---

### <a id="money"></a>💰 `Money` → Objeto de valor inmutable para representar dinero

```java
public class Money {
    private final BigDecimal amount;

    public Money(BigDecimal amount) {
        this.amount = setScale(amount);
    }

    public Money add(Money other) { ... }
    public Money subtract(Money other) { ... }
    public Money multiply(int multiplier) { ... }

    public boolean isGreaterThanZero() { ... }
    public boolean isGreaterThan(Money other) { ... }

    private BigDecimal setScale(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_EVEN);
    }

    public static final Money ZERO = new Money(BigDecimal.ZERO);
}
```

✅ Inmutable  
✅ Encapsula lógica de negocio monetaria  
✅ Maneja escala y redondeo correctamente

---

### <a id="streetaddress"></a>📦 `StreetAddress` → Objeto de valor para dirección

```java
public class StreetAddress {
    private final UUID id;
    private final String street;
    private final String postalCode;
    private final String city;
    // constructor, getters, equals, hashCode (sin usar id)
}
```

> El `id` es solo para persistencia relacional, no participa en la igualdad del objeto.

---

### <a id="orderstatus-enum"></a>🎯 `OrderStatus` como Enum

```java
public enum OrderStatus {
    PENDING, PAID, APPROVED, CANCELLING, CANCELLED
}
```

---

### <a id="domaineventt"></a>🧾 `DomainEvent<T>` (Interfaz genérica para eventos de dominio)

```java
public interface DomainEvent<T> {
    // marcador semántico
}
```

Las clases como `OrderCreatedEvent`, `OrderPaidEvent`, etc., implementarán esta interfaz.

---

### <a id="qué-ganamos-con-este-diseño"></a>🧠 ¿Qué ganamos con este diseño?

1. ✅ Reutilización completa y coherente entre microservicios.
2. ✅ Claridad: agregados bien delimitados, identidades fuertes, lógica encapsulada.
3. ✅ Código fácil de leer, probar y extender.
4. ✅ Separación total entre lógica del dominio y tecnologías/frameworks externos.
5. ✅ Aplicación real de DDD con Clean Architecture.

