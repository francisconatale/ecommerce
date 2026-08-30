import { ComponentType } from 'react';
import FeaturedProducts from './FeaturedProducts';

export const featuredProductsRegistry: Record<string, ComponentType> = {
  featuredProducts: FeaturedProducts,
};
