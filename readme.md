## Food-Ordering-System

Microservicios con Spring Boot. Arquitectura limpia y hexagonal, DDD, SAGA, Outbox, CQRS y Kafka.

![Texto alternativo](./infrastructure/assets/project-overview-section-1.png)

---

# 🍽️ Flujo completo de una orden en el sistema de pedidos

Este documento explica el **flujo de trabajo completo** desde que se crea una orden hasta que es aprobada o rechazada, detallando la participación de cada microservicio, evento Kafka, módulo `saga` y `outbox`.

---

## 🧭 Resumen del flujo

```
Cliente → Order Service → Saga / Outbox → Kafka → Payment Service / Restaurant Service → Kafka → Order Service → Finalización
```

---

## 🛒 Paso a paso

### 1. **Creación de la orden (cliente)**

- El cliente realiza una petición `POST /orders` al `OrderController`.
- La petición se convierte en un `CreateOrderCommand` y es gestionada por `OrderApplicationService`.

### 2. **Inicio del proceso de orden**

- Se ejecuta la lógica de negocio en `OrderCreateHelper` y `OrderDomainService`.
- Se crea la entidad `Order` con estado `PENDING`.
- Se genera un evento `OrderCreatedEvent`.

### 3. **Inicio de la Saga de Pago**

- El `OrderPaymentSaga` intercepta el evento y llama a `PaymentRequestMessagePublisher`.
- Se construye el payload y se guarda en la tabla `payment_outbox` (patrón **Outbox**).
- El `PaymentOutboxScheduler` detecta nuevos mensajes y los publica en Kafka.

---

## 💳 Validación del pago (Payment Service)

- `PaymentRequestKafkaListener` consume el evento desde Kafka.
- El `PaymentDomainService` evalúa si el cliente tiene crédito.
- Se genera uno de estos eventos:
   - `PaymentCompletedEvent`
   - `PaymentFailedEvent`

- Estos eventos se colocan en `order_outbox` y luego se publican a Kafka.

---

## 📦 Procesamiento de la respuesta de pago

- `PaymentResponseKafkaListener` en `OrderService` recibe el evento.
- `PaymentResponseMessageListenerImpl` lo maneja:
   - Si fue exitoso: avanza la saga.
   - Si falló: cancela la orden.

---

## 🍽️ Aprobación del restaurante

- Si el pago fue correcto, el `OrderApprovalSaga` inicia la segunda fase.
- Se genera un `RestaurantApprovalRequest` y se guarda en `approval_outbox`.
- El `ApprovalOutboxScheduler` publica el evento a Kafka.

---

## 🧑‍🍳 Respuesta del restaurante

- `RestaurantApprovalRequestKafkaListener` en `RestaurantService` recibe el evento.
- Evalúa si el restaurante acepta el pedido (stock, disponibilidad...).
- Publica una respuesta (`approved` o `rejected`) a Kafka.

---

## ✅ Finalización

- `RestaurantApprovalResponseKafkaListener` en `OrderService` consume el evento.
- `RestaurantApprovalResponseMessageListenerImpl` actualiza el estado de la orden:
   - `APPROVED` → Pedido confirmado.
   - `REJECTED` → Pedido cancelado.

- La saga se cierra. El flujo ha finalizado.

---

## 🧠 Participación de los módulos

| Módulo          | Rol                                                                 |
|------------------|----------------------------------------------------------------------|
| `order-application` | Lógica principal de orquestación, inicio de Saga, publicación inicial |
| `saga`           | Define los pasos y estados posibles de la transacción distribuida    |
| `outbox`         | Garantiza consistencia entre base de datos y Kafka                   |
| `kafka`          | Mecanismo de transporte asíncrono de eventos                         |
| `payment-service`| Evalúa crédito del cliente                                           |
| `restaurant-service` | Acepta o rechaza la orden                                         |

---

## 🔁 Diagrama de flujo simplificado

```
[ Order Service ]
    ↓ create order
[ Saga Init ] ──> [ Outbox → Kafka ] ──> [ Payment Service ]
                                        ↓
                               [ Kafka → Order Service ]
                                        ↓
                                [ Saga Step 2 ] ──> [ Restaurant Service ]
                                                   ↓
                                        [ Kafka → Order Service ]
                                                   ↓
                                          Orden Finalizada
```

---

## ✅ Conclusión

Este flujo demuestra cómo aplicar de forma efectiva los patrones **Saga**, **Outbox** y **mensajería con Kafka** en una arquitectura de microservicios.  
El diseño garantiza **consistencia eventual**, **desacoplamiento**, **resiliencia** y **trazabilidad completa** del proceso de una orden.

---

## 🚀 Build Configurations

### 🔧 General Builds

1. **Clean install (sin tests)**
   ```bash
   mvn clean install -DskipTests=true
   ```


2. **Clean, test, install (con tests)**
   ```bash
   mvn clean test install
   ```

### 🗑️ Limpieza de volúmenes

3. **Eliminar volúmenes de base de datos**
   ```bash
   mvn antrun:run@database-clean-folders -DskipTests=true
   ```

4. **Eliminar volúmenes de Kafka**
   ```bash
   mvn antrun:run@kafka-clean-folders -DskipTests=true
   ```

### 🐳 Build de imágenes Docker por servicio

5. **Customer Service**
   ```bash
   mvn -f F:/hex/food-ordering-system/customer-service/pom.xml clean install -P docker -DskipTests=true
   ```

