# 🏗️ Infrastructure Module

> Este módulo contiene todos los componentes de **infraestructura externa** necesarios para el sistema de pedidos basado en microservicios.  
Incluye la gestión de eventos, la coordinación distribuida de transacciones, el soporte para mensajería asincrónica, y el entorno de ejecución local con Docker.

---

## 📦 Submódulos incluidos

| Submódulo         | Descripción funcional                                                                 |
|-------------------|----------------------------------------------------------------------------------------|
| `kafka`           | Publicación, consumo y modelado de eventos Kafka.                                     |
| `outbox`          | Implementación del patrón **Transactional Outbox** para garantizar entrega de eventos.|
| `saga`            | Lógica de orquestación y coordinación entre microservicios mediante eventos.          |
| `docker-compose`* | (No Maven) Scripts y configuración para ejecutar la infraestructura local con Docker. |

---

## 🧩 Rol en la arquitectura hexagonal

Este módulo implementa los **adaptadores de entrada y salida** de la arquitectura hexagonal, facilitando la integración entre el núcleo de negocio y los sistemas externos:

```
[ Dominio ] ←→ [ Application ] ←→ [ Infrastructure (Kafka, Outbox, Saga, Docker) ]
```

Los microservicios utilizan estas piezas de infraestructura para emitir eventos, recibir respuestas, coordinar flujos complejos y simular entornos productivos de forma local.

---

## 🧱 Descripción de componentes

### 🟠 `kafka`
- Define productores, consumidores y modelos Avro para mensajería asincrónica.
- Incluye configuración centralizada (`kafka-config-data`).
- Facilita integración con Schema Registry y eventos en formato binario.

### 📬 `outbox`
- Persistencia de eventos como parte de la transacción principal.
- Garantiza consistencia eventual al extraer y publicar eventos de forma segura.
- Útil para evitar problemas de doble envío o pérdida de mensajes.

### 🔄 `saga`
- Coordinación de múltiples pasos distribuidos (como creación y cancelación de pedidos).
- Permite revertir operaciones con eventos compensatorios.
- Soporta tanto **coreografía** como **orquestación** de flujos de negocio.

### 🐳 `docker-compose`
- Archivos YAML y configuración para levantar localmente servicios como:
    - PostgreSQL + PgAdmin
    - Kafka + Zookeeper + Schema Registry
    - ELK stack para logging y trazabilidad
- Útil para desarrollo, pruebas y simulación de entornos productivos.

---

## ✅ Ventajas de este diseño

| Ventaja                | Descripción |
|------------------------|-------------|
| 🧩 Modularidad         | Cada componente cumple una única responsabilidad. |
| 🔁 Reutilización       | Compartido por múltiples microservicios. |
| 🚫 Desacoplamiento     | Se mantiene separado del dominio y la lógica de negocio. |
| 🧪 Entorno aislado     | Docker Compose permite levantar y testear sin depender de producción. |
| ☁️ Escalable           | Preparado para entornos distribuidos y pipelines CI/CD. |

---

## 📌 Nota

El submódulo `docker-compose` **no es un módulo Maven**, pero forma parte clave de esta infraestructura.  
Puedes encontrar su documentación específica dentro de su carpeta respectiva.

---

## 📚 Recursos relacionados

- [Arquitectura Hexagonal](https://alistair.cockburn.us/hexagonal-architecture/)
- [Transactional Outbox Pattern](https://microservices.io/patterns/data/transactional-outbox.html)
- [Saga Pattern](https://microservices.io/patterns/data/saga.html)
- [Apache Kafka](https://kafka.apache.org/)
