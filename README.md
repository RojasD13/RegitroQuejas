# RegitroQuejas

RegistroQuejas es una aplicación para registrar y dar seguimiento a quejas de usuarios en una entidad pública.

## Características

- Registro de quejas por parte de los usuarios
- Visualización y gestión de quejas por administradores
- Seguimiento del estado de cada queja
- Notificaciones y actualizaciones de estado

## Información técnica

- **Java:** 17
- **Spring Boot:** 3.5.4
- **Base de datos:** PostgreSQL
- **Gestor de dependencias:** Maven
- **Servidor:** Tomcat embebido
- **Dirección Servidor de despliegue:** `https://dashboard.render.com/web/srv-d2jnpvili9vc73c1l080/deploys/dep-d2jp5tje5dus738dbg3g?r=2025-08-21%4021%3A42%3A18~2025-08-21%4021%3A46%3A30`.
- **Imagen de Docker:** `https://hub.docker.com/r/danielrojas94/springboot-complains/tags`

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
2. Configurar las variables del archivo application.properties (url, username y password) si es necesario.
   2.1. Para configuración de desliegue:
      2.1.1 **url:** `jdbc:postgresql://ep-aged-dream-aepj5xhv-pooler.c-2.us-east-2.aws.neon.tech/neondb`
      2.1.2 **username:** `neondb_owner`
      2.1.3 **password:** `npg_dmwC9Obx1ESB`

## Uso

1. Accede a la interfaz web en tu navegador en `http://localhost:8080/registro`. (Local)
2. Para acceder al despliegue de la appi acceder desde el navegador a `https://springboot-complains.onrender.com/registro`.


## Licencia

Este proyecto está bajo la licencia MIT.

## Autores

- [Daniel Rojas](https://github.com/RojasD13)
- [Andres Vargas](https://github.com/andres-Vargas02)




