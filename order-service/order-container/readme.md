# 📦 Módulo: `order-container`

> El módulo `order-container` actúa como **composición final** del microservicio de pedidos. Aquí es donde se integran todos los módulos del contexto `order` (`application`, `domain`, `dataaccess`, `messaging`) y se configura el arranque de la aplicación mediante Spring Boot.

---

## 🧩 Estructura del módulo

```plaintext
order-container
├── src/main/java
│   └── com.food.ordering.system.order.service
│       └── OrderServiceApplication.java
├── resources
│   └── application.yml
```

---

## 🚀 1. `OrderServiceApplication.java`

Es la **clase principal de arranque** (`@SpringBootApplication`):

```java
@SpringBootApplication
public class OrderServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}
```

- Marca el **entry point** del microservicio.
- Usa `@SpringBootApplication`, que engloba:
    - `@Configuration`
    - `@EnableAutoConfiguration`
    - `@ComponentScan` → escanea todo lo necesario.

---

## ⚙️ 2. `application.yml`

Archivo de configuración de Spring Boot:

- Contendrá propiedades de conexión a base de datos, Kafka, logs, puertos, etc.
- Aquí se definen los **valores externos** que necesita el servicio en tiempo de ejecución.

---

## 🔗 Dependencias

El `order-container` importa como dependencias a los submódulos:

- `order-application`
- `order-domain`
- `order-dataaccess`
- `order-messaging`
- `common-application`
- `common-domain`

Así compone y ejecuta toda la lógica del microservicio.

---

## 🧠 Responsabilidad principal

Este módulo:

- Arranca el servicio Spring Boot.
- Orquesta la composición de los beans de todos los módulos internos.
- Expone los endpoints HTTP y empieza a escuchar eventos Kafka (cuando se implementen).
- Define los recursos de configuración externa (YAML, perfiles, etc.).