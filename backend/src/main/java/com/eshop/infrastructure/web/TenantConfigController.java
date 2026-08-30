package com.eshop.infrastructure.web;

import com.eshop.infrastructure.web.dto.StorefrontConfig;
import com.eshop.infrastructure.web.dto.StorefrontSection;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/tenants")
public class TenantConfigController {

    // Hardcoded map for ad-hoc MVP testing
    // tenant-a: hero primero, luego banner, luego productos destacados
    // tenant-b: banner primero, luego hero distinto
    private final Map<String, StorefrontConfig> mockTenants = Map.of(
            "tenant-a", new StorefrontConfig(
                    "tenant-a",
                    "default",
                    List.of(
                            new StorefrontSection(1, "hero", "hero1"),
                            new StorefrontSection(2, "banner", "banner1"),
                            new StorefrontSection(3, "featuredProducts", "featuredProducts")
                    )
            ),
            "tenant-b", new StorefrontConfig(
                    "tenant-b",
                    "default",
                    List.of(
                            new StorefrontSection(1, "banner", "banner1"),
                            new StorefrontSection(2, "hero", "hero2"),
                            new StorefrontSection(3, "featuredProducts", "featuredProducts")
                    )
            )
    );

    @GetMapping("/{tenantSlug}/storefront")
    public ResponseEntity<StorefrontConfig> getStorefrontConfig(@PathVariable String tenantSlug) {
        StorefrontConfig config = mockTenants.get(tenantSlug);
        if (config == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(config);
    }

    @GetMapping("/allowed")
    public ResponseEntity<List<String>> getAllowedRoutes() {
        return ResponseEntity.ok(mockTenants.keySet().stream().toList());
    }
}
