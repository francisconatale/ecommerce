# ADR 0001: Desacoplamiento de Capa Web y Dominio en Clean Architecture

## Estado
Aceptado

## Contexto
El prototipo inicial de la aplicación presentaba un acoplamiento donde los Casos de Uso (Servicios de Dominio) recibían directamente los DTOs de entrada (`CategoryRequest`, `ProductRequest`) definidos en la capa Web (Infraestructura). 
Además, se identificaron problemas con la semántica de las operaciones de actualización (`PUT` estricto vs `PATCH` parcial) y ambigüedades sobre cómo manejar la inyección de dependencias de paginación (`Pageable` de Spring Data) en el dominio.

## Decisiones

1. **Implementación del Patrón Command:**
   Se crearon objetos intermedios (`CreateCategoryCommand`, `UpdateCategoryCommand`, `CreateProductCommand`, `UpdateProductCommand`) dentro de la capa de Dominio. Los servicios ahora consumen estos comandos en lugar de los DTOs web, garantizando que el dominio sea agnóstico respecto a la infraestructura web.

2. **Refactorización de DTOs Web (Validación Estricta):**
   Se separaron los DTOs genéricos en peticiones de Creación (`CreateProductRequest`, `CreateCategoryRequest`) y Actualización (`UpdateProductRequest`, `UpdateCategoryRequest`). Esto permite aplicar validaciones estrictas (`@NotBlank`, `@NotNull`) al crear, y mantener flexibilidad (campos opcionales) durante la actualización, todo verificado automáticamente vía `@Valid` en el controlador.

3. **Semántica de Actualización Parcial (PATCH vs PUT):**
   Se reemplazaron los endpoints `@PutMapping` por `@PatchMapping`. Para preservar las reglas de negocio del Dominio (ej. `validatePrices()`), el "merge" de datos (valores nuevos vs valores existentes en BD) se realiza manualmente en el Servicio usando `Optional.ofNullable(...).orElse(...)`, evitando el uso de reflexión que podría eludir dichas reglas.

4. **Separación de Mappers (Web vs Persistencia):**
   Se introdujo `ProductWebMapper` (basado en MapStruct) en la capa Web para mapear objetos de Dominio hacia DTOs de Respuesta (`ProductResponse`). Se mantiene así una separación explícita respecto al `ProductMapper` de la capa de persistencia (Dominio <-> Entity).

## Consecuencias

### Positivas
- El núcleo de negocio (Dominio) ahora es 100% independiente de los contratos JSON y validaciones HTTP.
- Mayor flexibilidad y robustez en la validación de peticiones entrantes.
- Código de fusión de datos funcional, seguro a nulos, y predecible.
- Protección total de las invariantes y reglas de negocio al actualizar entidades.

### Negativas / Trade-offs
- Aumento en la cantidad de clases (creación de Commands, separación de Requests, adición de Web Mappers), lo cual introduce "boilerplate" (código repetitivo) en el proyecto.
- Se aceptó conscientemente el "leak" pragmático de `Pageable` de Spring Data dentro de las interfaces del Dominio para evitar la sobre-ingeniería de crear abstracciones propias de paginación, reconociendo esto como una decisión de conveniencia sobre pureza absoluta.
