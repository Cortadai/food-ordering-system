A continuación vemos cómo encajan los **módulos de Maven** con la **arquitectura hexagonal / limpia**.

---

### 🧱 ¿Por qué dividir en varios módulos Maven?

La división en múltiples módulos tiene como objetivo **reflejar la separación de responsabilidades** y **mantener el aislamiento de capas** que propone la arquitectura limpia. Cada módulo es una capa o componente independiente y puede compilarse y probarse por separado.

---

## 🧭 Relación entre módulos y capas de arquitectura

**mapa mental de cómo se estructuran**:

| Módulo Maven                     | Rol en la arquitectura hexagonal / limpia                                                                                     | Tipo de adaptador |
|----------------------------------|-------------------------------------------------------------------------------------------------------------------------------|-------------------|
| `order-domain-core`              | Contiene entidades, objetos de valor y lógica de dominio pura                                                                 | Ninguno (núcleo)  |
| `order-application-service`      | Contiene los **servicios de aplicación**: orquestan el uso del dominio                                                        | Input Port        |
| `order-dataaccess`               | Implementa los repositorios, acceso a base de datos                                                                           | Output Port       |
| `order-messaging`                | Implementa publicación y consumo de eventos (Kafka)                                                                           | Output Port       |
| `order-application`              | La API REST (controladores), entrada del sistema                                                                              | Primary Adapter   |
| `order-container`                | Módulo ejecutable, contiene la configuración Spring Boot, arranca el microservicio con todas sus dependencias (otros módulos) | N/A               |

---

### 🧩 Cómo encajan estos módulos

Imaginemos que recibimos una petición HTTP de creación de pedido:

1. `order-application` (API REST) recibe la solicitud ➝
2. Llama al servicio de aplicación en `order-application-service` ➝
3. El servicio de aplicación usa entidades del `order-domain-core` y ejecuta reglas de negocio ➝
4. Cuando necesita guardar algo, llama a una **interfaz de repositorio** definida en el core ➝
5. `order-dataaccess` implementa esa interfaz y accede a la base de datos ➝
6. Si hay que emitir un evento, se llama a un publisher definido en el core ➝
7. `order-messaging` implementa ese publisher y publica en Kafka.

➡ Todo esto lo orquesta `order-container`, que contiene la configuración de Spring Boot.

---

### 🔁 ¿Qué ventajas tiene esta modularización?

- Separación clara de responsabilidades.
- Podemos **testear el dominio sin base de datos ni Kafka.**
- Podemos reemplazar Kafka o JPA sin tocar la lógica de negocio.
- Podemos tener una **estructura escalable**: por ejemplo, replicar esta división para los otros microservicios (pagos, restaurante, cliente…).

---

### 📦 El `pom.xml` principal

Este POM principal actúa como **agregador**. Su tipo de empaquetado es `pom` (no genera un JAR), y contiene en `<modules>` todos los submódulos (`order-service`, `order-domain-core`, etc.).