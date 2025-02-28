# Etapa de construcción
FROM openjdk:21-jdk-slim as builder

# Establecer directorio de trabajo
WORKDIR /usr/src/app

# Copiar archivos del proyecto
COPY pom.xml ./
RUN mkdir -p src/main/java
COPY src ./src

# Construir el proyecto con Maven
RUN apt-get update && apt-get install -y maven \
    && mvn clean package \
    && rm -rf /var/lib/apt/lists/*

# Etapa final de ejecución
FROM openjdk:21-jdk-slim

# Configurar directorio de trabajo
WORKDIR /usrapp

# Copiar el JAR desde el builder
COPY --from=builder /usr/src/app/target/*.jar app.jar

# Exponer el puerto 8080
EXPOSE 8080

# Comando de ejecución
#CMD ["java", "-jar", "app.jar"]
