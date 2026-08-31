# ADR 011: Generación de Mappers con MapStruct y Ciclo de Vida JPA

## Estado
Aceptado

## Contexto
Durante el proceso de refactorización de la capa de persistencia (siguiendo los lineamientos de los ADR-009 y ADR-010), nos encontramos con dos problemas fundamentales:
1. **Boilerplate en los Mappers:** Escribir y mantener el código manual para transferir datos campo por campo entre el Dominio y las Entidades es tedioso, repetitivo y sumamente propenso a errores humanos (ej. olvidar mapear un campo nuevo).
2. **Conflictos con el ciclo de vida de JPA (Hibernate):** En Arquitectura Hexagonal, el caso de uso envía un objeto de Dominio al Adapter para ser guardado. Si el Adapter simplemente crea un nuevo `Entity` con el ID existente para hacer un *update*, Spring Data (a través de `merge()`) sobrescribirá con valores nulos aquellos campos técnicos que solo existen en la BD (como `createdAt` o `updatedAt`). Además, descubrimos que si generamos los UUIDs manualmente antes de insertar un registro nuevo, JPA asume erróneamente que la entidad ya existe en la BD (falla el `isNew()`), provocando excepciones de concurrencia (`ObjectOptimisticLockingFailureException`).

## Decisión
Para resolver estos problemas estructurales de forma definitiva, adoptamos las siguientes directrices:

### 1. Adopción de MapStruct
Se incorpora **MapStruct** como librería estándar en el proyecto para la generación automática de los Mappers en tiempo de compilación. 
Los Mappers manuales quedan deprecados. A partir de ahora, todo Mapper será una interfaz delegada a MapStruct (`@Mapper(componentModel = "spring")`).

### 2. Manejo de Actualizaciones Seguras (`@MappingTarget`)
Para evitar la pérdida de campos de infraestructura durante las actualizaciones, es obligatorio que los Mappers definan un método de actualización *in-place*:
`void updateEntity(D domain, @MappingTarget E entity);`
El Adapter **debe** recuperar primero la entidad existente de la base de datos (con todos sus campos técnicos intactos) y utilizar este método para que MapStruct solo sobrescriba los campos de negocio.

### 3. Delegación de IDs y la bandera `isNew()`
- **Creación:** Queda estrictamente prohibido generar UUIDs manualmente para entidades nuevas antes de guardarlas. El `id` debe permanecer en `null` para que el método `save()` de Spring Data detecte correctamente que es una inserción y genere el ID en la base de datos.
- **Bandera Semántica:** Se agrega el método `public boolean isNew() { return id == null; }` a la clase `BaseEntity` del Dominio. 
- **Flujo Funcional en el Adapter:** Los Adapters deben utilizar un enfoque declarativo y seguro para guardar, apoyándose en la bandera `isNew()`:
  - Si es nuevo: Crea el Entity desde cero con MapStruct.
  - Si no es nuevo: Busca el Entity por ID y lo actualiza usando `@MappingTarget`. En caso de no encontrarlo (fallback), crea uno nuevo.

## Consecuencias
**Positivas:**
- Cero código de mapeo manual; MapStruct garantiza transferencias de datos exactas y ultrarrápidas.
- Solución definitiva a los errores de `OptimisticLocking` y pérdida de datos en columnas de auditoría (`createdAt`).
- Lógica de los Adapters estandarizada, funcional y libre de condicionales con variables nulas.

**Negativas:**
- Agrega una nueva dependencia al proyecto (MapStruct) y requiere configurar el plugin del compilador en Maven.
- Requiere que los desarrolladores entiendan el flujo de "búsqueda previa antes de actualizar" dictado por la Arquitectura Hexagonal.
