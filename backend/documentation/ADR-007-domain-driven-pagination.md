# ADR 007: Paginación y Filtrado Aislados en el Dominio

## Estado
Aceptado

## Contexto
El MVP requiere endpoints que soporten paginación y filtrado (ej. para Categorías y Productos). En el ecosistema Spring, es muy común utilizar las interfaces `Pageable`, `Page` y `Specification` de Spring Data JPA.
Sin embargo, inyectar estas clases directamente en las interfaces de los repositorios de dominio (ej. `CategoryRepository`) o en los servicios de dominio violaría nuestra regla principal de Arquitectura Hexagonal: **El dominio no debe depender de frameworks externos (Spring)**.

## Decisión
Se decidió crear nuestras propias abstracciones de dominio para manejar estas operaciones:

1. **Abstracciones de Dominio**: Se crearon los Value Objects `PaginationQuery` (para requests de paginación) y `DomainPage<T>` (como contenedor de resultados paginados) dentro de `com.eshop.domain.base`. Para el filtrado, se crearán clases de tipo `Filter` específicas por entidad (ej. `CategoryFilter`).
2. **Uso en Interfaces Puras**: Las interfaces de repositorio del dominio reciben un `PaginationQuery` y devuelven un `DomainPage<T>`, manteniéndose agnósticas de la base de datos.
3. **Mapeo en los Adapters**: Los adaptadores de infraestructura (ej. `CategoryRepositoryAdapter`) son los únicos responsables de tomar nuestro `PaginationQuery`, convertirlo a un `PageRequest` de Spring Data, ejecutar la query sobre el repositorio JPA real, y finalmente mapear el objeto `Page<Entity>` de Spring nuevamente a un `DomainPage<DomainObject>`.

## Consecuencias
**Positivas:**
- Respetamos estrictamente los límites arquitectónicos del DDD. El dominio queda completamente limpio de dependencias de Spring Data.
- Facilita testear la lógica de paginación en el dominio de forma aislada.

**Negativas:**
- Agrega una pequeña sobrecarga de transformación de datos (mapping) en la capa de adaptadores, debiendo desensamblar y reensamblar las páginas de datos en cada petición.
