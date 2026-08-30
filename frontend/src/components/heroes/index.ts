// Registry central de heroes — agregar nuevos heroes aquí sin tocar nada más
import { ComponentType } from 'react';
import Hero1 from './Hero1';
import Hero2 from './Hero2';

export const heroRegistry: Record<string, ComponentType> = {
  hero1: Hero1,
  hero2: Hero2,
};
