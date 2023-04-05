package com.todaymaker.api;

import com.todaymaker.domain.Todo;
import com.todaymaker.service.TodoService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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

    @Data
    @AllArgsConstructor
    static class TodoDto {
        private Long id;
        private String name;
    }
}
