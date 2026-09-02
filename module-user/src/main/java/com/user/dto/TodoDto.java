package com.user.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record TodoDto(
    UUID id,
    String content,
    LocalDateTime createdAt
) {}
