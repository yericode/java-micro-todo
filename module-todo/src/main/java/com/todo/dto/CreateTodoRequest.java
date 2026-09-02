package com.todo.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record CreateTodoRequest(
    @NotBlank @Length(min = 1, max = 200) String content
) {}
