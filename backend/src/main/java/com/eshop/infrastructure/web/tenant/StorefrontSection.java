package com.eshop.infrastructure.web.tenant;

public record StorefrontSection(
        int priority,   // Orden de renderizado (1 = primero)
        String type,    // Tipo lÃ³gico: "hero", "banner", "featuredProducts", etc.
        String component // Componente concreto: "hero1", "hero2", "banner1", etc.
) {}
