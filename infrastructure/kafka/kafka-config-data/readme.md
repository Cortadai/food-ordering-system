# 📦 `kafka-config-data`

Este submódulo forma parte de la infraestructura del sistema y contiene la **configuración compartida** necesaria para trabajar con Apache Kafka en los microservicios.

---

## 🧭 Rol en la arquitectura

- Pertenece a la **capa de infraestructura** de la arquitectura hexagonal.
- Es un **módulo de configuración común** utilizado por los componentes que interactúan con Kafka (consumidores y productores).
- No contiene lógica de negocio ni lógica de mensajería. Su función es proporcionar configuración desacoplada y centralizada.

---

## 🧱 Estructura del módulo

### 📁 `com.food.ordering.system.kafka.config`

Contiene clases de configuración anotadas con `@Configuration` de Spring.

#### ✅ `KafkaConfigData`
Clase `@ConfigurationProperties` que mapea la configuración Kafka definida en `application.yml`.

Ejemplo de propiedades mapeadas:

```yaml
kafka-config:
  bootstrap-servers: localhost:9092
  schema-registry-url-key: schema.registry.url
  schema-registry-url: http://localhost:8081
  topic-name: food-ordering-topic
```

En código:

```java
@Configuration
@ConfigurationProperties(prefix = "kafka-config")
public class KafkaConfigData {
    private String bootstrapServers;
    private String schemaRegistryUrlKey;
    private String schemaRegistryUrl;
    private String topicName;
    // Getters y setters
}
```

Esto permite que otros módulos simplemente inyecten esta clase para acceder a la configuración centralizada de Kafka.

---

## 🎯 ¿Quién lo utiliza?

Este módulo es usado como dependencia por:

- `kafka-producer`
- `kafka-consumer`
- Cualquier microservicio que quiera publicar o consumir eventos Kafka
- Servicios que interactúan con el esquema Avro a través de un **Schema Registry**

---

## ✅ Ventajas de este enfoque

| Ventaja | Descripción |
|--------|-------------|
| 🔁 Reutilización | La configuración de Kafka se define una vez y se reutiliza en múltiples lugares. |
| 🛠️ Mantenimiento | Si cambias el servidor de Kafka o los topics, lo haces en un único sitio. |
| 🚫 Aislamiento | Este módulo no conoce nada de los productores o consumidores, solo configura. |
| ☁️ Compatible con Spring Cloud Config | Puede evolucionar fácilmente a configuración externa centralizada.|