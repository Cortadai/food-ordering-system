# 🧭 Relación entre módulos Maven y arquitectura hexagonal / limpia

> Este proyecto sigue una arquitectura limpia + hexagonal con separación clara de responsabilidades a través de módulos Maven.

---

## 🧱 ¿Por qué dividir en varios módulos Maven?

Cada módulo representa una **responsabilidad bien definida** dentro del microservicio. Esta división:

- Ayuda a **separar el dominio puro** de los detalles tecnológicos.
- Permite **compilar, testear y mantener de forma aislada** cada componente.
- Aplica el principio de **dependencias dirigidas hacia el dominio**, donde la lógica de negocio no depende de detalles externos.

---

## 🧩 ¿Qué es la arquitectura hexagonal / limpia?

Se basa en dividir la aplicación en **núcleo de dominio + adaptadores externos**, conectados mediante **puertos (ports)**. Es decir:

| Concepto              | Explicación                                                                                      |
|------------------------|--------------------------------------------------------------------------------------------------|
| 🧠 **Dominio**         | Donde vive la lógica de negocio. No depende de nada externo.                                     |
| 🔌 **Puerto (Port)**   | Una **interfaz** que define lo que necesita o expone el dominio (por ejemplo: guardar un pedido).|
| 🔌 **Input Port**      | Interfaz usada por adaptadores de entrada para invocar casos de uso (comandos, queries).         |
| 🔌 **Output Port**     | Interfaz usada por el dominio para interactuar con cosas externas (DB, Kafka, APIs…).            |
| 🔁 **Adaptador (Adapter)** | Implementación de un puerto. Puede ser de entrada (REST Controller) o salida (repositorio JPA, Kafka…). |
| 🎯 **Caso de uso**     | Lógica que orquesta el negocio, definida en los servicios de aplicación.                         |
| 🧱 **Entidades / VO**  | Modelos del dominio (Order, CustomerId, Money…)                                                  |
| 📤 **Publisher**       | Adaptador que publica eventos del dominio a sistemas externos (Kafka, etc.)                      |

---

## 📦 Estructura general del proyecto

| Módulo Maven                     | Descripción                                                                                                                   | Rol en arquitectura limpia      | Tipo de adaptador |
|----------------------------------|-------------------------------------------------------------------------------------------------------------------------------|----------------------------------|-------------------|
| `common-domain`                  | Entidades, objetos de valor, clases base (`BaseEntity`, `AggregateRoot`, `Money`, `BaseId`, etc.) que se **comparten** entre microservicios | Dominio compartido               | N/A               |
| `common-application`            | Contratos y utilidades comunes a nivel de aplicación                                                                          | Utilidades de aplicación         | N/A               |
| `order-domain-core`             | Núcleo del dominio del servicio de pedidos: entidades, objetos de valor, lógica de negocio, eventos                          | Núcleo del dominio               | N/A               |
| `order-application-service`     | Servicios de dominio que coordinan las reglas de negocio, reciben comandos y devuelven eventos                               | Casos de uso (Application Service) | **Input Port**    |
| `order-application`             | Entrada al sistema: servicios REST y validaciones. Recibe peticiones externas y traduce hacia el servicio de dominio         | Adaptador primario               | **Input Adapter** |
| `order-dataaccess`              | Implementación de repositorios, mapeadores JPA, entidades de base de datos (JPA)                                              | Adaptador secundario             | **Output Adapter**|
| `order-messaging`               | Publicación de eventos de dominio a Kafka y consumo de mensajes desde otros servicios                                         | Adaptador secundario             | **Output Adapter**|
| `order-container`               | Módulo ejecutable con configuración Spring Boot, beans, propiedades, arranque de la app                                      | Orquestador final                | N/A               |

---

## 🔁 Flujo de una solicitud

```text
[Cliente HTTP]
      |
      v
order-application (Controller)
      |
      v
order-application-service (caso de uso = Application Service)
      |
      v
order-domain-core (Order, OrderItem, lógica de negocio)
      |
      v
Ports de salida → Repositorios → order-dataaccess
                → Publishers    → order-messaging
```

El `order-container` se encarga de arrancar y conectar todos los componentes con Spring Boot.

---

## 🧠 Explicación de los elementos clave

| Elemento                      | Qué es                                                                                                  | Ejemplo real en el proyecto                    |
|-------------------------------|-----------------------------------------------------------------------------------------------------------|------------------------------------------------|
| **Entidades**                 | Modelos de negocio con identidad y reglas de negocio propias                                              | `Order`, `Product`, `OrderItem`                |
| **Objetos de valor (VO)**     | Clases inmutables que encapsulan un valor con contexto                                                    | `Money`, `StreetAddress`, `OrderId`            |
| **Aggregate Root**            | Entidad principal de un agregado que orquesta sus entidades hijas                                        | `Order`                                        |
| **Evento de dominio**         | Representa algo que ha pasado en el negocio (no técnico)                                                  | `OrderCreatedEvent`, `OrderPaidEvent`          |
| **Input Port**                | Interfaz que define cómo se puede interactuar con el dominio desde fuera                                  | `OrderApplicationService`                      |
| **Output Port**               | Interfaz que define lo que necesita el dominio de capas externas                                          | `OrderRepository`, `OrderCreatedMessagePublisher` |
| **Adaptador de entrada**      | Implementa un input port, suele ser un Controller o un Listener                                          | `OrderController`                              |
| **Adaptador de salida**       | Implementa un output port, como un repositorio JPA o publicador Kafka                                     | `OrderRepositoryImpl`, `KafkaPublisher`        |
| **Servicio de aplicación**    | Orquesta reglas de negocio, llama entidades, repositorios, publishers                                     | `OrderCreateHelper`, `OrderCommandHandler`     |

---

## 📐 Patrón de eventos y transacciones

El dominio devuelve eventos (como `OrderCreatedEvent`), pero **no los publica directamente**.  
Esto permite:

- Primero guardar el estado (pedido) en base de datos.
- Luego publicar el evento, asegurando consistencia.

Hay dos formas de hacerlo:

1. 📦 Usar **un publisher explícito** en el service de aplicación (`OrderCreatedPaymentRequestMessagePublisher`)
2. 📢 Usar `@TransactionalEventListener` con un `ApplicationEventPublisher`

El curso usa ambos como ejemplo, pero continúa con el primero.

---

## 📦 El `pom.xml` principal

- Tiene `<packaging>pom</packaging>` (no genera código).
- Agrupa todos los submódulos en `<modules>`.
- Contiene versiones compartidas y dependencias comunes (Lombok, Spring, etc.)

---

## ✅ Beneficios de este enfoque

- Separación clara de dominio / infraestructura.
- Base de código **fácil de testear**.
- Independencia tecnológica (podemos cambiar Kafka, base de datos, etc.).
- Reutilización en múltiples microservicios.
- Totalmente alineado con Clean Architecture y Hexagonal Architecture.