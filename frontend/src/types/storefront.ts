// Tipos compartidos del Storefront

export interface StorefrontSection {
  priority: number;      // Orden de renderizado (1 = primero)
  type: string;          // Tipo lógico: "hero", "banner", "featuredProducts"
  component: string;     // Componente concreto: "hero1", "hero2", "banner1", etc.
}

export interface StorefrontConfig {
  tenantSlug: string;
  layoutType: string;
  sections: StorefrontSection[]; // Ya vienen del backend, se ordenan por priority en el frontend
}
