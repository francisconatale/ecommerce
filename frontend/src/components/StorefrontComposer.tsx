import { heroVariants } from "./heroes";
import { featuredProductsVariants } from "./featured-products";

// A global registry mapping section types to their respective variants registry
const globalRegistry: Record<string, any> = {
  hero: heroVariants,
  featuredProducts: featuredProductsVariants,
};

export default function StorefrontComposer({ layout }: { layout: any[] }) {
  return (
    <>
      {layout.map((section) => {
        // Find the specific registry for this section type (e.g. 'hero' or 'featuredProducts')
        const sectionRegistry = globalRegistry[section.type];
        
        if (!sectionRegistry) {
          console.warn(`No registry found for section type: ${section.type}`);
          return null;
        }

        // Determine which variant to render. If no variant specified, fallback to the type name itself.
        const variantKey = section.variant || section.type;
        const Component = sectionRegistry[variantKey];

        if (!Component) {
          console.warn(`No component found for variant: ${variantKey}`);
          return null;
        }

        // Render the component dynamically, passing its config payload!
        return <Component key={section.id} config={section.config} />;
      })}
    </>
  );
}
