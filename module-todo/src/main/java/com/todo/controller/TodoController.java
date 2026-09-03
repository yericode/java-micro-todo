package com.todo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.common.exception.CommonErrorCode;
import com.common.exception.CommonException;
import com.todo.exception.TodoErrorCode;
import com.todo.exception.TodoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.todo.dto.CreateTodoRequest;
import com.todo.dto.UpdateTodoRequest;
import com.todo.entity.Todo;
import com.todo.repository.TodoRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/{version}")
public class TodoController {

    private static final Logger log = LoggerFactory.getLogger(TodoController.class);

    @Autowired
    private TodoRepository todoRepository;

    @GetMapping("/")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("This is a test from TodoController");
    }

    @GetMapping(value = "/todos", version = "v1")
    public ResponseEntity<List<Todo>> getAllTodosByUserId(@RequestParam(name = "user", required = false) UUID userId) {
        List<Todo> todos = new ArrayList<>();
        if (userId != null) {
            todos = todoRepository.findAllByUserId(userId);
        }
        return ResponseEntity.ok(new ArrayList<>(todos));
    }

    @GetMapping(value = "/todos/{id}", version = "v1")
    public ResponseEntity<Todo> getTodoById(@PathVariable UUID id) {
        Todo todo = todoRepository.findById(id).orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND, "查無內容"));
        return ResponseEntity.ok(todo);
    }

    @PostMapping(value = "/todos", version = "v1")
    @Transactional
    public ResponseEntity<Void> createTodo(@RequestBody @Valid CreateTodoRequest request) {
        todoRepository.save(new Todo(request));
        return ResponseEntity.ok(null);
    }

    @PutMapping(value = "/todos/{id}", version = "v1")
    @Transactional
    public ResponseEntity<Void> updateTodoById(@PathVariable UUID id, @RequestBody @Valid UpdateTodoRequest request) {
        Todo todo = todoRepository.findById(id).orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND, "查無內容"));
        todo.updateContent(request.content());
        return ResponseEntity.ok(null);
    }

    @DeleteMapping(value = "/todos/{id}", version = "v1")
    @Transactional
    public ResponseEntity<Void> deleteTodoById(@PathVariable UUID id) {
        todoRepository.deleteById(id);
        return ResponseEntity.ok(null);
    }
}
