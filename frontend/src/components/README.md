# Component Architecture

Mantenemos una estructura de carpetas organizada para separar responsabilidades y facilitar la escalabilidad del frontend.

## Estructura

- **/ui**: Componentes base, genéricos y altamente reutilizables. No están atados a la lógica de negocio ni a los datos (ej. `Button.tsx`, `ProductCard.tsx`, `HeroSection.tsx`). Reciben toda su información a través de `props`.
- **/layout**: Componentes estructurales globales que envuelven la aplicación (ej. `Header.tsx`, `Footer.tsx`, `Sidebar.tsx`).
- **/heroes**: Implementaciones específicas de secciones tipo Hero/Banner (ej. `Hero1.tsx`). Se encargan de conectar los componentes `/ui` con los datos reales de configuración (como `siteContent.ts`) o interactividad específica.
- **/featured-products**: Implementaciones específicas para los grids o sliders de productos destacados.
- **/banners**: Implementaciones específicas de banners promocionales intermedios.

## Reglas
1. Los componentes en `/ui` **no** deben importar configuración (como `siteContent.ts`). Son puros (dumb components).
2. Las implementaciones en carpetas de secciones (`/heroes`, `/featured-products`) actúan como "Smart Components". Ellos importan los datos (`siteContent.ts`) y se los pasan a los componentes `/ui`.
