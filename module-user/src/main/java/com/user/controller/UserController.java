package com.user.controller;

import com.user.dto.CreateUserRequest;
import com.user.dto.GetUserTodosResponse;
import com.user.dto.TodoDto;
import com.user.dto.UpdateUserRequest;
import com.user.entity.Users;
import com.user.exception.UserErrorCode;
import com.user.exception.UserException;
import com.user.repository.UserRepository;
import jakarta.validation.Valid;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/{version}")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${services.todo-service.uri}")
    private String todoServiceUri;

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

    @GetMapping(value = "/users/{id}", version = "v1")
    public ResponseEntity<GetUserTodosResponse> getUserTodos(@PathVariable UUID id) throws IOException, InterruptedException {
        Users user = userRepository.findById(id).orElseThrow(() -> new UserException(UserErrorCode.NOT_EXISTS));

        String body = "";
        if (StringUtils.hasText(todoServiceUri)) {
            String url = todoServiceUri + "/api/v1/todos";
            HttpClient http = HttpClient.newHttpClient();
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(url)).build();
            HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
            body = res.body();
            log.info("TodoService Response: {}, {}", res.statusCode(), body);
        } else {
            throw new UserException(UserErrorCode.INVALID_INPUT);
        }

        List<TodoDto> todos = new ArrayList<>();
        if (StringUtils.hasText(body)) {
            todos = objectMapper.readValue(body, new TypeReference<List<TodoDto>>() {});
        } else {
            throw new UserException(UserErrorCode.NOT_EXISTS);
        }

        return ResponseEntity.ok(new GetUserTodosResponse(user, todos));
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
