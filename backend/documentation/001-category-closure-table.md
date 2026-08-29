# Feature Breakdown: 001-category-closure-table

**Date:** 2026-08-29
**Feature:** Modelo de Categorías Jerárquicas con Closure Table

## Resumen del Trabajo
- Se generó la especificación funcional y el plan de implementación basándonos en un requerimiento técnico provisto por el usuario.
- Se detalló el flujo de borrado de categorías intermedias.
- Se refinó la estrategia de lectura de categorías (Breadcrumbs) para evitar problemas de rendimiento (N+1) típicos en ORMs.

## Decisiones Tomadas por el Usuario
- **Modelo de Árbol (Fuente de la Verdad):** Uso del patrón Closure Table como el "motor matemático" del catálogo. Este modelo es estrictamente necesario para tres operaciones críticas:
  1. **Búsquedas "Hacia Abajo":** Obtener todos los productos de un nodo raíz (ej: "Tecnología") y de todos sus descendientes en una sola query eficiente.
  2. **Integridad Estructural:** Prevenir ciclos (bucles infinitos) al mover categorías y limitar la profundidad máxima a 10 niveles.
  3. **Mantenimiento:** Recalcular subárboles completos cuando se mueven o eliminan nodos.
- **Optimización de Lectura (Caché / Desnormalización):** Para solucionar el problema de N+1 al mostrar listados masivos de productos con sus respectivos *breadcrumbs* (rutas de categorías como `Tecnología > Computadora > Laptop`), se decidió agregar una columna desnormalizada `path_names` a la tabla `category`. Esta columna actúa exclusivamente como un caché de lectura.
- **Asignación de Productos:** Un producto siempre debe apuntar a la categoría más especializada (hoja del árbol) al momento de ser creado o editado.
- **Borrado de Categorías (Resolución de Gap):** Si se borra una categoría, sus productos y subcategorías suben al padre directo. Aunque esto pueda causar que una categoría intermedia contenga productos temporalmente (herencia de borrado), se acepta como un fallback seguro para evitar forzar reasignaciones ilógicas a categorías hermanas.

## Razonamiento
- **Separación de responsabilidades entre Escritura y Lectura:** El árbol de categorías se edita muy rara vez (escritura), pero los productos se consultan constantemente (lectura). 
  - La **Closure Table** asume el costo de procesamiento durante la escritura (inserts/updates complejos) para garantizar la integridad de los datos.
  - La columna **`path_names` (Caché)** permite que el frontend renderice el texto de la categoría al instante con costo O(1) y sin realizar JOINs adicionales o queries N+1, asumiendo el costo de actualizarse transaccionalmente solo cuando una categoría cambia de nombre o de lugar.
- **Herencia en el borrado:** Permitir que el nodo padre herede los productos preserva la jerarquía semántica original, resultando en una mejor experiencia de usuario y lógica de negocio más predecible.
