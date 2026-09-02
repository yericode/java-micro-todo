package com.user.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateUserRequest(
        @NotBlank String name,
        @NotBlank String email,
        @NotBlank String phone
) { }
