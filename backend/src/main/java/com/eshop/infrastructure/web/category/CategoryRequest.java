package com.eshop.infrastructure.web.category;

import java.util.UUID;

public record CategoryRequest(String name, UUID parentId) {}