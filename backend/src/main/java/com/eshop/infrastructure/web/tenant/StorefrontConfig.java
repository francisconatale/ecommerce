package com.eshop.infrastructure.web.tenant;

import java.util.List;

public record StorefrontConfig(
        String tenantSlug,
        String layoutType,
        List<StorefrontSection> sections
) {}
