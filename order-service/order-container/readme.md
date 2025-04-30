# 📦 Módulo: `order-container`

> Este módulo representa el **componente principal de arranque** del microservicio `order-service`.

> Aquí se integran todos los submódulos (`application`, `domain`, `dataaccess`, `messaging`) y se configuran los beans necesarios para que la aplicación funcione mediante Spring Boot.

---

## 🧩 Estructura del módulo

```plaintext
order-container
├── src/main/java
│   └── com.food.ordering.system.order.service
│       ├── OrderServiceApplication.java
│       └── BeanConfiguration.java
├── resources
│   ├── application.yml
│   ├── init-schema.sql
│   └── logback-spring.xml
```

---

## 🚀 `OrderServiceApplication.java`

- Es la **clase principal de arranque** del microservicio.
- Anotada con `@SpringBootApplication`:
  - Activa autoconfiguración
  - Inicia el escaneo de componentes
  - Ejecuta el microservicio

```java
@SpringBootApplication
public class OrderServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}
```

---

## 🧰 `BeanConfiguration.java`

- Clase anotada con `@Configuration`
- Declara manualmente algunos `@Bean` como:
  - Mapeadores de datos
  - Servicios de dominio
  - Publicadores o listeners personalizados
- Actúa como **punto central de ensamblaje de dependencias** que no se autoconfiguran

---

## ⚙️ `application.yml`

- Archivo principal de configuración del microservicio
- Contiene:
  - Configuración de base de datos
  - Brokers de Kafka
  - Puertos y contexto de servidor
  - Logs y perfiles
- Separa la configuración externa del código fuente

---

## 🪛 Otros recursos

- `init-schema.sql`: crea las tablas necesarias al arrancar (usado con Spring Boot + JPA/Hibernate)
- `logback-spring.xml`: define el formato y niveles de logs

---

## 🔗 Dependencias

Este módulo **declara como dependencias** los siguientes submódulos:

- `order-application`
- `order-domain`
- `order-dataaccess`
- `order-messaging`
- `common-application`
- `common-domain`

---

## 📦 Empaquetado y despliegue

- Este módulo es el que se **empaqueta como JAR ejecutable** (`spring-boot:repackage`)
- Se usa en producción para ejecutar el microservicio real
- Puede integrarse con Docker, Kubernetes o cualquier orquestador

---

## 🧠 Rol dentro de la arquitectura

- Actúa como **adaptador principal del sistema**
- Es la “bota de arranque” de Spring y responsable de cargar toda la infraestructura
- Orquesta y une el dominio, la lógica de aplicación y los mecanismos de infraestructura
- No contiene lógica de negocio

---

Este módulo es fundamental para ejecutar correctamente el microservicio, pero su responsabilidad es puramente de **composición y configuración**.
