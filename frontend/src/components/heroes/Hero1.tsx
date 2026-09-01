"use client";

import { HeroSection } from "@/components/ui/HeroSection";
import { siteContent } from "@/config/siteContent";

export default function Hero1({ config }: { config: any }) {
  const { title, description, image, cta, align = 'center' } = config;

  return (
    <div className="w-full flex flex-col items-center">
      <HeroSection
        title={title}
        description={description}
        image={image}
        cta={cta}
        align={align}
      />
    </div>
  );
}
