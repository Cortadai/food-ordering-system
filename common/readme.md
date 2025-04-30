# 🔧 Módulo principal: `common`

> Este módulo agrupa componentes **reutilizables** y **compartidos** entre todos los microservicios del sistema. Está dividido en tres submódulos: `common-domain`, `common-application` y `common-data-access`.

---

## 📦 Submódulos incluidos

| Submódulo              | Descripción                                                                 |
|------------------------|-----------------------------------------------------------------------------|
| `common-domain`        | Entidades base, objetos de valor, eventos de dominio compartidos           |
| `common-application`   | Utilidades comunes de la capa de aplicación, como manejo global de errores |
| `common-data-access`   | Persistencia compartida, como la entidad `Restaurant`                      |

---

## 🧩 ¿Por qué existe este módulo?

El objetivo de `common` es **evitar duplicación** y facilitar una base sólida para todos los microservicios, que puedan compartir:

- Tipos genéricos (`Money`, `BaseId`, `OrderStatus`, etc.)
- Entidades JPA comunes
- Utilidades de infraestructura y errores

---

## 🔁 Dependencias

Cada microservicio importa uno o más de estos submódulos según lo que necesita:

- `order-service`, `restaurant-service`, `payment-service` utilizan `common-domain` y `common-application`
- `order-service` y `restaurant-service` utilizan `common-data-access` para gestionar `Restaurant`

---

## ✅ Beneficios

- Reutilización consistente entre servicios
- Separación de responsabilidades
- Mejores prácticas de diseño modular

Este módulo es clave para mantener una arquitectura limpia y coherente en un ecosistema de microservicios.
