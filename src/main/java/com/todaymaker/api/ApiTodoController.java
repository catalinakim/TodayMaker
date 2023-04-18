package com.todaymaker.api;

import com.todaymaker.domain.Category;
import com.todaymaker.domain.DailyPlan;
import com.todaymaker.domain.Todo;
import com.todaymaker.dto.CategoryDto;
import com.todaymaker.service.TodoService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ApiTodoController {

    private final TodoService todoService;

    //상위 카테고리 클릭시 할일 목록
    @GetMapping("/categories/{id}/todos")
    public List<TodoDto> subCategoryList(@PathVariable Long id) {
        List<Todo> todos = todoService.findTodosByCategory(id);
        List<TodoDto> result = todos.stream()
                .map(t -> new TodoDto(t.getId(), t.getName()))
                .collect(toList());
        return result;
    }

    @PostMapping("/todos/today")
    public List<Todo> addTodo(@RequestBody List<Long> todoIds) {
        List<Todo> savedTodos = todoService.saveTodoToday(todoIds);

        return savedTodos;
    }

    @DeleteMapping("/todos/today")
    public Long removeFromToday(@RequestBody Long todoId){
    //public boolean removeFromToday(@RequestBody TodoDto todoDto){
        todoService.removeFromToday(todoId);
        return todoId;
    }

    @PutMapping("/todos/{id}")
    public Long editTodo(@PathVariable Long id, @RequestBody UpdateTodoDto dto){
        todoService.update(id, dto.getName());
        return id;
    }

    @DeleteMapping("/todos")
    public Long deleteTodo(@RequestBody TodoDto todo){
        todoService.deleteTodo(todo.getId());
        return todo.getId();
    }

    @PostMapping("/todos")
    public Todo addTodo(@RequestBody CreateTodoDto dto){
        return todoService.addTodo(dto);
    }

    @GetMapping("/categories/sub/{cateId}")
    public List<Todo> subTodos(@PathVariable Long cateId) {  //하위카테고리의 할일
        List<Todo> subTodoList = todoService.findSub(cateId);

        return subTodoList;
    }

    @Data
    @AllArgsConstructor
    static class TodoDto {
        private Long id;
        private String name;
    }

    @Data
    static class UpdateTodoDto {
        private String name;
    }

    @Data
    @AllArgsConstructor
    public static class CreateTodoDto {
        private Long userId;
        private Long categoryId;
        private String name;
    }
}
