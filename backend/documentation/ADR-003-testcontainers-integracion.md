# ADR 003: Uso de Testcontainers para Tests de Integración en lugar de Mocks

## Estado
Aceptado

## Contexto
Durante el desarrollo de la lógica de negocio (ej. borrado de categorías y propagación en el árbol), inicialmente los tests se construyeron utilizando `Mockito` para aislar los repositorios. Esto permitía validar la lógica en memoria, pero ocultaba comportamientos reales de la base de datos (PostgreSQL), tales como restricciones `NOT NULL` (ej. `created_at`, `price_buy`), triggers o errores de consultas nativas (`@Query`).

## Decisión
Se decidió abandonar el uso de Mocks para los flujos principales de negocio y en su lugar utilizar **Testcontainers** con una imagen real de PostgreSQL.
Los tests ahora se ejecutan levantando todo el contexto de Spring (`@SpringBootTest`) contra una base de datos efímera en Docker.

## Consecuencias
**Positivas:**
- Mayor confianza: Los tests descubren fallas reales de constraints de SQL y configuración de ORM que los mocks ocultarían.
- Entorno replicable: Se utiliza exactamente el mismo motor de base de datos que en producción (Postgres).

**Negativas:**
- Rendimiento: La ejecución de los tests requiere inicializar Docker y levantar el contexto de Spring Boot, lo cual toma más segundos que un test unitario tradicional.
- Requiere entorno con Docker funcional para ejecutar la build de Maven.
