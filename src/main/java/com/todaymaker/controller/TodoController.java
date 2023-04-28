package com.todaymaker.controller;

import com.todaymaker.Login;
import com.todaymaker.domain.Category;
import com.todaymaker.domain.DailyPlan;
import com.todaymaker.domain.Todo;
import com.todaymaker.domain.User;
import com.todaymaker.dto.TodoCreateDto;
import com.todaymaker.service.CategoryService;
import com.todaymaker.service.DailyPlanService;
import com.todaymaker.service.TodoService;
import com.todaymaker.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@Controller
@RequiredArgsConstructor
public class TodoController {

    private final CategoryService categoryService;
    private final TodoService todoService;
    private final UserService userService;
    private final DailyPlanService dailyPlanService;
    @Value("${testerId}")
    private Long testerId;
    @Value("${testerLoginId}")
    private String testerLoginId;

    @GetMapping("/")
    public String home(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if(session == null){
            return "index";
        }
        String loginId = (String) session.getAttribute("loginId");
        if(loginId == null){
            return "index";
        }
        //return "redirect:/todos";
        return "index";
    }

    @GetMapping("/todo")
    public String todoPage(@Login Long userId, Model model) {
        User user = userService.findUser(userId);
        TodoCreateDto todo = new TodoCreateDto();
        todo.setUser(user);
        model.addAttribute("todo", todo);

        List<Category> categories = categoryService.findCategories(user.getId());
        model.addAttribute("categories", categories);

        return "todo/todoForm";
    }

    @PostMapping("/todo")  //할일추가
    public String addTodo(@ModelAttribute("todo") TodoCreateDto todoDto, BindingResult bindingResult) {
        if(!StringUtils.hasText(todoDto.getName())){
            bindingResult.addError(new FieldError("todo","name","할일 공백 불가"));
        }
        if(bindingResult.hasErrors()){
            return "todo/todoForm";
        }
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
    public String list(@Login Long userId, Model model) {
        List<Category> categories = categoryService.findCategories(userId);
        model.addAttribute("categories", categories);

        List<Todo> noCateTodos = todoService.findNoCateTodos(userId);
        model.addAttribute("noCateTodos", noCateTodos);

        List<DailyPlan.TodayTodos> todayTodos = todoService.getTodayTodos(userId);
        model.addAttribute("todayTodos", todayTodos);

        List<DailyPlan.Important> importantList = dailyPlanService.getImpList();
        model.addAttribute("todays", importantList);
        model.addAttribute("date", LocalDateTime.now());

        return "todo/todos";
    }

    //쌤플 유저로 사용해보기
    @GetMapping(value = "/todos/tester")
    public String testerUse(HttpSession session) {
        User user = userService.findUser(testerId);
        if(user != null){
            session.setAttribute("userId", testerId);
            session.setAttribute("loginId", testerLoginId);
        }

        return "redirect:/todos";
    }

}
