package com.user.controller;

import com.user.dto.CreateUserRequest;
import com.user.dto.UpdateUserRequest;
import com.user.entity.Users;
import com.user.exception.UserErrorCode;
import com.user.exception.UserException;
import com.user.repository.UserRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/{version}")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String test() {
        return "This is a test from UserController";
    }

    @GetMapping(value = "/users", version = "v1")
    public ResponseEntity<List<Users>> getAllUsers() {
        List<Users> all = userRepository.findAll();
        return ResponseEntity.ok(new ArrayList<>(all));
    }

    @GetMapping(value = "/users/{id}", version = "v1")
    public ResponseEntity<Users> getUserById(@PathVariable UUID id) {
        Users user = userRepository.findById(id).orElseThrow(() -> new UserException(UserErrorCode.NOT_EXISTS));
        return ResponseEntity.ok(user);
    }

    @PostMapping(value = "/users", version = "v1")
    @Transactional
    public ResponseEntity<Void> createUser(@RequestBody @Valid CreateUserRequest request) {
        userRepository.save(new Users(request));
        return ResponseEntity.ok(null);
    }

    @PutMapping(value = "/users/{id}", version = "v1")
    @Transactional
    public ResponseEntity<Void> updateUserById(@PathVariable UUID id, @RequestBody @Valid UpdateUserRequest request) {
        Users user = userRepository.findById(id).orElseThrow(() -> new UserException(UserErrorCode.NOT_EXISTS));
        user.updateUser(request);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping(value = "/users/{id}", version = "v1")
    @Transactional
    public ResponseEntity<Void> deleteUserById(@PathVariable UUID id) {
        userRepository.deleteById(id);
        return ResponseEntity.ok(null);
    }
}
