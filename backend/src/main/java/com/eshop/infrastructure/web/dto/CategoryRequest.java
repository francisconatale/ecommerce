package com.eshop.infrastructure.web.dto;

import java.util.UUID;

public record CategoryRequest(String name, UUID parentId) {}