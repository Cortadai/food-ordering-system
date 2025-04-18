# 📦 `kafka-producer`

Este submódulo implementa la **publicación de eventos** a Kafka. Forma parte de la **infraestructura de salida** de la arquitectura hexagonal, y es un **adaptador de salida (output adapter)** que implementa los **puertos de publicación de eventos** definidos en los módulos de dominio.

---

## 🧭 Rol en la arquitectura

- Pertenece a la **capa de infraestructura**.
- **Implementa los ports de salida** definidos en el dominio (`DomainEventPublisher<T>`).
- Utiliza el cliente de Kafka de Spring para publicar mensajes con configuración desacoplada.

---

## 🧱 Estructura del módulo

### 📁 `com.food.ordering.system.kafka.producer.service`

Contiene la implementación concreta del publicador de eventos Kafka.

#### ✅ `KafkaProducer<K, V>`
Clase genérica que encapsula la lógica de publicación de eventos en Kafka.

```java
@Component
public class KafkaProducer<K, V> {
    private final KafkaTemplate<K, V> kafkaTemplate;

    public void send(String topicName, K key, V message) {
        kafkaTemplate.send(topicName, key, message);
        // Puede incluir lógica de logging o trazabilidad
    }
}
```

> ✅ Esta clase se comporta como una **utilidad reutilizable** por cualquier publisher específico.

---

### 📁 Ejemplo de implementación de publisher

En módulos como `order-messaging`, se implementa:

```java
@Component
public class OrderCreatedKafkaMessagePublisher
        implements OrderCreatedPaymentRequestMessagePublisher {

    private final KafkaProducer<String, AvroModel> kafkaProducer;

    @Override
    public void publish(OrderCreatedEvent event) {
        String key = event.getOrder().getId().toString();
        AvroModel message = mapper.mapToAvro(event);
        kafkaProducer.send("order-topic", key, message);
    }
}
```

---

## 🔧 ¿Cómo se configura?

- Usa el módulo `kafka-config-data` para obtener los datos de conexión y configuración de topics.
- Define beans como `KafkaTemplate` o `ProducerFactory` mediante la auto configuración de Spring Boot Kafka o archivos `@Configuration` si es necesario.

---

## 🎯 Ventajas de este enfoque

| Ventaja | Descripción |
|--------|-------------|
| ✅ Desacoplamiento | El dominio no conoce Kafka ni su implementación concreta. |
| ♻️ Reutilización | `KafkaProducer<K, V>` es genérica y puede usarse para muchos tipos de eventos. |
| 📦 Extensión simple | Solo necesitas implementar un publisher que use esta clase para publicar nuevos tipos de eventos. |
| 🧪 Testeabilidad | Puedes sustituir `KafkaProducer` por un mock en tests sin tocar el dominio. |