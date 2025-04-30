# 🗄️ Módulo: `common-data-access`

> Este módulo proporciona soporte de **persistencia compartida** para entidades como `Restaurant`, utilizadas por múltiples microservicios.

---

## 📦 Componentes

- `RestaurantEntity`, `RestaurantEntityId`: entidades JPA.
- `RestaurantJpaRepository`: interfaz Spring Data JPA.
- `RestaurantDataAccessException`: excepción personalizada para persistencia.

---

## 🎯 Propósito

- Centralizar el acceso a datos de entidades compartidas
- Evitar duplicación de código de infraestructura entre servicios
