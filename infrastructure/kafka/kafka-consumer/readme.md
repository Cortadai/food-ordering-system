# 📥 kafka-consumer

> Este submódulo actúa como **adaptador de entrada** que permite recibir eventos desde Kafka.  
Está diseñado como un componente genérico reutilizable que transforma mensajes Kafka en eventos del dominio, sin acoplarse a la lógica de negocio.

---

## 📦 Estructura de paquetes

```text
kafka-consumer
└── com.food.ordering.system.kafka.consumer
    ├── KafkaConsumer.java
    └── config
        └── KafkaConsumerConfig.java
```

---

## 🧱 Componentes principales

### 🔁 `KafkaConsumer<K, V>`
Clase genérica que centraliza la lógica de recepción de mensajes:
- Utiliza `@KafkaListener` para escuchar topics configurados
- Encapsula el procesamiento de mensajes mediante un `Consumer<K, V>` funcional
- Facilita trazabilidad, logging y manejo de errores

---

### ⚙️ `KafkaConsumerConfig`
Clase de configuración que:
- Registra los beans necesarios para consumir desde Kafka (`ConsumerFactory`, `ConcurrentKafkaListenerContainerFactory`)
- Utiliza las propiedades inyectadas desde `KafkaConfigData`
- Define estrategias de deserialización y políticas de retry

---

## 🔁 Flujo típico: Consumo de evento

1. Kafka publica un mensaje en un topic (p. ej. `payment-response-topic`)
2. `KafkaConsumer` lo recibe mediante `@KafkaListener`
3. El mensaje Avro es deserializado y transformado a DTO o evento del dominio
4. Se invoca un handler o listener de aplicación (ej. `PaymentResponseMessageListener`)
5. El servicio de dominio procesa el evento

---

## 🧠 Ventajas del diseño

| Ventaja | Descripción |
|--------|-------------|
| 🔌 Extensible | Cualquier evento puede ser consumido registrando un nuevo `@KafkaListener`. |
| 🧩 Reutilizable | La clase genérica `KafkaConsumer` puede adaptarse a múltiples tipos de mensaje. |
| 💡 Observabilidad | Fácil de instrumentar con logs, trazas y métricas. |
| 🧪 Testeable | Puede probarse de forma aislada usando datos simulados. |

---

## 📚 Dependencias requeridas

- Spring Kafka
- kafka-config-data (para centralizar configuración)
- Avro (para los modelos de datos serializados)

---

## ✅ Conclusión

`kafka-consumer` representa una capa de entrada desacoplada, preparada para recibir y transformar eventos Kafka sin introducir dependencias con la lógica de negocio.  
Este diseño respeta los principios de la arquitectura hexagonal, permitiendo evolucionar el sistema sin romper integraciones existentes.
