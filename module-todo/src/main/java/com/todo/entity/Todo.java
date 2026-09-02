package com.todo.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "todo")
public class Todo {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    private UUID id;
    private UUID userId;
    private String content;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Todo() {
    }

    public Todo(String content) {
        this.content = content;
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void updateContent(String content) {
        this.content = content != null ? content : "";
    }

}
