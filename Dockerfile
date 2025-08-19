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
