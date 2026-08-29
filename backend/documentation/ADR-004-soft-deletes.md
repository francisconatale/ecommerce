# ADR 004: Soft Deletes (Borrado Lógico) para Entidades Core

## Estado
Aceptado

## Contexto
En un e-commerce, el borrado físico (DELETE) de entidades principales como `Product` o `Category` puede ocasionar pérdida de historial, ruptura de integridad referencial en reportes de ventas, o problemas en facturas pasadas que referencian a un producto eliminado.

## Decisión
Se implementó un mecanismo de **Borrado Lógico (Soft Delete)**.
- Se agregó el flag booleano `deleted` a las entidades principales (`CategoryEntity`, `ProductEntity`).
- Al realizar la acción de borrado en el sistema, en lugar de un `DELETE` en SQL, se hace un `UPDATE` marcando `deleted = true`.
- Las consultas activas (ej. mostrar productos al cliente) deben filtrarse con una cláusula `WHERE deleted = false`.

## Consecuencias
**Positivas:**
- Preservación de datos históricos e integridad para facturas y reportes.
- Facilidad para "deshacer" eliminaciones accidentales si se requiere soporte técnico.

**Negativas:**
- Todas las consultas de lectura deben recordar incluir el filtro `deleted = false` para no exponer datos eliminados (o usar una feature de Hibernate como `@Where`/`@Filter`).
