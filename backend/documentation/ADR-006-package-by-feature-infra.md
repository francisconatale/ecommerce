# ADR 006: Empaquetado por Feature en la capa de Infraestructura

## Estado
Aceptado

## Contexto
A medida que el proyecto fue creciendo, las carpetas de la capa de infraestructura (`infrastructure/persistence` y `infrastructure/web`) comenzaron a llenarse de clases correspondientes a distintos dominios (Categorías, Productos, Base, etc.). 

Si manteníamos un empaquetado estrictamente "por capa" (Package by Layer), todas las entidades JPA, repositorios Spring Data y controladores de todos los módulos terminarían mezclados en las mismas dos carpetas. Esto produce baja cohesión, dificulta la navegación del código, propaga el acoplamiento involuntario y empeora drásticamente la escalabilidad a medida que se suman nuevos módulos al MVP (Usuarios, Carritos, Órdenes).

## Decisión
Se decidió organizar el código interno de las capas de infraestructura utilizando **Package by Feature** (Empaquetado por Módulo/Característica), alineándolo con la estructura que ya posee la capa de `domain`.

La nueva estructura establece que, dentro de `persistence` y `web`, se crearán subpaquetes específicos para cada módulo del dominio:
- `infrastructure/persistence/category/`
- `infrastructure/persistence/product/`
- `infrastructure/web/category/`
- `infrastructure/web/product/`

## Consecuencias
**Positivas:**
- **Alta Cohesión**: Todos los detalles de infraestructura de una funcionalidad específica (ej. Producto) residen en su propio paquete.
- **Mejor Encapsulamiento**: Permite usar visibilidad `package-private` (default en Java) para clases internas de infraestructura (como los repositorios de Spring Data o los Entity), forzando el acceso a través del Adapter o Controller público.
- **Escalabilidad**: Evita cuellos de botella organizativos; crear nuevos módulos es tan simple como crear nuevas carpetas sin afectar las existentes.
- **Navegación**: Facilita a los desarrolladores ubicar dónde hacer modificaciones relacionadas a un feature de punta a punta.

**Negativas:**
- Puede haber un poco más de estructura de carpetas anidada, aunque compensa ampliamente en orden y mantenibilidad.
- Obliga a mover clases y corregir imports si un módulo de infraestructura se extrae incorrectamente en el paquete global en lugar de su paquete de feature.
