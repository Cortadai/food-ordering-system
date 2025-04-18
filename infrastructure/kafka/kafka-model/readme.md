# 📦 `kafka-model`

Este submódulo contiene los **modelos de datos compartidos** que se usan al publicar o consumir mensajes a través de Kafka. Se utiliza para **garantizar que tanto productores como consumidores trabajen con la misma estructura de datos**, sin necesidad de duplicar clases ni romper el principio DRY.

---

## 🧭 Rol en la arquitectura

- Pertenece a la **capa de infraestructura**.
- **No contiene lógica de negocio ni integración directa con Kafka**.
- Es un módulo **compartido** entre productores y consumidores.
- Representa los **eventos (mensajes)** que viajan entre microservicios.

---

## 📁 Estructura típica

### 📁 `com.food.ordering.system.kafka.order.avro.model`

Contiene modelos generados o escritos a mano que se serializan/deserializan con **Avro**. Algunos ejemplos típicos:

#### ✅ `PaymentRequestAvroModel`

```java
@AvroGenerated
public class PaymentRequestAvroModel extends SpecificRecordBase {
    private String id;
    private String sagaId;
    private String customerId;
    private String orderId;
    private BigDecimal price;
    private String createdAt;
    private PaymentStatus paymentStatus;
    // Getters, setters, builder, etc.
}
```

#### ✅ `RestaurantApprovalRequestAvroModel`

```java
@AvroGenerated
public class RestaurantApprovalRequestAvroModel extends SpecificRecordBase {
    private String id;
    private String sagaId;
    private String restaurantId;
    private String orderId;
    private List<OrderItemAvroModel> items;
    private BigDecimal price;
    private String createdAt;
    private OrderApprovalStatus orderApprovalStatus;
}
```

---

## 💡 ¿Por qué Avro?

- **Formato binario eficiente** → reduce el tamaño del mensaje.
- **Schema Registry**: se puede validar que productor y consumidor están de acuerdo con el esquema.
- Permite **evolución de esquemas** (añadir campos opcionales, etc.)

---

## 📦 ¿Dónde se usa?

Se importa desde:

- `kafka-producer`: para construir los mensajes antes de enviarlos a Kafka.
- `kafka-consumer`: para deserializar correctamente los mensajes recibidos.

---

## 🧘 Ventajas

| Ventaja                     | Descripción |
|-----------------------------|-------------|
| 🔁 Reutilización            | Los mismos modelos se comparten en todos los microservicios. |
| 🔒 Tipado fuerte            | Evita errores por diferencias de estructura entre servicios. |
| 📉 Eficiencia               | Uso de Avro reduce el tamaño de los mensajes. |
| 🧪 Mantenibilidad del esquema | Se puede validar y evolucionar con Schema Registry. |