# 🧭 Módulo principal: `order-service`

> Este módulo representa el microservicio completo de pedidos, compuesto por varios submódulos Maven alineados con los principios de **Arquitectura Hexagonal + Clean Architecture + SAGA Pattern**.

---

## 🧱 ¿Por qué dividir en varios módulos Maven?

Cada módulo representa una **responsabilidad específica** y desacoplada dentro del microservicio. Esta estructura:

- Permite compilar, testear y evolucionar cada módulo por separado.
- Favorece el principio de **dependencias dirigidas hacia el dominio**.
- Facilita la reutilización y el cumplimiento de **DDD (Domain-Driven Design)**.

---

## 🧩 ¿Qué es la arquitectura hexagonal / limpia?

Se basa en dividir el sistema en **núcleo de dominio + adaptadores externos**, conectados mediante **puertos (ports)**. Cada capa tiene reglas claras:

| Concepto                  | Explicación                                                                                      |
|---------------------------|--------------------------------------------------------------------------------------------------|
| 🧠 **Dominio**            | Lógica de negocio independiente de detalles técnicos.                                            |
| 🔌 **Puerto (Port)**      | Interfaz que define una necesidad o capacidad del dominio.                                       |
| 🔌 **Input Port**         | Cómo el dominio puede ser invocado (casos de uso, comandos).                                     |
| 🔌 **Output Port**        | Cómo el dominio interactúa con infraestructura (repositorios, mensajería).                      |
| 🔁 **Adaptador**          | Implementación concreta de un puerto (REST, Kafka, JPA...).                                      |
| 🎯 **Caso de uso**        | Lógica orquestada que interactúa con el dominio y sus dependencias.                             |
| 🧱 **Entidades / VO**     | Modelos del dominio con reglas internas.                                                         |
| 📤 **Publisher**          | Adaptador que publica eventos del dominio hacia otros sistemas.                                 |

---

## 📦 Estructura general del proyecto

| Módulo Maven              | Descripción                                                                                       | Rol en arquitectura limpia      | Tipo de adaptador |
|---------------------------|---------------------------------------------------------------------------------------------------|----------------------------------|-------------------|
| `common-domain`           | Entidades y objetos de valor reutilizables entre microservicios                                   | Dominio compartido               | N/A               |
| `common-application`      | Contratos y utilidades compartidas entre servicios                                                | Utilidades de aplicación         | N/A               |
| `order-domain-core`       | Entidades, lógica de negocio, eventos del dominio puro                                            | Núcleo del dominio               | N/A               |
| `order-application-service`| Servicios de aplicación, orquestación, lógica de negocio, coordinación de SAGA                   | Application Service              | **Input Port**    |
| `order-application`       | Controladores REST y validación, entrada al sistema                                                | Adaptador primario               | **Input Adapter** |
| `order-dataaccess`        | Implementación de repositorios, mapeadores, JPA, Outbox                                           | Adaptador secundario             | **Output Adapter**|
| `order-messaging`         | Publicación y consumo de eventos Kafka con Avro                                                   | Adaptador secundario             | **Output Adapter**|
| `order-container`         | Módulo ejecutable con `@SpringBootApplication`, configuración, ensamblaje de beans               | Composición final                | N/A               |

---

## 🔁 Flujo de una solicitud

```text
[Cliente HTTP]
      |
      v
order-application (OrderController)
      |
      v
order-application-service (caso de uso / SAGA)
      |
      v
order-domain-core (entidades y lógica de negocio)
      |
      v
Output Ports
    ├──> Repositorios → order-dataaccess
    └──> Eventos Outbox → order-messaging
```

---

## 🧠 Elementos clave

| Elemento                      | Qué es                                                                                                | Ejemplo en el proyecto                          |
|-------------------------------|---------------------------------------------------------------------------------------------------------|--------------------------------------------------|
| **Entidades**                 | Modelos con identidad y reglas propias                                                                 | `Order`, `OrderItem`                            |
| **Value Objects (VO)**        | Clases inmutables que encapsulan valores con significado                                               | `Money`, `OrderId`, `StreetAddress`             |
| **Aggregate Root**            | Entidad principal de un agregado                                                                       | `Order`                                          |
| **Domain Event**              | Evento de negocio que describe algo que ocurrió                                                         | `OrderCreatedEvent`, `OrderCancelledEvent`       |
| **Input Port**                | Interfaz que define cómo invocar al dominio desde fuera                                                 | `OrderApplicationService`                        |
| **Output Port**               | Interfaz que define lo que necesita el dominio de infraestructura externa                              | `OrderRepository`, `PaymentRequestMessagePublisher` |
| **Input Adapter**             | Implementación de un Input Port (REST Controller, Kafka Listener)                                      | `OrderController`, `PaymentResponseKafkaListener`|
| **Output Adapter**            | Implementación de un Output Port (JPA, Kafka Publisher)                                                 | `OrderRepositoryImpl`, `OrderPaymentEventKafkaPublisher` |
| **Application Service**       | Orquesta entidades, repositorios, y publishers. Coordina reglas y lógica                               | `OrderCreateCommandHandler`, `OrderPaymentSaga` |

---

## 🔄 Patrón SAGA + Outbox

Este microservicio usa el patrón **SAGA** para coordinar operaciones distribuidas, y el patrón **Transactional Outbox** para publicar eventos de forma fiable.

- Se usan **entidades Outbox** (`PaymentOutboxEntity`, `ApprovalOutboxEntity`)
- El dominio genera un evento (`OrderCreatedEvent`) y lo pasa al servicio de aplicación
- El evento se **almacena en la outbox**, y luego es publicado por un scheduler externo a Kafka

---

## 📤 Kafka Messaging

El módulo `order-messaging`:

- Escucha eventos desde `payment-service` y `restaurant-service` usando `@KafkaListener`
- Publica eventos del dominio (`OrderCreatedEvent`, etc.) hacia otros servicios usando Kafka
- Utiliza modelos Avro serializados generados desde `.avsc`

---

## 🪛 Composición y ejecución

El módulo `order-container`:

- Contiene `OrderServiceApplication` con `@SpringBootApplication`
- Define `BeanConfiguration` con los ensamblajes manuales de beans
- Incluye `application.yml`, `init-schema.sql`, `logback-spring.xml`
- Es el que se empaqueta como **JAR ejecutable**

---

## ✅ Beneficios del enfoque

- Separación de responsabilidades clara
- Independencia tecnológica (DB, Kafka, Avro, REST...)
- Testeo fácil del dominio sin necesidad de infraestructura
- Escalabilidad y mantenibilidad
- Compatible con microservicios, despliegues Docker/Kubernetes, y CI/CD

---

Este proyecto ejemplifica cómo aplicar **Arquitectura Hexagonal**, **DDD**, **SAGA** y **Clean Architecture** en un entorno real de microservicios Java.
