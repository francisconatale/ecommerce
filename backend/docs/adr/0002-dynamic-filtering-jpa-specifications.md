# ADR 0002: Implementación de Filtros Dinámicos con JPA Specifications

## Estado
Aceptado

## Contexto
El sistema requería la capacidad de filtrar entidades (específicamente Productos) a través de múltiples criterios opcionales (nombre, categoría, precio mínimo y máximo) recibidos desde la capa Web.

Durante el análisis, se consideraron dos enfoques principales para construir consultas dinámicas en Spring Data JPA:
1. **`@Query` con HQL/SQL estático:** Usar una consulta estática manejando la opcionalidad con sentencias del tipo `(:param IS NULL OR campo = :param)`.
2. **JPA `Specification` API:** Construir la consulta de forma programática y condicional desde el código Java.

El enfoque `@Query` demostró problemas de compatibilidad con PostgreSQL al recibir valores nulos, ya que el motor de base de datos no lograba inferir el tipo de dato subyacente (`bytea` por defecto), requiriendo *casts* explícitos que ensuciaban el código (ej: `cast(:minPrice as big_decimal) IS NULL`).

## Decisiones

1. **Uso de JPA Specifications:** Se decidió utilizar la API `Specification` de Spring Data JPA para la construcción de consultas dinámicas con parámetros opcionales. Esto permite que los parámetros nulos simplemente no generen condiciones en el SQL final, delegando a Hibernate la construcción segura de la consulta y evitando conflictos de tipado en PostgreSQL.
2. **Extracción a Clases Utilitarias:** La lógica de construcción de los predicados (`.and(...)`, `.like(...)`, etc.) se extrajo de los adaptadores de persistencia (ej. `ProductRepositoryAdapter`) hacia clases dedicadas exclusivamente a esa responsabilidad (ej. `ProductSpecifications`).
3. **Mapeo Web Automático:** Los parámetros HTTP de búsqueda se mapean y validan automáticamente mediante el uso de `@Valid @ModelAttribute` en los controladores, inyectando un objeto de filtro tipado (ej. `ProductFilter`) hacia los servicios.
4. **YAGNI en Categorías:** Se determinó que las categorías, al ser una estructura de árbol pequeña que suele cargarse en memoria completa (ej. menús), no requieren filtros de búsqueda en base de datos. Se eliminó el código experimental de filtros para categorías aplicando el principio YAGNI (*You Aren't Gonna Need It*).

## Consecuencias

### Positivas
- **Responsabilidad Única (SRP):** El `RepositoryAdapter` se mantiene limpio, delegando la complejidad de la consulta a la clase `Specifications`.
- **Robustez:** Evita errores en tiempo de ejecución en PostgreSQL asociados al casteo de valores nulos.
- **Escalabilidad:** Añadir nuevos filtros al `ProductFilter` requiere cambios mínimos e incrementales en `ProductSpecifications`.

### Negativas / Trade-offs
- Ligera curva de aprendizaje y verbosidad adicional introducida por la sintaxis de JPA Criteria API (`cb.conjunction()`, `cb.like(...)`).
