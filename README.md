# RegistroQuejas

RegistroQuejas es una aplicación para registrar y dar seguimiento a quejas de usuarios en una entidad pública.

## Características

- Registro de quejas por parte de los usuarios
- Visualización y gestión de quejas por administradores
- Seguimiento del estado de cada queja
- Notificaciones y actualizaciones de estado

## Información técnica

- **Java:** 17
- **Spring Boot:** 3.5.4
- **Base de datos:** PostgreSQL v17.6
- **Gestor de dependencias:** Maven
- **Servidor:** Tomcat embebido

### Frontend

- **Thymeleaf:** Motor de plantillas para la generación dinámica de HTML ([src/main/resources/templates/](src/main/resources/templates/)).
- **HTML5 y CSS3:** Estructura y estilos personalizados en [src/main/resources/static/css/style.css](src/main/resources/static/css/style.css).
- **JavaScript:** Funcionalidades interactivas en [src/main/resources/static/js/app.js](src/main/resources/static/js/app.js).
- **Font Awesome:** Íconos para la interfaz (CDN en los archivos de plantilla).
- **Google reCAPTCHA:** Protección contra bots en el formulario de búsqueda.
- **Diseño responsivo:** Adaptación a dispositivos móviles mediante CSS.

### Instalación

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/tu-usuario/RegitroQuejas.git
   ```
2. Configurar las variables del archivo `application.properties` (url, username y password) si es necesario; ubicado en [src\main\resources](src\main\resources).
   + 2.1. Para configuración del servidor en local:
      + **url:** `jdbc:postgresql://localhost:5432/RegistroDB}`
      + **username:** `${DB_USERNAME:nombre_de_usuario_configurado_en_su_postgreSQL}`
      + **password:** `${DB_PASSWORD:contraseña_configurada_en_su_postgreSQL}`
   + 2.2. Para configuración del servidor en despliegue consultar el confluence
      

## Uso

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/tu-usuario/RegitroQuejas.git
   ```
2. Abrir el proyecto y dirigirse al archivo `Main.java` ubicado en: [src\main\java\com\uptc\edu\main](src\main\java\com\uptc\edu\main)
3. Correr el método main o mediante línea de comandos: 
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```
4. Accede a la interfaz web en tu navegador en `http://localhost:8080/registro`. (Local)
5. Para acceder al despliegue de la appi acceder desde el navegador a `https://springboot-complains.onrender.com/registro`.

## SCRIPTS
1. Creación de empresas:
   ```SQL
   INSERT INTO companies (company_id, company_name) values (1,'Ministerio de Salud');
   INSERT INTO companies (company_id, company_name) values (2,'Ministerio de Educación');
   INSERT INTO companies (company_id, company_name) values (3,'Empresa Metropolitana');
   INSERT INTO companies (company_id, company_name) values (4,'Defensoría del Pueblo');
   INSERT INTO companies (company_id, company_name) values (5,'Ministerio de Transporte');
   ```

## Esctructura del proyecto
   ```bash
   RegistroQuejas/
   ├── .gitattributes
   ├── .gitignore
   ├── Dockerfile
   ├── mvnw
   ├── mvnw.cmd
   ├── pom.xml
   ├── README.md
   ├── .github/
   │   ├── release-drafter.yml
   │   └── workflows/
   │       ├── flow.yml
   │       ├── release-drafter.yml
   │       └── update-changelog.yml
   ├── .mvn/
   │   └── wrapper/
   │       └── maven-wrapper.properties
   ├── src/
   │   ├── main/
   │   │   ├── java/
   │   │   │   └── com/
   │   │   │       └── uptc/
   │   │   │           └── edu/
   │   │   │               └── main/
   │   │   │                   ├── controller/
   │   │   │                   ├── dto/
   │   │   │                   ├── model/
   │   │   │                   ├── repository/
   │   │   │                   └── service/
   │   │   └── resources/
   │   │       ├── static/
   │   │       │   ├── css/
   │   │       │   ├── js/
   │   │       │   └── ...
   │   │       ├── templates/
   │   │       │   ├── fragments/
   │   │       │   ├── registro.html
   │   │       │   ├── buscar.html
   │   │       │   └── analisis.html
   │   │       ├── .env.properties
   │   │       └── application.properties
   │   └── test/
   │       └── java/
   │           └── com/
   │               └── uptc/
   │                   └── edu/
   │                       └── main/
   │                           ├── EmailTest.java
   │                           └── RegistroQuejasApplicationTests.java
   ├── target/
   │   ├── RegistroQuejas-0.0.1-SNAPSHOT.jar
   │   ├── classes/
   │   │   ├── .env.properties
   │   │   ├── application.properties
   │   │   ├── com/
   │   │   ├── static/
   │   │   └── templates/
   │   ├── generated-sources/
   │   ├── generated-test-sources/
   │   ├── maven-archiver/
   │   ├── maven-status/
   │   ├── surefire-reports/
   │   └── test-classes/
   └── ...
```

## Licencia

Este proyecto está bajo la licencia MIT.

## Autores

- [Daniel Rojas](https://github.com/RojasD13)
- [Andres Vargas](https://github.com/andres-Vargas02)