6. **Order Service**
   ```bash
   mvn -f F:/hex/food-ordering-system/order-service/pom.xml clean install -P docker -DskipTests=true
   ```

7. **Payment Service**
   ```bash
   mvn -f F:/hex/food-ordering-system/payment-service/pom.xml clean install -P docker -DskipTests=true
   ```

8. **Restaurant Service**
   ```bash
   mvn -f F:/hex/food-ordering-system/restaurant-service/pom.xml clean install -P docker -DskipTests=true
   ```

---

## 💎 Utils

### 📊 graphviz

A continuación vemos cómo sacar un gráfico con las dependencias del proyecto:

1. Instalamos [graphviz](https://www.graphviz.org/download/)

2. Nos aseguramos de agregarlo al PATH

3. Dentro de una terminal con permisos de administrador corremos el siguiente comando:
```
mvn com.github.ferstl:depgraph-maven-plugin:4.0.3:aggregate -DcreateImage=true -DreduceEdges=false -Dscope=compile "-Dincludes=com.food.ordering.system*:*"
```

### 💻 kafkacat

Como instalar y usar kafkacat:

``` 
sudo apt-get install kafkacat
```

``` 
kafkacat -C -b localhost:19092 -t payment-request
```

----

## 📦 API Testing with `curl`

### 👤 Crear clientes

#### 🧍‍♂️ Crear cliente 1 (`user_1`)
```bash
curl --location 'http://localhost:8184/customers' \
--header 'Content-Type: application/json' \
--data '{
  "customerId": "d215b5f8-0249-4dc5-89a3-51fd148cfb41",
  "username": "user_1",
  "firstName": "First",
  "lastName": "User"
}'
```

#### 🧍‍♂️ Crear cliente 2 (`user_2`)
```bash
curl --location 'http://localhost:8184/customers' \
--header 'Content-Type: application/json' \
--data '{
  "customerId": "d215b5f8-0249-4dc5-89a3-51fd148cfb43",
  "username": "user_2",
  "firstName": "Second",
  "lastName": "User"
}'
```
---

### ✅ Pedido correcto

```bash
curl --location 'http://localhost:8181/orders' \
--header 'Content-Type: application/json' \
--data '{
  "customerId": "d215b5f8-0249-4dc5-89a3-51fd148cfb41",
  "restaurantId": "d215b5f8-0249-4dc5-89a3-51fd148cfb45",
  "address": {
    "street": "street_1",
    "postalCode": "1000AB",
    "city": "Amsterdam"
  },
  "price": 200.00,
  "items": [
    {
      "productId": "d215b5f8-0249-4dc5-89a3-51fd148cfb48",
      "quantity": 1,
      "price": 50.00,
      "subTotal": 50.00
    },
    {
      "productId": "d215b5f8-0249-4dc5-89a3-51fd148cfb48",
      "quantity": 3,
      "price": 50.00,
      "subTotal": 150.00
    }
  ]
}'
```

#### ❌ Pedido con precio elevado (error en `payment-service`)
> El cliente no tiene suficiente crédito para pagar el pedido. Hay que lanzarlo tras un pedido correcto.
```bash
curl --location 'http://localhost:8181/orders' \
--header 'Content-Type: application/json' \
--data '{
  "customerId": "d215b5f8-0249-4dc5-89a3-51fd148cfb41",
  "restaurantId": "d215b5f8-0249-4dc5-89a3-51fd148cfb45",
  "address": {
    "street": "street_1",
    "postalCode": "1000AB",
    "city": "Amsterdam"
  },
  "price": 550.00,
  "items": [
    {
      "productId": "d215b5f8-0249-4dc5-89a3-51fd148cfb48",
      "quantity": 1,
      "price": 50.00,
      "subTotal": 50.00
    },
    {
      "productId": "d215b5f8-0249-4dc5-89a3-51fd148cfb48",
      "quantity": 10,
      "price": 50.00,
      "subTotal": 500.00
    }
  ]
}'
```
### 🧪 Pedidos con error

#### ❌ Pedido con producto no disponible (error en `restaurant-service`)
> El producto `"d215b5f8-0249-4dc5-89a3-51fd148cfb47"` esta deshabilitado en base de datos.
```bash
curl --location 'http://localhost:8181/orders' \
--header 'Content-Type: application/json' \
--data '{
  "customerId": "d215b5f8-0249-4dc5-89a3-51fd148cfb41",
  "restaurantId": "d215b5f8-0249-4dc5-89a3-51fd148cfb45",
  "address": {
    "street": "street_1",
    "postalCode": "1000AB",
    "city": "Amsterdam"
  },
  "price": 225.00,
  "items": [
    {
      "productId": "d215b5f8-0249-4dc5-89a3-51fd148cfb48",
      "quantity": 1,
      "price": 50.00,
      "subTotal": 50.00
    },
    {
      "productId": "d215b5f8-0249-4dc5-89a3-51fd148cfb48",
      "quantity": 3,
      "price": 50.00,
      "subTotal": 150.00
    },
    {
      "productId": "d215b5f8-0249-4dc5-89a3-51fd148cfb47",
      "quantity": 1,
      "price": 25.00,
      "subTotal": 25.00
    }
  ]
}'
```
---

### 🔍 Consultar estado de una orden por `trackingId`

```bash
curl --location 'http://localhost:8181/orders/043b4032-8655-46e6-bb79-9652cea46b8a'
```