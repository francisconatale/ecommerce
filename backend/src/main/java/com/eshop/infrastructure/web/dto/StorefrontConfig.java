package com.eshop.infrastructure.web.dto;

import java.util.List;

public record StorefrontConfig(
        String tenantSlug,
        String layoutType,
        List<StorefrontSection> sections
) {}
