package com.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record CreateUserRequest(
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotBlank @Length(min = 10, max = 10) String phone
) {}
