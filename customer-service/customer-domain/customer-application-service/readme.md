# 📦 Módulo: `customer-application-service`

> Este módulo implementa el caso de uso de **creación de cliente**, orquestando la lógica del dominio, repositorio y publicación de eventos.

---

## 🧩 Estructura general

```plaintext
com.food.ordering.system.customer.service.domain
├── config
├── create
├── exception
├── mapper
├── ports
│   ├── input.service
│   └── output.{repository,message.publisher}
├── CustomerCreateCommandHandler
└── CustomerApplicationServiceImpl
```

---

## 📥 Puerto de entrada

- `CustomerApplicationService`: interfaz con método `createCustomer(CreateCustomerCommand)`.
- `CustomerApplicationServiceImpl`: implementación que delega en el handler.

---

## 🧠 Orquestador

- `CustomerCreateCommandHandler`: ejecuta el flujo de creación de cliente:
    - Valida datos
    - Crea entidad y evento
    - Persiste el cliente
    - Publica el evento

---

## 🔄 DTOs y Mapper

- `CreateCustomerCommand`: datos de entrada.
- `CreateCustomerResponse`: resultado de la operación.
- `CustomerDataMapper`: convierte entre DTOs y entidades del dominio.

---

## 📤 Puertos de salida

- `CustomerRepository`: interfaz para persistencia.
- `CustomerMessagePublisher`: interfaz para publicar el evento de cliente creado.

---

## ⚙️ Configuración

- `CustomerServiceConfigData`: valores externos del servicio.

---

## ✅ Conclusión

Este módulo representa la **capa de aplicación** del microservicio `customer-service`. Coordina la lógica de creación de clientes y la publicación de eventos de forma limpia y desacoplada.
