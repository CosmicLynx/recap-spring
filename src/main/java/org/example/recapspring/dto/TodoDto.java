package org.example.recapspring.dto;

import org.example.recapspring.model.TodoStatus;

public record TodoDto( String description, TodoStatus status ) {
}
