package br.com.todolist;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/todos")
@CrossOrigin("*")
public class TodoController {
    private final TodoRepository todoRepository;

    public TodoController(TodoRepository todoRepository){
        this.todoRepository = todoRepository;
    }

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<Todo> getAll() {
        return this.todoRepository.findAll();
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Todo create(@RequestBody Todo todo) {
        return this.todoRepository.save(todo);
    }

    @DeleteMapping("/{todoId}")
    public ResponseEntity<Void> delete(@PathVariable Integer todoId) {
        Optional<Todo> todo = this.todoRepository.findById(todoId);

        if (todo.isPresent()){
            this.todoRepository.deleteById(todoId);

            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{todoId}/start_task")
    public ResponseEntity<Todo> startTask(@PathVariable Integer todoId){
        Todo todoDatabase = this.todoRepository.findById(todoId).get();
        if (todoDatabase != null){
            todoDatabase.setStatus(StatusEnum.IN_PROGRESS);
            this.todoRepository.save(todoDatabase);
            return ResponseEntity.ok(todoDatabase);
        } else {
            return  ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{todoId}/end_task")
    public ResponseEntity<Todo> endTask(@PathVariable Integer todoId){
        Todo todoDatabase = this.todoRepository.findById(todoId).get();
        if (todoDatabase != null){
            todoDatabase.setStatus(StatusEnum.FINISHED);
            this.todoRepository.save(todoDatabase);
            return ResponseEntity.ok(todoDatabase);
        } else {
            return  ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{todoId}")
    public ResponseEntity<Todo> update(@PathVariable Integer todoId, @RequestBody Todo todo){
        Todo todoDatabase = this.todoRepository.findById(todoId).get();
        if (todoDatabase != null){
            todoDatabase.setTitle(todo.getTitle());
            todoDatabase.setDescription(todo.getDescription());
            this.todoRepository.save(todoDatabase);
            return ResponseEntity.ok(todoDatabase);
        }else {
            return ResponseEntity.notFound().build();
        }
    }
}