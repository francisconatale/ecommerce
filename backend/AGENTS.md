# Reglas de Proyecto: E-Shop Backend

## 1. Estilo de Código Java (Imports)
- **NUNCA** utilices nombres de clases completamente cualificados (fully qualified names) inline dentro del código (ej. `java.util.Objects.equals`).
- **SIEMPRE** importa las clases correctamente al inicio del archivo (ej. `import java.util.Objects;`).

## 2. Arquitectura y Domain-Driven Design (DDD)
- **Modelos de Dominio Ricos:** Evita el "Modelo de Dominio Anémico". No dependas de setters desde los servicios para modificar entidades.
- **Encapsulamiento:** Las entidades de dominio deben exponer métodos de negocio expresivos (ej. `updateDetails`, `assignToCategory`) que representen acciones reales.
- **Protección de Invariantes:** El dominio debe proteger sus propias reglas e invariantes de forma autónoma. No dependas de la capa web o de Spring Boot (`@NotNull`, etc.) para validar el estado de las entidades. Lanza excepciones explícitas dentro de las propias entidades si se intentan crear o actualizar con estados inválidos (ej. nulls donde no corresponden).
