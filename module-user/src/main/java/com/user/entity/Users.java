package com.user.entity;

import com.user.dto.CreateUserRequest;
import com.user.dto.UpdateUserRequest;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "users")
public class Users {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    private UUID id;

    private String name;
    private String email;
    private String phone;

    public Users() {
    }

    public Users(CreateUserRequest request) {
        this.name = request.name();
        this.email = request.email();
        this.phone = request.phone();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public void updateUser(UpdateUserRequest request) {
        this.name = request.name();
        this.email = request.email();
        this.phone = request.phone();
    }
}
