# ADR 010: Interfaz Genérica para Mapeo de Entidades (EntityMapper)

## Estado
Aceptado

## Contexto
Tras la adopción del ADR-009, que establece la extracción de la lógica de mapeo a componentes dedicados, se identificó un patrón fuertemente repetitivo en los Adapters: la conversión de colecciones (Listas) y envoltorios (`Optional`).

Continuamente, al recuperar datos de Spring Data JPA, el Adapter debía ocuparse de transformar estructuras de datos enteras escribiendo código repetitivo como:
```java
return repository.findAll().stream()
        .map(mapper::toDomain)
        .collect(Collectors.toList());
```
O para métodos de búsqueda única:
```java
return repository.findById(id).map(mapper::toDomain);
```
Esta repetición (boilerplate) ensuciaba la legibilidad de los Adapters, obligándolos a gestionar la manipulación de flujos (`Streams`) o transformaciones de `Optional` directamente en su capa.

## Decisión
Se decide introducir una interfaz genérica `EntityMapper<D, E>` (donde `D` es el Dominio y `E` es la Entidad) en el paquete `com.eshop.infrastructure.persistence.base`.

Esta interfaz define los contratos básicos de transformación simple que todo Mapper debe implementar:
- `D toDomain(E entity);`
- `E toEntity(D domain, E existingEntity);`

Y, de manera crucial, aporta **default methods** (métodos por defecto de Java) estandarizados para manejar colecciones y Optionals de forma unificada:
- `default List<D> toDomain(List<E> entities)`
- `default Optional<D> toDomain(Optional<E> entityOptional)`

Adicionalmente, se establece como regla de estilo que las clases e interfaces de persistencia deben utilizar importaciones relativas/estándar (`import java.util.List;`) en lugar de nombres de clase totalmente cualificados (literales) en las firmas de los métodos, para favorecer la legibilidad.

## Consecuencias
**Positivas:**
- **Eliminación de Boilerplate:** Los Adapters ahora pueden invocar de forma limpia `mapper.toDomain(repository.findAll())` o `mapper.toDomain(repository.findById(id))`, ocultando por completo la fontanería de Streams y Optional.
- **Uniformidad Absoluta:** Todas las entidades del sistema se mapean bajo exactamente el mismo contrato predecible, independientemente de quién escriba el Mapper.
- **Centralización de lógica de seguridad:** La comprobación de colecciones nulas o retornos vacíos se controla centralmente dentro de los `default methods` de la interfaz.

**Negativas:**
- Agrega un ligero nivel de indirección debido al uso de interfaces con tipos genéricos.
