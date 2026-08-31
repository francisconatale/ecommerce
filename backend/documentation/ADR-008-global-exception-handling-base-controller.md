# ADR 008: Abstracción de Respuestas REST y Manejo Global de Excepciones

## Estado
Aceptado

## Contexto
Los controladores de la capa Web (`infrastructure/web`) empezaban a acumular código repetitivo, específicamente en:
1. La construcción de objetos `ResponseEntity` (particularmente para el código 201 Created y el header `Location`, o los retornos de status 204/200).
2. El manejo individual de errores que podrían ocurrir en las operaciones de negocio.
Adicionalmente, se detectó el uso de Fully Qualified Names (FQNs) literales de forma inline en el código de controladores y excepciones, rompiendo la regla de "Clean Code & Imports".

## Decisión
Se implementaron dos estrategias principales en la capa Web:

1. **Clase Abstracta `BaseController`**:
   - Centraliza los métodos de respuesta estándar (ej. `ok()`, `created()`, `noContent()`).
   - Envuelve automáticamente la salida utilizando el sobre común estandarizado del proyecto (`ApiResponse.success(...)`).
   - Los controladores individuales (ej. `CategoryController`) ahora extienden de esta clase abstracta, reduciendo drásticamente el ruido del boilerplate de Spring en los endpoints.

2. **`GlobalExceptionHandler` unificado**:
   - A través de un `@RestControllerAdvice`, se centralizó la captura de todas las excepciones específicas del negocio (ej. `ResourceNotFoundException`, `BusinessException`, etc.).
   - Se mapea unívocamente cada tipo de excepción de dominio a un código de estado HTTP adecuado (404, 400, 409, 405, etc.) envolviendo el mensaje en `ApiResponse.error(...)`.
   - Se reemplazaron todos los usos de nombres completamente calificados (FQNs inline) por importaciones convencionales en la parte superior del archivo.

## Consecuencias
**Positivas:**
- Controladores limpios, enfocados puramente en enrutar las peticiones al dominio y devolver el objeto resultante de forma legible.
- Manejo de excepciones consistente en toda la API; si se lanza un error de dominio, la capa web siempre lo traducirá al formato JSON esperado por los clientes, con el HttpStatus correcto.
- Se asegura el cumplimiento de las reglas de importación limpia del proyecto.

**Negativas:**
- Cualquier respuesta con una cabecera o estado HTTP muy particular y fuera de la norma requerirá sobreescribir u obviar los métodos de `BaseController` en endpoints aislados.
