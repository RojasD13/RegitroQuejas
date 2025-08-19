# 🏗 Etapa 1: Construcción
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app

# Copiar pom.xml y descargar dependencias primero (para cache)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar el código fuente y compilar
COPY src ./src
RUN mvn clean package -DskipTests

# 🏗 Etapa 2: Imagen final
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

# Copiamos el .jar generado desde la etapa anterior
COPY --from=builder /app/target/*.jar app.jar

# Puerto interno de la aplicación
EXPOSE 8080

# Perfil "prod" para Render
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]
