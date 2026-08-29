# ADR 001: Implementación de Arquitectura Hexagonal y Patrón Adapter

## Estado
Aceptado

## Contexto
El proyecto busca mantener una capa de Dominio pura que contenga exclusivamente lógica de negocio (`Category`, `Product`), sin acoplarse a frameworks externos, bases de datos o librerías de infraestructura como Spring Boot o Hibernate.

Sin embargo, para persistir los datos, necesitamos utilizar Spring Data JPA (`@Entity`, `@Table`, `JpaRepository`), lo cual requiere un acoplamiento fuerte con el framework. Inyectar directamente los repositorios de Spring Data en los servicios de dominio violaría el principio de inversión de dependencias y el aislamiento del dominio.

## Decisión
Hemos decidido utilizar **Arquitectura Hexagonal (Puertos y Adaptadores)**. 
- **Puertos**: El dominio definirá interfaces puras en Java (ej. `CategoryRepository`).
- **Adaptadores**: En la capa de infraestructura crearemos clases (ej. `CategoryRepositoryAdapter`) anotadas con `@Component` que implementarán las interfaces del dominio.

El Adapter será responsable de:
1. Recibir objetos del dominio.
2. Mapearlos a Entidades JPA (`CategoryEntity`).
3. Delegar la persistencia al repositorio real de Spring Data (`SpringDataCategoryRepository`).
4. Mapear la respuesta de vuelta a objetos puros del dominio.

## Consecuencias
**Positivas:**
- El código de dominio queda 100% aislado y puede ser testeado unitariamente (si se desea) sin levantar contextos de Spring.
- Si en el futuro cambiamos de base de datos o de ORM, la capa de dominio no sufre ningún cambio.
- Obliga a los desarrolladores (y a la IA) a mantener una separación estricta de responsabilidades.

**Negativas:**
- Mayor verbosidad: requiere crear y mantener clases de mapeo (`toEntity`, `toDomain`) y duplicar modelos (Modelo de Dominio vs Modelo de Persistencia).
