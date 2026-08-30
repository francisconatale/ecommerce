// Orquesta las secciones del storefront ordenadas por prioridad.
// Cada sección es opcional — si el backend no la incluye, no se renderiza.
// Para agregar una sección nueva: crear componente + registry + agregar el case aquí.

import { ComponentType } from 'react';
import { StorefrontConfig } from '@/types/storefront';
import { heroRegistry } from '@/components/heroes';
import { bannerRegistry } from '@/components/banners';
import { featuredProductsRegistry } from '@/components/featured-products';

// Registry global por tipo de sección
const sectionRegistries: Record<string, Record<string, ComponentType>> = {
  hero: heroRegistry,
  banner: bannerRegistry,
  featuredProducts: featuredProductsRegistry,
};

interface Props {
  config: StorefrontConfig;
}

export default function StorefrontComposer({ config }: Props) {
  // Ordenar secciones por prioridad ascendente (1 = primero)
  const sortedSections = [...config.sections].sort((a, b) => a.priority - b.priority);

  return (
    <div>
      {sortedSections.map((section) => {
        const registry = sectionRegistries[section.type];
        const SectionComponent = registry?.[section.component] ?? null;

        if (!SectionComponent) {
          // Sección configurada pero sin componente registrado — se ignora silenciosamente
          console.warn(
            `[StorefrontComposer] No component found for type="${section.type}" component="${section.component}"`
          );
          return null;
        }

        return <SectionComponent key={`${section.type}-${section.priority}`} />;
      })}
    </div>
  );
}
