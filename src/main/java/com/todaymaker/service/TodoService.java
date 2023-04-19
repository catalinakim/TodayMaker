package com.todaymaker.service;

import com.todaymaker.api.ApiTodoController;
import com.todaymaker.domain.Category;
import com.todaymaker.domain.DailyPlan;
import com.todaymaker.domain.Todo;
import com.todaymaker.domain.User;
import com.todaymaker.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoJpaRepository todoJpaRepository;
    private final TodoRepository todoRepository;
    private final CategoryRepository categoryRepository;
    private final DailyPlanRepository dailyPlanRepository;
    private final UserJpaRepository userJpaRepository;

    @Transactional
    public void saveTodo(Todo todo) {
        todoJpaRepository.save(todo);
    }

    @Transactional(readOnly = true)
    public List<Todo> findTodos() {
        return todoJpaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Todo> findTodosByCategory(Long id) {
        Category category = categoryRepository.findById(id).get();//추후 optional로
        List<Todo> todosInCategory = todoRepository.findByCategory(category);
        System.out.println("todosInCategory = " + todosInCategory);
        System.out.println("todosInCategory.size() = " + todosInCategory.size());
        return todosInCategory;
    }
    @Transactional
    public List<Todo> saveTodoToday(List<Long> todoIds) {
        List<DailyPlan> todayList = new ArrayList<>();
        List<Todo> todos = new ArrayList<>();
        for (Long todoId : todoIds) {
            DailyPlan dailyPlan = new DailyPlan();
            Todo todo = todoRepository.findById(todoId).get();
            dailyPlan.setDay(LocalDate.now());
            dailyPlan.setTodoId(todoId);
            dailyPlan.setUser(todo.getUser());
            todayList.add(dailyPlan);
            todos.add(todo);
        }
        List<DailyPlan> savedTodos = dailyPlanRepository.saveAll(todayList);
        return todos;
    }
    @Transactional
    public void removeFromToday(Long todoId) {
        dailyPlanRepository.deleteByTodoIdAndDayEquals(todoId, LocalDate.now());
        return;
    }

    @Transactional(readOnly = true)
    public List<DailyPlan.TodayTodos> getTodayTodos(Long userId) {
        User user = userJpaRepository.findOne(userId);
        List<DailyPlan> todays = dailyPlanRepository.findByUserAndDay(user, LocalDate.now());
        List<DailyPlan.TodayTodos> todayTodosDto = new ArrayList<>();
        for (DailyPlan todayRow : todays) {
            Todo todo = todoRepository.findById(todayRow.getTodoId()).orElse(null);
            if(todo != null){
                DailyPlan.TodayTodos each = new DailyPlan.TodayTodos(todayRow, todo);
                todayTodosDto.add(each);
            }
        }
        return todayTodosDto;
    }
    @Transactional
    public void update(Long id, String name) {
        Todo todo = todoJpaRepository.findOne(id);
        todo.setName(name);
    }
    @Transactional
    public void deleteTodo(Long id) {
        dailyPlanRepository.deleteByTodoId(id);
        todoRepository.deleteById(id);
    }
    @Transactional
    public Todo addTodo(ApiTodoController.CreateTodoDto dto) {
        User user = userJpaRepository.findOne(dto.getUserId());
        Category category = categoryRepository.findById(dto.getCategoryId()).get();
        Todo todo = Todo.createTodo(user, category, dto.getName());
        return todoRepository.save(todo);
    }

    public List<Todo> findSub(Long cateId) {
        List<Todo> todos = todoRepository.findByCategoryId(cateId);
        return todos;
    }

    public List<Todo> findNoCateTodos(Long userId) {
        List<Todo> noCateTodos = todoRepository.findByUserIdAndCategoryIsNull(userId);
        return noCateTodos;
    }
}
