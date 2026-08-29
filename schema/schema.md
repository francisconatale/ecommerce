# 📖 API & Domain Contracts

Este documento está diseñado para ser **leído por humanos** (desarrolladores de Frontend, integradores, etc.). Aquí se describen las estructuras de datos y los endpoints disponibles en el Backend.

---

## 🏗 Entidades Principales (Modelos)

### 1. Categoría (Category)
Representa un nodo dentro del árbol de nuestro catálogo. Se modela para soportar anidamiento infinito.

| Campo | Tipo | Requerido | Descripción |
|---|---|---|---|
| \id\ | \UUID\ | Sí (Auto) | Identificador único de la categoría. |
| \
ame\ | \String\ | Sí | El nombre visible (ej: "Notebooks", "Electrónica"). |
| \parentId\ | \UUID\ | No | El ID de la categoría padre. Si es \
ull\, es una categoría principal (Raíz). |
| \pathNames\ | \String\ | Sí (Auto) | La ruta completa generada (ej: "Electrónica > Computación > Notebooks"). Útil para mostrar breadcrumbs. |
| \system\ | \Boolean\ | Sí (Auto) | Si es \	rue\, significa que es vital para el sistema y no se puede borrar. |
| \deleted\ | \Boolean\ | Sí (Auto) | Flag de borrado lógico (Soft Delete). |

### 2. Producto (Product)
Representa el ítem de inventario a la venta.

| Campo | Tipo | Requerido | Descripción |
|---|---|---|---|
| \id\ | \UUID\ | Sí (Auto) | Identificador único. |
| \
ame\ | \String\ | Sí | Nombre del producto. |
| \priceBuy\ | \Decimal\ | Sí | Precio de costo/compra. |
| \priceSell\| \Decimal\ | Sí | Precio de venta al público. |
| \categoryId\| \UUID\ | Sí | La categoría exacta donde está guardado el producto. |
| \deleted\ | \Boolean\ | Sí (Auto) | Flag de borrado lógico (Soft Delete). |

---

## 📡 Endpoints (REST API)

Todas las rutas parten desde \http://localhost:8080/api\

### 📂 Categorías

#### \GET /categories\
- **Qué hace:** Devuelve la lista completa de todas las categorías activas.
- **Respuesta (200 OK):** Un array de objetos \Category\.

#### \POST /categories\
- **Qué hace:** Crea una nueva categoría.
- **Body esperado (JSON):**
  \\\json
  {
    "name": "Muebles",
    "parentId": "opcional-uuid-del-padre"
  }
  \\\
- **Respuesta (200 OK):** Sin cuerpo (vacío). El backend se encarga de calcular el \pathNames\ y el árbol internamente.

#### \GET /categories/{id}/products\
- **Qué hace:** Devuelve todos los productos que pertenezcan a esta categoría **y a todos sus hijos** (recursivamente).
- **Respuesta (200 OK):** Un array de productos enriquecidos con su breadcrumb.
  \\\json
  [
    {
      "id": "uuid...",
      "name": "Silla Gamer",
      "priceBuy": 100.50,
      "priceSell": 250.00,
      "categoryId": "uuid-categoria",
      "categoryBreadcrumb": "Hogar > Muebles > Sillas"
    }
  ]
  \\\

### 📦 Productos (Asignaciones)

#### \PUT /products/{productId}/category/{categoryId}\
- **Qué hace:** Mueve o asigna un producto existente hacia una nueva categoría.
- **Respuesta (200 OK):** Vacío.