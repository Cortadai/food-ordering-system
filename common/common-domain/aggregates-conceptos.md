# 💡 Agregados: Conceptos clave

[🔧 Ver implementación técnica](./aggregates-implementacion.md)

## Tabla de Contenidos

- [¿Qué es un Aggregate?](#qué-es-un-aggregate)
- [¿Qué es un Aggregate Root?](#qué-es-un-aggregate-root)
- [¿Qué es una Entidad?](#qué-es-una-entidad)
- [¿Qué es un Value Object?](#qué-es-un-value-object)
- [Ejemplo para un niño de 5 años](#ejemplo-para-un-niño-de-5-años)
- [Traducción al mundo real](#traducción-al-mundo-real)

---

### <a id="qué-es-un-aggregate"></a>💡 ¿Qué es un **Aggregate**?

Un **Aggregate** (o Agregado) es un **grupo de objetos del dominio** que **trabajan juntos como una unidad coherente** para mantener la consistencia de los datos y reglas del negocio.

- Se compone de una **Aggregate Root**, una o más **entidades** y algunos **Value Objects**.
- Es **una frontera de consistencia**: todo lo que pasa dentro del agregado debe cumplir sus reglas internas antes de dejarlo.
- Solo se accede desde el exterior **a través de su raíz**.

---

### <a id="qué-es-un-aggregate-root"></a>👑 ¿Qué es un **Aggregate Root**?

Es la **entidad principal** de un agregado. Se encarga de:

- Mantener el **estado global del agregado**.
- Validar y aplicar las **reglas del negocio**.
- Coordinar las demás entidades internas.
- Garantizar la **integridad y coherencia** de todo el conjunto.

> Solo puedes modificar el agregado si lo haces a través de su raíz.

---

### <a id="qué-es-una-entidad"></a>🧱 ¿Qué es una **Entidad**?

Una **entidad** representa un objeto del dominio que:

- Tiene una **identidad única** (`UUID`, `Long`, etc.)
- Su **estado puede cambiar con el tiempo**.
- Su **identidad lo distingue**, no sus atributos.

Ejemplo: un `OrderItem` puede tener diferente cantidad o precio, pero sigue siendo el mismo si tiene el mismo `OrderItemId`.

---

### <a id="qué-es-un-value-object"></a>📦 ¿Qué es un **Value Object**?

Un **objeto de valor** es un tipo de objeto del dominio que:

- **No tiene identidad** propia.
- Se define por sus **atributos**.
- Es **inmutable**.
- Encapsula **valor con significado**.

Ejemplos típicos:
- `Money`: para representar valores monetarios.
- `StreetAddress`: para direcciones postales.
- `OrderId`, `CustomerId`: IDs tipados seguros.

---

### <a id="ejemplo-para-un-niño-de-5-años"></a>🧒🎲 Ejemplo para un niño de 5 años

Imagina que tienes una **caja de LEGO** que representa un **pedido de comida** 🍔:

- Esa **caja de LEGO es el Agregado**: todo lo que hay dentro forma parte del pedido.
- Dentro hay:
  - Una **hoja con instrucciones**: eso es `Order`, la **Aggregate Root** 🧱.
  - Las piezas sueltas como la hamburguesa, bebida, patatas: son **entidades**, como `OrderItem`.
  - La etiqueta con la dirección: es un **Value Object**, como `StreetAddress`.

Cuando tu mamá pregunta por el pedido, **no abre la caja para mirar las piezas una a una**. Ella mira la **hoja de instrucciones (Order)** que le dice: “Este pedido tiene una hamburguesa, una bebida y va a tal dirección”. Esa hoja es la **única forma válida de ver y modificar el pedido**. Esa es la **Aggregate Root**.

---

### <a id="traducción-al-mundo-real"></a>🎯 Traducción al mundo real

En el dominio de pedidos de comida:

- `Order` → **Aggregate Root**
- `OrderItem` → **Entidad**
- `Product`, `Money`, `StreetAddress` → **Value Objects**
- El **Aggregate** es: `Order` + sus `OrderItems` + los `ValueObjects` que necesita