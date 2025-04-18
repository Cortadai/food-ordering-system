# 📦 `kafka-consumer`

Este submódulo se encarga de **consumir mensajes desde Kafka** y redirigirlos a los componentes del dominio que reaccionan a eventos externos. Es un **adaptador de entrada (input adapter)** en la arquitectura hexagonal.

---

## 🧭 Rol en la arquitectura

- Pertenece a la **capa de infraestructura**.
- **Recibe eventos** desde Kafka y los convierte en objetos del dominio o en DTOs que serán entregados a servicios de aplicación o controladores de eventos (`MessageListener`).
- Actúa como **puente entre Kafka y los puertos de entrada** del dominio.

---

## 🧱 Estructura del módulo

### 📁 `com.food.ordering.system.kafka.consumer`

Contiene implementaciones concretas para los consumidores de eventos.

#### ✅ `KafkaConsumer<K, V>`
Clase genérica que encapsula la lógica de consumo de eventos desde Kafka.

```java
@Component
public class KafkaConsumer<K, V> {
    // Internamente podría usar Spring Kafka Listener (no mostrado aquí)
    public void consume(String topicName, Consumer<K, V> handler) {
        // Lógica de binding con listener
    }
}
```

> Es habitual que los consumidores específicos implementen interfaces como `@KafkaListener`.

---

## 🧭 Flujo de trabajo típico

1. **Kafka produce** un evento (`PaymentCompletedEvent`, por ejemplo).
2. `KafkaConsumer` recibe ese evento.
3. Lo convierte (si es necesario) usando un `DataMapper` desde Avro a DTO.
4. Llama a un **puerto de entrada del dominio**, como `PaymentResponseMessageListener`.
5. El dominio maneja el evento y ejecuta las reglas necesarias (p. ej., marcar un pedido como pagado).

---

### Ejemplo práctico (en otro módulo)

```java
@KafkaListener(topics = "${kafka.payment.response.topic}")
public void receive(@Payload PaymentResponseAvroModel message) {
    PaymentResponse paymentResponse = mapper.toPaymentResponse(message);
    listener.paymentCompleted(paymentResponse);
}
```

---

## 🔧 ¿Cómo se configura?

- Utiliza `kafka-config-data` para recuperar información de topics, grupo de consumidores, etc.
- Anotaciones típicas: `@KafkaListener`, `@EnableKafka`, configuración de `ConsumerFactory`, `ConcurrentKafkaListenerContainerFactory`.

---

## 🎯 Ventajas

| Ventaja | Descripción |
|--------|-------------|
| ✅ Desacoplamiento | El dominio no sabe que los eventos vienen de Kafka. |
| ♻️ Centralización | Se puede reutilizar lógica de deserialización, logging, error handling. |
| 🔄 Integración reactiva | Ideal para patrones como Event Sourcing, Saga o Coreografía basada en eventos. |