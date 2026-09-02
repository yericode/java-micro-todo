package com.todo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

import org.hibernate.validator.constraints.Length;

public record CreateTodoRequest(
    @NotNull UUID userId,
    @NotBlank @Length(min = 1, max = 200) String content
) {}
