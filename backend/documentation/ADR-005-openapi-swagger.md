# ADR 005: Adopción de OpenAPI y Swagger UI para Documentación de la API

## Estado
Aceptado

## Contexto
A medida que el proyecto MVP crece, especialmente con la incorporación de un frontend en Next.js, se vuelve indispensable contar con una documentación clara, interactiva y siempre actualizada de los endpoints REST expuestos por el backend. El equipo necesita poder visualizar los contratos (request/response) y ejecutar pruebas manuales de forma rápida sin depender exclusivamente de herramientas externas como Postman.

## Decisión
Se decide integrar **Springdoc OpenAPI** (springdoc-openapi-starter-webmvc-ui) en el backend. 
Esta herramienta inspecciona automáticamente los controladores de Spring (@RestController) y genera una especificación OpenAPI 3.0 en tiempo real. Además, expone la interfaz interactiva de **Swagger UI**.

## Consecuencias
- **Positivas:** 
  - Generación automática de documentación basada en el código (Single Source of Truth).
  - Interfaz web interactiva (/swagger-ui.html) disponible localmente para probar los endpoints.
  - Facilita enormemente la integración con el equipo de frontend.
- **Negativas:** 
  - Añade una nueva dependencia al proyecto.
  - Puede requerir agregar anotaciones específicas (@Operation, @Schema) si se desea enriquecer la documentación más allá de lo que se deduce automáticamente del código.

