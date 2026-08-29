# E-Shop MVP - Backlog y Próximos Pasos

Este documento sirve como mapa de ruta para evaluar y priorizar las siguientes funcionalidades del MVP, una vez completada la estructura base del catálogo (Categorías y Closure Table).

## Opción 1: Gestión Avanzada de Productos (Product Catalog)
**Prioridad sugerida:** Alta
**Complejidad:** Media
* **ABM/CRUD Completo:** Endpoints para crear, editar y eliminar (soft-delete) productos.
* **Búsqueda y Filtrado:** Endpoint de búsqueda (`GET /products`) que permita filtrar por nombre, rango de precio, y por categoría (aprovechando la Closure Table para traer todos los productos de un sub-árbol).
* **Control de Inventario Básico:** Agregar un campo `stock` y lógica para no permitir añadir al carrito si el stock es cero.

## Opción 2: Carrito de Compras y Órdenes (Core Transaccional)
**Prioridad sugerida:** Alta
**Complejidad:** Alta
* **Carrito Temporal:** Creación de una sesión de carrito (puede ser guardada en la base de datos o en memoria) para que el usuario añada `OrderItems`.
* **Checkout (Generación de Orden):** Convertir el carrito en una entidad `Order` (con estado `PENDING`, `PAID`, `SHIPPED`).
* **Descuento de Stock:** Al confirmar la orden, restar transaccionalmente el stock de los productos involucrados.

## Opción 3: Usuarios, Roles y Seguridad (Auth)
**Prioridad sugerida:** Media / Alta
**Complejidad:** Media
* **Entidad User:** Crear la tabla de usuarios con campos básicos (email, password_hash, rol).
* **Autenticación (JWT):** Implementar Spring Security para emitir y validar tokens JWT.
* **Restricción de Accesos:** 
  * Rol `ADMIN`: Puede modificar categorías y productos.
  * Rol `CUSTOMER`: Puede ver el catálogo y crear órdenes.

## Opción 4: Medios de Pago y Envíos (Integraciones)
**Prioridad sugerida:** Baja (Post-MVP)
**Complejidad:** Alta
* Integración con pasarela de pagos (ej: Stripe o MercadoPago).
* Cálculos de costo de envío basados en la ubicación del `User`.

---
*Nota: Para iniciar cualquiera de estos módulos, se recomienda utilizar las herramientas de especificación (`speckit-specify` o `/plan`) para generar el diseño detallado antes de codificar.*
