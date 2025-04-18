# 🐳 `docker-compose` - Infraestructura para el entorno de desarrollo

Este submódulo contiene los **archivos `docker-compose.yml` y configuraciones auxiliares** necesarios para levantar los servicios externos requeridos por el sistema, como bases de datos, Kafka, Schema Registry, etc.

Su propósito es facilitar el **despliegue local o en pruebas** de toda la infraestructura necesaria para ejecutar el sistema completo.

---

## 🧱 Servicios orquestados

| Servicio                     | Descripción                                                  |
|------------------------------|--------------------------------------------------------------|
| 🐘 PostgreSQL                | Base de datos para persistencia (por ejemplo, pedidos).      |
| 🔑 PgAdmin                   | Dashboard para la base de datos.                             |
| 🟠 Apache Kafka              | Broker de eventos para comunicación asíncrona entre servicios. |
| 📑 Confluent Schema Registry | Registro de esquemas Avro para validar mensajes Kafka.       |
| 📡 Kafka UI / Kafdrop        | Interfaz para visualizar topics, mensajes y configuraciones. |
| 📦 Zookeeper                 | Requisito de Kafka para coordinación (en versiones no KRaft). |

> 🔁 Es posible que se incluyan otros servicios en el futuro (por ejemplo, servicios simulados para restaurante o pagos).

---

## 📂 Estructura del módulo

```bash
docker-compose/
├── database.yaml                # Archivo con la base de datos y dashboard
├── .env                        # Variables de entorno comunes
├── common.yaml                 # Scripts auxiliares
├── zookeeper.yaml              # Archivo con el coordinador de Kafka
├── kafka-cluster.yaml          # Broker de eventos
├── kafka-init.yaml             # Scripts auxiliares de kafka que se corren SOLO 1 vez
├── volumes                     # Mapeo de todos los volúmenes de docker (no se suben a git)
└── README.md                   # Este mismo documento
```

---

## 🚀 Cómo utilizar

1. Asegúrate de tener Docker y Docker Compose instalados.
2. Desde el directorio `docker-compose`, ejecuta:

### Para kafka la 1ª vez (esperando a que entre comandos los contenedores se armen y estén estables)
#### Cada comando en una ventana de terminal diferente

```bash
docker-compose -f .\common.yaml -f .\zookeeper.yaml up
docker-compose -f .\common.yaml -f .\kafka-cluster.yaml up
docker-compose -f .\common.yaml -f .\kafka-init.yaml up
```
### Para kafka una vez arrancado la 1ª vez y de entonces en adelante (esperando a que entre comandos los contenedores se armen y estén estables)
#### Cada comando en una ventana de terminal diferente

```bash
docker-compose -f .\common.yaml -f .\zookeeper.yaml up
docker-compose -f .\common.yaml -f .\kafka-cluster.yaml up
```
### Para la base de datos

```bash
docker-compose -f .\database.yaml up -d
```

3. Accede a servicios como:

- **Kafka manager**: http://localhost:9000
- **Schema Registry**: http://localhost:8081
- **PostgreSQL**: puerto 5432
- **PgAdmin**: http://localhost:7777

---

## 🧠 ¿Por qué está separado este módulo?

- Aísla la infraestructura del código de negocio.
- Facilita el **encendido/apagado** de los entornos necesarios sin contaminar el código fuente.
- Permite que otros microservicios usen esta configuración sin importar su lenguaje o stack.
- **Escalable**: puedes añadir nuevos servicios o redes Docker sin afectar otros módulos.