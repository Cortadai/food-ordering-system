## Info

Microservicios con Spring Boot. Arquitectura limpia y hexagonal, DDD, SAGA, Outbox, CQRS y Kafka.

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