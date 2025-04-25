package org.example.recapspring.service;

import org.example.recapspring.dto.TodoDto;
import org.example.recapspring.model.Todo;
import org.example.recapspring.repository.TodoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {
    private final TodoRepository repo;
    private final IdService idService;
    
    public TodoService( TodoRepository repo, IdService idService ) {
        this.repo = repo;
        this.idService = idService;
    }
    
    public List<Todo> getAllTodos() {
        return repo.findAll();
    }
    
    public Todo createTodo( TodoDto todoDto ) {
        Todo newTodo = new Todo( idService.randomId(), todoDto.description(), todoDto.status() );
        repo.save( newTodo );
        return newTodo;
    }
    
    public Todo getTodoById( String id ) {
        return repo.findById( id ).orElse( null );
    }
    
    public void updateTodo( String id, TodoDto todoDto ) {
        repo.findById( id )
                .ifPresent( oldTodo -> repo.save( oldTodo
                        .withDescription( todoDto.description() )
                        .withStatus( todoDto.status() ) ) );
    }
    
    public void deleteTodo( String id ) {
        repo.deleteById( id );
    }
}
