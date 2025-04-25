package org.example.recapspring.controller;

import org.example.recapspring.model.*;
import org.example.recapspring.repository.TodoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class TodoControllerTest {
    @Autowired
    private MockMvc mvc;
    
    @Autowired
    private TodoRepository repo;
    
    @Test
    void getAllTodos() throws Exception {
        Todo testTodo = new Todo( "1", "new", TodoStatus.OPEN );
        repo.save( testTodo );
        
        mvc.perform( MockMvcRequestBuilders.get( "/api/todo" ) )
                .andExpect( MockMvcResultMatchers.status().isOk() )
                .andExpect( MockMvcResultMatchers.content().json( """
                        [
                            {
                                "id": "1",
                                "description": "new",
                                "status": "OPEN"
                            }
                        ]
                        """ ) );
    }
    
    @Test
    void getTodoById_returnsTodo_withValidId() throws Exception {
        Todo testTodo = new Todo( "1", "new", TodoStatus.OPEN );
        repo.save( testTodo );
        
        mvc.perform( MockMvcRequestBuilders.get( "/api/todo/1" ) )
                .andExpect( MockMvcResultMatchers.status().isOk() )
                .andExpect( MockMvcResultMatchers.content().json( """
                        {
                            "id": "1",
                            "description": "new",
                            "status": "OPEN"
                        }
                        """ ) );
    }
    
    @Test
    void getTodoById_returnsNotFound_withInvalidId() throws Exception {
        Todo testTodo = new Todo( "1", "new", TodoStatus.OPEN );
        repo.save( testTodo );
        
        mvc.perform( MockMvcRequestBuilders.get( "/api/todo/2" ) )
                .andExpect( MockMvcResultMatchers.status().isNotFound() )
                .andReturn();
    }
    
    @Test
    void createTodo_returnsTodo_withValidData() throws Exception {
        mvc.perform( MockMvcRequestBuilders.post( "/api/todo" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( """
                                {
                                    "description": "new",
                                    "status": "OPEN"
                                }
                                """ )
                )
                .andExpect( MockMvcResultMatchers.status().isOk() )
                .andExpect( MockMvcResultMatchers.jsonPath( "$.id" ).isNotEmpty() )
                .andExpect( MockMvcResultMatchers.content().json( ("""
                        {
                            "description": "new",
                            "status": "OPEN"
                        }
                        """) ) );
    }
    
    @Test
    void createTodo_returnsBadRequest_withInvalidData() throws Exception {
        mvc.perform( MockMvcRequestBuilders.post( "/api/todo" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( """
                                {
                                      "description": "new",
                                      "status": "OPENN"
                                }
                                """ ) )
                .andExpect( MockMvcResultMatchers.status().isBadRequest() )
                .andReturn();
    }
    
    @Test
    void updateTodo_updatesTodo_withValidData() throws Exception {
        Todo testTodo = new Todo( "1", "new", TodoStatus.OPEN );
        repo.save( testTodo );
        
        mvc.perform( MockMvcRequestBuilders.put( "/api/todo/1" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( """
                                {
                                    "description": "new updated",
                                    "status": "OPEN"
                                }
                                """ )
                )
                .andExpect( MockMvcResultMatchers.status().isOk() );
        mvc.perform( MockMvcRequestBuilders.get( "/api/todo/1" ) )
                .andExpect( MockMvcResultMatchers.status().isOk() )
                .andExpect( MockMvcResultMatchers.content().json( """
                        {
                            "id": "1",
                            "description": "new updated",
                            "status": "OPEN"
                        }
                        """ ) );
    }
    
    @Test
    void deleteTodo_returns404_afterSuccessfulDelete() throws Exception {
        Todo testTodo = new Todo( "1", "new", TodoStatus.OPEN );
        repo.save( testTodo );
        
        mvc.perform( MockMvcRequestBuilders.delete( "/api/todo/1" ) )
                .andExpect( MockMvcResultMatchers.status().isOk() );
        mvc.perform( MockMvcRequestBuilders.get( "/api/todo/1" ) )
                .andExpect( MockMvcResultMatchers.status().isNotFound() );
    }
    
}