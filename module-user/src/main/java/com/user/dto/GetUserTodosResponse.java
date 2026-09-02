package com.user.dto;

import java.util.List;
import java.util.UUID;

import com.user.entity.Users;

public record GetUserTodosResponse(
        UUID userId,
        String name,
        String email,
        String phone,
        List<TodoDto> todos
) {
    public GetUserTodosResponse(Users user, List<TodoDto> todos) {
        this(user.getId(), user.getName(), user.getEmail(), user.getPhone(), todos);
    }
}
