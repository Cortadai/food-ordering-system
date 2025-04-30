# ⚙️ kafka-config-data

> Este submódulo contiene las **clases de configuración centralizadas** para Kafka.  
Proporciona una forma tipada y reutilizable de acceder a propiedades definidas en `application.yml` o en un servidor de configuración externo.

---

## 📦 Estructura de paquetes

```text
kafka-config-data
└── com.food.ordering.system.kafka.config.data
    ├── KafkaConfigData.java
    ├── KafkaConsumerConfigData.java
    └── KafkaProducerConfigData.java
```

---

## 📁 Clases principales

### 🔧 `KafkaConfigData`
Contiene propiedades generales comunes a Kafka:
- `bootstrapServers`
- `schemaRegistryUrl`
- `topicName` (o prefijos base)

Mapea propiedades bajo el prefijo:

```yaml
kafka-config:
  bootstrap-servers: localhost:9092
  schema-registry-url: http://localhost:8081
```

---

### 🛠️ `KafkaProducerConfigData`
Propiedades específicas para el productor Kafka:
- `acks`
- `batchSize`
- `lingerMs`
- `keySerializerClass`
- `valueSerializerClass`

Prefijo:
```yaml
kafka-producer-config:
  acks: all
  batch-size: 16384
```

---

### 📥 `KafkaConsumerConfigData`
Propiedades específicas del consumidor Kafka:
- `groupId`
- `autoOffsetReset`
- `keyDeserializerClass`
- `valueDeserializerClass`

Prefijo:
```yaml
kafka-consumer-config:
  group-id: payment-group
  auto-offset-reset: earliest
```

---

## 🧩 Rol en la arquitectura

- Sirve como módulo **compartido** entre `kafka-producer` y `kafka-consumer`
- Proporciona objetos `@ConfigurationProperties` listos para inyección
- Permite que los módulos de mensajería estén **desacoplados del origen de configuración**

---

## 🎯 Ventajas del diseño

| Ventaja | Descripción |
|--------|-------------|
| 🔁 Reutilización | Las propiedades se definen una vez y se usan en múltiples módulos |
| ✅ Tipado fuerte | Se detectan errores en tiempo de compilación |
| ☁️ Configurable externamente | Compatible con Spring Cloud Config |
| 🔐 Centralización | Todas las propiedades Kafka están agrupadas de forma coherente |

---

## ✅ Conclusión

`kafka-config-data` permite mantener la configuración de Kafka limpia, centralizada y desacoplada del código de publicación y consumo.  
Es un módulo clave para escalar microservicios que dependan de mensajería Kafka sin replicar configuración en cada servicio.
