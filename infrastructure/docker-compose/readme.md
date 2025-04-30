# 🐳 docker-compose - Infraestructura local para desarrollo

> Este módulo define la infraestructura necesaria para ejecutar y probar el sistema de microservicios en local.  
Utiliza múltiples archivos `docker-compose` para orquestar servicios como Kafka, PostgreSQL, Schema Registry, Zookeeper y el stack ELK.

---

## 🧱 Servicios orquestados

| Servicio                     | Descripción                                                  | Puerto(s)                  |
|------------------------------|--------------------------------------------------------------|----------------------------|
| 🐘 PostgreSQL                | Base de datos relacional principal.                          | 5432                       |
| 🔑 PgAdmin                   | Interfaz de administración para PostgreSQL.                  | 7777                       |
| 📦 Zookeeper                 | Coordinador de clúster para Kafka.                           | 2181                       |
| 🟠 Apache Kafka              | Sistema de mensajería distribuido.                           | 9092, 9093, 9094           |
| 📑 Schema Registry           | Registro de esquemas Avro para Kafka.                        | 8081                       |
| 📊 Kafka UI (Kafdrop u otro) | Herramienta de visualización para topics Kafka.             | 9000                       |
| 📈 Elasticsearch             | Almacenamiento de logs estructurados.                        | 9200                       |
| 🧪 Logstash                  | Procesamiento de logs (parsing, filtrado, etc.).             | 5044 (default pipeline)    |
| 📺 Kibana                    | Dashboard de visualización de logs.                          | 5601                       |

---

## 📂 Estructura del módulo

```text
docker-compose/
├── .env                    # Variables reutilizadas (hostnames, puertos, rutas)
├── common.yaml             # Redes, volúmenes y configuraciones compartidas
├── database.yaml           # PostgreSQL y PgAdmin
├── kafka-cluster.yaml      # Brokers Kafka (multi-nodo)
├── kafka-init.yaml         # Inicialización de topics Kafka (solo una vez)
├── zookeeper.yaml          # Servicio de Zookeeper
├── elk.yaml                # Elasticsearch, Logstash y Kibana
├── volumes/                # Directorios persistentes (vía bind mount o volúmenes)
│   ├── kafka/
│   ├── zookeeper/
│   ├── logstash/
│   └── database/
└── readme.md               # Este documento
```

---

## 🚀 Cómo levantar los servicios

> Asegúrate de tener **Docker y Docker Compose** instalados.

### 🔄 Inicialización de Kafka (solo la primera vez)

Ejecutar **en terminales separadas** (o usar `-d` para modo detached):

```bash
docker-compose -f common.yaml -f zookeeper.yaml up
docker-compose -f common.yaml -f kafka-cluster.yaml up
docker-compose -f common.yaml -f kafka-init.yaml up
```

### 🔁 Kafka en ejecuciones posteriores

```bash
docker-compose -f common.yaml -f zookeeper.yaml up
docker-compose -f common.yaml -f kafka-cluster.yaml up
```

### 🐘 Base de datos y panel

```bash
docker-compose -f database.yaml up -d
```

### 📊 Stack ELK (Logs)

```bash
docker-compose -f elk.yaml up -d
```

---

## 🌐 Acceso a herramientas

| Herramienta         | URL                                 |
|---------------------|--------------------------------------|
| PgAdmin             | http://localhost:7777                |
| Kafka UI / Kafdrop  | http://localhost:9000                |
| Schema Registry     | http://localhost:8081                |
| Kibana              | http://localhost:5601                |

---

## 🧠 Ventajas del diseño modular

- ✅ **Separación por capas**: puedes levantar solo lo que necesites.
- ✅ **Reutilización de volúmenes**: persistencia y aislamiento por servicio.
- ✅ **Multi-ambiente**: posible extender para CI/CD, staging, etc.
- ✅ **Independencia del lenguaje**: usable por microservicios en cualquier stack.

---

## 📌 Recomendaciones

- Usa `.env` para cambiar puertos o rutas si algún servicio entra en conflicto.
- Agrega más servicios creando nuevos YAML que extiendan `common.yaml`.
- Revisa `volumes/` para limpiar datos si hay inconsistencias.

---

## ✅ Conclusión

Esta carpeta permite levantar de forma rápida y confiable toda la infraestructura de soporte para pruebas, desarrollo local o simulación de escenarios productivos.  
Ideal para integrarse con los microservicios del sistema durante el desarrollo.
