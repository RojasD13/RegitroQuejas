<<<<<<< HEAD
# ================================
# Etapa 1: Build con Maven
# ================================
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Crear directorio de trabajo
WORKDIR /app

# Copiar pom.xml y descargar dependencias
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar código fuente y compilar
COPY src ./src
RUN mvn clean package -DskipTests

# ================================
# Etapa 2: Imagen final
# ================================
FROM eclipse-temurin:17-jdk-alpine

# Crear directorio de trabajo
WORKDIR /app

# Copiar el jar generado desde la etapa build
COPY --from=build /app/target/*.jar app.jar

# Exponer el puerto (ajústalo si en tu application.properties cambiaste el default)
EXPOSE 8080

# Comando de inicio
ENTRYPOINT ["java","-jar","app.jar"]
=======
# Imagen base con JDK 17 (ajusta la versión según uses)
FROM eclipse-temurin:17-jdk-alpine

# Crear directorio para la app
WORKDIR /app

# Copiar el JAR generado por Maven/Gradle
COPY target/*.jar app.jar

# Exponer el puerto de tu aplicación
EXPOSE 8080

# Comando de inicio
ENTRYPOINT ["java", "-jar", "app.jar"]
>>>>>>> main
