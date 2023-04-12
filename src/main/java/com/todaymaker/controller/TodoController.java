package com.todaymaker.controller;

import com.todaymaker.domain.Category;
import com.todaymaker.domain.Todo;
import com.todaymaker.domain.User;
import com.todaymaker.dto.TodoCreateDto;
import com.todaymaker.service.CategoryService;
import com.todaymaker.service.TodoService;
import com.todaymaker.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Controller
@RequiredArgsConstructor
public class TodoController {

    private final CategoryService categoryService;
    private final TodoService todoService;
    private final UserService userService;

    @GetMapping("/")
    public String home() {

        return "index";
    }

    @GetMapping("/todo")
    public String todoPage(Model model) {
        User user = userService.findTester();
        TodoCreateDto todo = new TodoCreateDto();
        todo.setUser(user);
        model.addAttribute("todo", todo);
        log.info("뷰에 전달된 user id: {}", user.getId());
        List<Category> categories = categoryService.findCategories();
        model.addAttribute("categories", categories);

        return "todo/todoForm";
    }

    @PostMapping("/todo")  //할일추가
    public String addTodo(TodoCreateDto todoDto) {
        //Todo todo = new Todo();  //생성자와 setter로 생성하지 않고, DTO와 생성메소드 통해서 생성
        if(todoDto.getCategory().getId() == null){  //카테고리 선택X
            Todo todo = Todo.createTodoWOCate(todoDto.getUser(), todoDto.getName());
            todoService.saveTodo(todo);
        }else{
            Todo todo = Todo.createTodo(todoDto.getUser(), todoDto.getCategory(), todoDto.getName());
            todoService.saveTodo(todo);
        }

        return "redirect:/todos";
    }

    //할일목록
    @GetMapping(value = "/todos")
    public String list(Model model) {
        List<Category> categories = categoryService.findCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("date", LocalDateTime.now());

        List<Todo> todos = todoService.findTodos();
        //model.addAttribute("todos", todos);
        List<Todo> noCateTodos = todoService.findNoCateTodos();
        model.addAttribute("noCateTodos", noCateTodos);


        List<Todo> todayTodos = todoService.getTodayTodos();
        model.addAttribute("todayTodos", todayTodos);

        return "todo/todos";
    }

}
