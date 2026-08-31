# ADR 009: Modelado y Separación de Mapeos (Domain vs Entity)

## Estado
Aceptado

## Contexto
Siguiendo la Arquitectura Hexagonal (definida en el ADR 002), existe una separación estricta entre los objetos del Dominio (ej. `Category`, `Product`, que contienen lógica pura de negocio) y los objetos de la Infraestructura (ej. `CategoryEntity`, `ProductEntity`, que contienen detalles de base de datos como anotaciones JPA, `createdAt`, `updatedAt`).

Debido a esta separación, es imperativo "traducir" los datos entre ambas capas mediante funciones de mapeo (`toDomain` y `toEntity`). Inicialmente, esta lógica de traducción se implementó como métodos privados dentro de los propios Adapters de persistencia (ej. `CategoryRepositoryAdapter`). 

A medida que las entidades crecen en complejidad, dejar esta lógica de mapeo acoplada como métodos privados dentro del Adapter genera:
1. **Falta de Cohesión (Rotura del SRP):** El Adapter termina asumiendo dos grandes responsabilidades: la orquestación de la persistencia (llamar a repositorios Spring Data) y la minuciosa transformación campo por campo de los datos.
2. **Propensión a errores:** Lógicas sutiles de mapeo se entrelazan y causan bugs. (Por ejemplo: forzar la creación de un UUID manual en el mapper provocaba que el estado `isNew()` de Spring Data fallara, causando excepciones `StaleObjectStateException` en los inserts).
3. **Difícil Testeo:** No se puede probar la lógica de mapeo de forma aislada sin levantar o instanciar el Adapter completo.

## Decisión
Se decide establecer una política explícita sobre **cómo y por qué modelamos la traducción entre Dominio y Entidades**, documentando la necesidad de delegar esta responsabilidad:

1. **La justificación (El "Por qué"):** Se mantiene la duplicidad entre *Domain* y *Entity* para evitar que el framework (JPA/Hibernate) contamine las reglas de negocio. Esto nos permite evolucionar el esquema de base de datos sin afectar el core, y viceversa.
2. **Separación de Responsabilidades:** El mapeo debe dejar de ser un detalle oculto en métodos privados kilométricos dentro del Adapter. El Adapter solo debe orquestar, delegando la traducción.
3. **Estrategias de Modelado Aceptadas:** Toda la lógica de transformación (`toDomain`, `toEntity`) deberá encapsularse en una de las siguientes aproximaciones:
   - **Factory Methods en la Infraestructura:** Dado que el Entity pertenece a infraestructura, está permitido que conozca el Dominio. Se pueden usar métodos estáticos (ej. `CategoryEntity.fromDomain()`) y de instancia (`entity.toDomain()`).
   - **Clases Mapper Dedicadas (Componentes):** Extraer la lógica a clases independientes (ej. `CategoryMapper`) que el Adapter consuma por inyección de dependencias, facilitando el testeo unitario.
   - *(Nota: Si la complejidad del mapeo a futuro es muy alta, se adoptará la librería estandarizada **MapStruct** para auto-generar este código).*
4. **Respeto al Ciclo de Vida del Framework:** Los mappers no deben interferir destructivamente con el comportamiento de Spring Data/JPA. Si un Entity genera su ID nativamente (`@GeneratedValue`), el mapper debe transferir `null` o vacíos correspondientemente en entidades nuevas para no falsificar el estado de desvinculación (detached).

## Consecuencias
**Positivas:**
- Adapters mucho más limpios, pequeños, legibles y enfocados únicamente en persistencia.
- Los errores de mapeo (especialmente los vinculados a IDs y JPA) quedan encapsulados y son más fáciles de detectar y depurar.
- La traducción de datos puede probarse de forma 100% unitaria.

**Negativas:**
- Mayor carga estructural: requiere formalizar el mapeo agregando métodos extras o componentes de infraestructura, pero el trade-off en mantenibilidad y reducción de bugs lo justifica enteramente.
