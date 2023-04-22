package com.todaymaker.api;

import com.todaymaker.Login;
import com.todaymaker.domain.DailyPlan;
import com.todaymaker.domain.Todo;
import com.todaymaker.service.TodoService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/today")  //오늘할일에 추가
    public List<DailyPlan.TodayTodos> addTodo(@Login Long userId, @RequestBody List<Long> todoIds) {
        List<DailyPlan.TodayTodos> savedTodosAtToday = todoService.saveTodoToday(userId, todoIds);

        return savedTodosAtToday;
    }

    @DeleteMapping("/today")
    public Long removeFromToday(@RequestBody Long todoId){
        todoService.removeFromToday(todoId);
        return todoId;
    }

    @PutMapping("/todos/{id}")
    public Long editTodo(@PathVariable Long id, @RequestBody UpdateTodoDto dto){
        todoService.update(id, dto.getName());
        return id;
    }

    @DeleteMapping("/todos/{id}")
    public Long deleteTodo(@Login Long userId, @PathVariable Long id){
        if(userId != null){
            todoService.deleteTodo(id);
        }
        return id;
    }

    @PostMapping("/todos")
    public Todo addTodo(@Login Long userId, @RequestBody CreateTodoDto dto){
        return todoService.addTodo(userId, dto.getCategoryId(), dto.getName());
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
    @NoArgsConstructor
    public static class CreateTodoDto {
        private Long categoryId;
        private String name;
    }
}
