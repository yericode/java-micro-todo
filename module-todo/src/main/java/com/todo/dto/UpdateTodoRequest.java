package com.todo.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateTodoRequest(
    @NotBlank String content
) {}
