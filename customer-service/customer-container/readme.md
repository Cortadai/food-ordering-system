# 🚀 Módulo: `customer-container`

> Este módulo representa el **punto de entrada** del microservicio de clientes (`customer-service`). Se encarga de iniciar la aplicación Spring Boot y ensamblar todos los componentes del sistema.

---

## 🧩 Estructura del módulo

```plaintext
customer-container
├── src/main/java
│   └── com.food.ordering.system.customer.service
│       ├── CustomerServiceApplication.java
│       └── BeanConfiguration.java
├── resources
│   ├── application.yml
│   ├── init-schema.sql
│   └── logback-spring.xml
```

---

## 🚀 `CustomerServiceApplication.java`

- Punto de entrada del microservicio
- Anotado con `@SpringBootApplication`
- Arranca Spring Boot y registra todos los beans

---

## ⚙️ `BeanConfiguration.java`

- Clase de configuración con `@Configuration`
- Declara manualmente `@Bean` para:
    - Mapeadores
    - Servicios de dominio
    - Publicadores de eventos
    - Repositorios

---

## 📦 Recursos

- `application.yml`: configuración de base de datos, Kafka, puerto, logs, etc.
- `init-schema.sql`: script SQL de inicialización.
- `logback-spring.xml`: configuración de logs.

---

## 🔗 Dependencias

Este módulo importa:

- `customer-domain`
- `customer-application-service`
- `customer-dataaccess`
- `customer-messaging`
- `common-domain`
- `common-application`

---

## ✅ Propósito

- Empaqueta el microservicio como `.jar` ejecutable
- Une todas las piezas del sistema: dominio, persistencia, eventos, REST
- Listo para despliegue en Docker, Kubernetes, etc.

---

Este módulo es el responsable de **componer, arrancar y ejecutar** el microservicio completo de clientes.
