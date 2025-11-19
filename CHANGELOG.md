# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).  
This project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---
### Agregado
- integración de Broker de Mensajes para centralizar eventos generados por la aplicación.
- Archivo `docker-compose.yml`

### Cambiado
- Uso de eventos para separar del proyecto la funcionalidad de envío de correos y registro de estado.

### Corregido
- Aviso de no disponibilidad cuando el servicio de autentificación está caído.

## [3.1.0] - 2025-11-19
### Added
- Implementación EDA para el manejo de envío de correos y registro de estado de las quejas.
- Logs generados para trazabilidad completa de los eventos.