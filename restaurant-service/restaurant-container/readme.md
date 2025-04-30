# 🚀 Módulo: `restaurant-container`

> Este módulo representa el **punto de entrada (boot module)** del microservicio de restaurante (`restaurant-service`). Se encarga de arrancar la aplicación Spring Boot y ensamblar los componentes internos.

---

## 🧩 Estructura del módulo

```plaintext
restaurant-container
├── src/main/java
│   └── com.food.ordering.system.restaurant.service.domain
│       ├── RestaurantServiceApplication.java
│       └── BeanConfiguration.java
├── resources
│   ├── application.yml
│   ├── init-schema.sql
│   └── logback-spring.xml
```

---

## 🚀 `RestaurantServiceApplication.java`

- Clase principal del microservicio.
- Usa `@SpringBootApplication` para iniciar Spring y el escaneo de componentes.

```java
@SpringBootApplication
public class RestaurantServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(RestaurantServiceApplication.class, args);
    }
}
```

---

## ⚙️ `BeanConfiguration.java`

- Configura explícitamente los `@Bean` necesarios.
- Conecta puertos con sus implementaciones:
    - Mapeadores
    - Servicios del dominio
    - Publicadores Kafka
    - Listeners

---

## 📦 Recursos

- `application.yml`: configuración de Kafka, base de datos, logs, puertos, etc.
- `init-schema.sql`: script para inicializar las tablas necesarias.
- `logback-spring.xml`: configuración de logging.

---

## 🔗 Dependencias

Este módulo importa:

- `restaurant-domain`
- `restaurant-dataaccess`
- `restaurant-messaging`
- `common-domain`
- `common-application`

---

## ✅ Rol dentro de la arquitectura

- **Composición final del microservicio**
- **Producción del `.jar` ejecutable**
- Único módulo que se despliega directamente en entornos (Docker, Kubernetes, etc.)

---

Este módulo es esencial para levantar, configurar y desplegar el microservicio `restaurant-service`.
