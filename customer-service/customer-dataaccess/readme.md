# 🗄️ Módulo: `customer-dataaccess`

> Este módulo implementa el **adaptador de salida** para el microservicio de clientes (`customer-service`). Se encarga de persistir la entidad `Customer` usando JPA.

---

## 🧩 Estructura del módulo

```plaintext
com.food.ordering.system.customer.service.dataaccess.customer
├── adapter
├── entity
├── repository
├── mapper
└── exception
```

---

## 🧱 Entidad JPA

- `CustomerEntity`: mapeo JPA de la entidad del dominio `Customer`.

---

## 🔁 Repositorio JPA

- `CustomerJpaRepository`: interfaz que extiende `JpaRepository` y permite CRUD sobre `CustomerEntity`.

---

## 🔌 Adaptador

- `CustomerRepositoryImpl`: implementación del puerto `CustomerRepository` definido en la capa de aplicación.

---

## 🔄 Mapper

- `CustomerDataAccessMapper`: convierte entre `CustomerEntity` y la entidad del dominio `Customer`.

---

## 🚨 Excepciones

- `CustomerDataaccessException`: errores específicos de la persistencia de clientes.

---

## 🎯 Propósito

Este módulo:

- Encapsula la lógica de acceso a base de datos
- Permite que el dominio no dependa de JPA o Spring
- Implementa el patrón **puertos y adaptadores** de la arquitectura hexagonal

---

Es un módulo sencillo pero fundamental para la persistencia segura y desacoplada del cliente.
