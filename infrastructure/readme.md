# 🏗️ Módulo `infrastructure`

Este módulo contiene las **integraciones técnicas y herramientas externas** necesarias para ejecutar el sistema, pero que **no forman parte de la lógica de negocio**.

## 🔹 ¿Qué incluye?

- [`kafka`](./kafka/readme.md): Implementación de la mensajería asíncrona entre servicios mediante Apache Kafka.
- [`docker-compose`](./docker-compose/readme.md): Definiciones de servicios y herramientas en contenedores para facilitar la ejecución local del sistema (Kafka, Zookeeper, Postgres...).

## 🎯 Propósito

El módulo `infrastructure` **no implementa lógica de negocio**, sino que proporciona el soporte necesario para:

- Ejecutar el ecosistema de microservicios en local o entornos de prueba.
- Integrar mecanismos de mensajería (Kafka).
- Montar fácilmente las dependencias externas con Docker.