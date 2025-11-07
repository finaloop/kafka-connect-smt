# Kafka Connect SMT: Add Timestamp Fields

**Kafka Connect Single Message Transform (SMT)** to automatically add timestamp fields to Kafka records before sink.  

This SMTs can be used 
---

## Features

- SMT can be used for specific topics.  
- Different SMT to insert record time and current time

---

## Requirements

- Java 17+  
- Maven 3.6+  
- Apache Kafka 3.x  
- Kafka Connect runtime (standalone or distributed)  

---

## Build Instructions

1. **Clone the repository:**

```bash
git clone https://github.com/yourusername/kafka-connect-smt-add-timestamp-fields.git
cd kafka-connect-smt-add-timestamp-fields
```

2. Build the JAR using Maven:

```
mvn clean package
```

3. The compiled JAR will be in the target/ directory:
```
target/kafka-connect-smt-add-timestamp-fields-1.0.0.jar
```

## Local testing

For local testing docker-compose can be used, just place jar into ./plugins directory