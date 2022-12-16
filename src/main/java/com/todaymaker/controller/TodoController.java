package com.todaymaker.controller;

import com.todaymaker.domain.Todo;
import com.todaymaker.domain.User;
import com.todaymaker.service.TodoService;
import com.todaymaker.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;
    private final UserService userService;

    @GetMapping("/")
    public String home() {

        return "index";
    }

    @GetMapping("/todo")
    public String todoPage(Model model) {
        User user = userService.findTester();
        Todo todo = new Todo();
        todo.setUser(user);
        model.addAttribute("todo", todo);

        return "todo/todoForm";
    }

    @PostMapping("/todo")
    public String addTodo(Todo todo) {
        todoService.saveTodo(todo);

        return "redirect:/todos";
    }

    //할일목록
    @GetMapping(value = "/todos")
    public String list(Model model) {
        List<Todo> todos = todoService.findTodos();
        model.addAttribute("todos", todos);
        return "todo/todoList";
    }


}
