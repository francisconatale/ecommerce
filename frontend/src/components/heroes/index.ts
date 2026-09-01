import { ComponentType } from "react";
import Hero1 from "./Hero1";
import Hero2 from "./Hero2";

export const heroVariants: Record<string, ComponentType> = {
  "hero-1": Hero1,
  "hero-2": Hero2,
};
