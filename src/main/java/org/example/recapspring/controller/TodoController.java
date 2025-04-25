package org.example.recapspring.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.example.recapspring.dto.TodoDto;
import org.example.recapspring.model.Todo;
import org.example.recapspring.service.TodoService;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/todo")
public class TodoController {
    
    private final TodoService service;
    
    public TodoController( TodoService service ) {
        this.service = service;
    }
    
    @GetMapping
    public List<Todo> getAllTodos() {
        return service.getAllTodos();
    }
    
    @GetMapping("/{id}")
    public Optional<Todo> getTodoById( @PathVariable String id, HttpServletResponse response ) {
        Todo foundTodo = service.getTodoById( id );
        if ( foundTodo == null ) {
            response.setStatus( 404 );
        } else {
            response.setStatus( 200 );
        }
        return Optional.ofNullable( foundTodo );
    }
    
    @PostMapping
    public Todo createTodo( @RequestBody TodoDto todoDto ) {
        return service.createTodo( todoDto );
    }
    
    @PutMapping("/{id}")
    public void updateTodo( @PathVariable String id, @RequestBody TodoDto todoDto ) {
        service.updateTodo( id, todoDto );
    }
    
    @DeleteMapping("/{id}")
    public void deleteTodo( @PathVariable String id ) {
        service.deleteTodo( id );
    }
}
