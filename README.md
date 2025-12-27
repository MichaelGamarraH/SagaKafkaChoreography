# SagaKafkaChoreography

Proyecto de ejemplo que implementa el patrón **Saga Coreografía** usando **Kafka** como sistema de mensajería y varios microservicios en Spring Boot.

---

## Microservicios

- **common-utils**  
  Librería compartida con clases y utilidades comunes para los demás servicios.

- **inventory-service**  
  Gestiona el inventario de productos. Actualiza stock en memoria durante la ejecución.

- **order-service**  
  Maneja la creación y el estado de las órdenes. Coordina con los demás servicios vía eventos Kafka.

- **payment-service**  
  Procesa pagos y comunica resultados al resto de servicios.

---

## 🗄️ Base de datos

- Se utiliza una **estructura Map en memoria** durante la ejecución.  
- No hay persistencia en disco: los datos se pierden al reiniciar la aplicación.  
- Esto simplifica la demostración del patrón Saga sin necesidad de configurar una base de datos externa.

---

## ⚙️ Configuración de Kafka en Docker

Para levantar un broker Kafka local en modo KRaft:

```bash
# Crear carpeta de trabajo
mkdir -p ~/proyectos/kafka
cd ~/proyectos/kafka

# Crear archivo de configuración kafka.properties
cat > kafka.properties << 'EOF'
process.roles=broker,controller
node.id=1
controller.quorum.voters=1@localhost:9093
listeners=PLAINTEXT://:9092,CONTROLLER://:9093
advertised.listeners=PLAINTEXT://localhost:9092
listener.security.protocol.map=CONTROLLER:PLAINTEXT,PLAINTEXT:PLAINTEXT
inter.broker.listener.name=PLAINTEXT
log.dirs=/tmp/kraft-combined-logs
EOF

# Descargar imagen oficial de Kafka
docker pull apache/kafka:3.9.1

# Ejecutar contenedor con configuración montada
docker run -d --name saga-with-kafka \
  -p 9092:9092 -p 9093:9093 \
  -v "$HOME/proyectos/kafka/kafka.properties":/opt/kafka/kafka.properties \
  apache/kafka:3.9.1
