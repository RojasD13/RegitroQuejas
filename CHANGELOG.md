# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).  
This project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]
### Added
- Lista de nuevas features

### Changed
- Cambios importantes

### Fixed
- Correcciones de bugs

## [2.1.0] - 2025-10-20
### Added
- Implementación completa del módulo de autenticación.
- Validación de inicio de sesión con manejo de escenarios:
  - Usuario no existente.
  - Contraseña incorrecta.
  - Inicio de sesión exitoso con activación de sesión.
- Validación de sesión activa:
  - Cada petición verifica que la sesión esté activa antes de proceder.
- Cierre de sesión:
  - Desactivación de sesión del usuario al cerrar sesión.
- Integración del servicio de autenticación con la aplicación principal (API Gateway) para consumo de endpoints de Auth.

## [2.0.0] - 2025-10-20
### Added
- 

