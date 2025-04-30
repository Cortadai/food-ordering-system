# 📦 kafka-model

> Este submódulo contiene los **modelos de datos serializados en Avro** utilizados en la comunicación entre microservicios vía Kafka.  
Define los eventos que viajan entre servicios, asegurando compatibilidad, tipado fuerte y eficiencia de red.

---

## 📦 Estructura de paquetes

```text
kafka-model
└── com.food.ordering.system.kafka.order.avro.model
    ├── PaymentRequestAvroModel
    ├── PaymentResponseAvroModel
    ├── RestaurantApprovalRequestAvroModel
    ├── RestaurantApprovalResponseAvroModel
    ├── CustomerAvroModel
    └── Enums y objetos auxiliares
```

---

## 🧱 Propósito del módulo

- Facilitar la **reutilización de estructuras de eventos** Kafka entre productores y consumidores
- Asegurar que ambos extremos entiendan el mismo esquema de datos
- **Evitar duplicación de clases** o pérdida de coherencia entre servicios
- Permitir evolución segura de esquemas usando **Schema Registry**

---

## 🔄 ¿Por qué Avro?

Apache Avro permite:
- ✅ Serialización compacta en formato binario
- ✅ Evolución de esquemas (añadir campos sin romper compatibilidad)
- ✅ Integración con Kafka y Spring para validación automática

---

## 📚 Ejemplos de modelos

### 💸 `PaymentRequestAvroModel`
Contiene los datos necesarios para iniciar un proceso de pago:
- `orderId`, `customerId`, `price`, `createdAt`, `paymentStatus`

### ✅ `RestaurantApprovalResponseAvroModel`
Respuesta del restaurante tras evaluar un pedido:
- `restaurantId`, `orderId`, `status`, `items`, `price`, `createdAt`

### 📊 Enums comunes
- `PaymentStatus`
- `OrderApprovalStatus`
- `RestaurantOrderStatus`

---

## 🎯 Uso del módulo

Este módulo es una **dependencia directa de**:
- `kafka-producer`: para construir mensajes Kafka
- `kafka-consumer`: para deserializar eventos entrantes

Se integra automáticamente con el esquema registrado en **Confluent Schema Registry** mediante Spring Kafka y Avro.

---

## 🧠 Ventajas del diseño

| Ventaja | Descripción |
|--------|-------------|
| 🔄 Consistencia | Todos los servicios usan los mismos modelos de eventos |
| 📉 Eficiencia | El uso de Avro reduce el tamaño del mensaje |
| 🧪 Validación | El esquema puede verificarse al producir y consumir |
| ☁️ Evolutivo | Soporta versiones de esquema sin romper contratos |

---

## ✅ Conclusión

`kafka-model` actúa como el contrato compartido entre microservicios en el ecosistema Kafka.  
Gracias al uso de Avro, proporciona una base estable, eficiente y evolutiva para la mensajería distribuida.
