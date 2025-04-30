# 🚀 kafka-producer

> Este submódulo implementa el **adaptador de salida** responsable de publicar eventos en Kafka.  
Se conecta con el `KafkaTemplate` de Spring y aplica buenas prácticas de desacoplamiento y reutilización para facilitar la emisión de mensajes desde los microservicios.

---

## 📦 Estructura de paquetes

```text
kafka-producer
└── com.food.ordering.system.kafka.producer
    ├── service
    │   └── impl
    ├── exception
    └── KafkaMessageHelper.java
    └── KafkaProducerConfig.java
```

---

## 🧱 Componentes principales

### ⚙️ `KafkaProducerConfig`
Define la configuración de Kafka para productores:
- Bean de `KafkaTemplate<K, V>`
- Propiedades del productor (serializers, retries, etc.)
- Integración con `KafkaConfigData` para centralizar la configuración

---

### 📤 `KafkaProducer<K, V>`
Interfaz genérica que define cómo se debe enviar un mensaje a Kafka:
```java
void send(String topic, K key, V message);
```
Se utiliza para abstraer la lógica de publicación, permitiendo mocks y testabilidad.

---

### 🧪 `KafkaProducerImpl<K, V>`
Implementación concreta que:
- Usa `KafkaTemplate.send(...)` para publicar
- Aplica callbacks para éxito o error
- Registra eventos para trazabilidad

```java
kafkaTemplate.send(topic, key, value)
             .addCallback(successCallback, failureCallback);
```

---

### 🧰 `KafkaMessageHelper`
Clase utilitaria para:
- Crear `ProducerRecord` con cabeceras
- Enriquecer mensajes con metadatos si es necesario

---

### ❗ `KafkaProducerException`
Excepción específica para errores en la publicación de eventos Kafka.

---

## 🔁 Flujo típico: Publicar evento

1. Un servicio del dominio (por ejemplo, `OrderCreatedEventPublisher`) construye un mensaje.
2. Se transforma en modelo Avro con un `DataMapper`.
3. Se invoca `KafkaProducer.send(topic, key, message)`.
4. El mensaje es enviado al topic mediante `KafkaTemplate`.
5. El callback informa del éxito o error del envío.

---

## 🧠 Ventajas del diseño

| Ventaja | Descripción |
|--------|-------------|
| 🔄 Reutilizable | `KafkaProducer<K,V>` puede usarse para múltiples eventos o servicios. |
| ✅ Desacoplado | El dominio no depende de Kafka ni de Spring. Solo usa puertos. |
| 🧪 Testeable | Fácil de simular en tests. Se puede mockear sin necesidad de Kafka real. |
| 📊 Observabilidad | Usa callbacks y logs para rastrear éxito o fallo en la publicación. |

---

## 📚 Dependencias requeridas

- Spring Kafka
- kafka-config-data (para inyección de propiedades)
- Avro (para los modelos serializados)

---

## ✅ Conclusión

`kafka-producer` proporciona una solución limpia, genérica y reutilizable para la publicación de eventos en Kafka, alineada con la arquitectura hexagonal del sistema.  
Permite mantener la lógica de negocio desacoplada del broker de eventos, asegurando que los cambios en Kafka no afecten a los microservicios de dominio.
