# 🚀 Módulo: `payment-container`

> Este módulo representa el **punto de arranque** del microservicio de pagos (`payment-service`). Se encarga de iniciar Spring Boot, ensamblar beans y cargar la configuración necesaria.

---

## 🧩 Estructura del módulo

```plaintext
payment-container
├── src/main/java
│   └── com.food.ordering.system.payment.service.domain
│       ├── PaymentServiceApplication.java
│       └── BeanConfiguration.java
├── resources
│   ├── application.yml
│   ├── init-schema.sql
│   └── logback-spring.xml
```

---

## 🚀 `PaymentServiceApplication.java`

- Clase principal del microservicio.
- Anotada con `@SpringBootApplication`.
- Escanea y levanta todos los componentes de Spring.

```java
@SpringBootApplication
public class PaymentServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PaymentServiceApplication.class, args);
    }
}
```

---

## ⚙️ `BeanConfiguration.java`

- Clase con `@Configuration` que declara manualmente los `@Bean` necesarios.
- Facilita el acoplamiento entre interfaces (puertos) y sus implementaciones.
- Proporciona los mapeadores, servicios del dominio, publishers, listeners, etc.

---

## 📦 Recursos

- `application.yml`: propiedades de configuración (DB, Kafka, logging, puertos, etc.)
- `init-schema.sql`: crea tablas necesarias para JPA
- `logback-spring.xml`: define formato y niveles de logging

---

## 🔗 Dependencias

Este módulo importa los submódulos:

- `payment-domain`
- `payment-application-service`
- `payment-dataaccess`
- `payment-messaging`
- `common-domain`
- `common-application`

---

## 🧱 Rol en la arquitectura

- Es el **compositor y arrancador final** del microservicio
- Une dominio, infraestructura, mensajería y APIs
- Produce el `.jar` ejecutable para despliegue (Docker, Kubernetes, etc.)

---

Este módulo es esencial para lanzar y configurar el microservicio `payment-service` de forma desacoplada y modular.
