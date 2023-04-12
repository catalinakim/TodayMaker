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
        List<DailyPlan> todosToday = new ArrayList<>();
        List<Todo> todos = new ArrayList<>();
        for (Long todoId : todoIds) {
            DailyPlan dailyPlan = new DailyPlan();
            dailyPlan.setDay(LocalDate.now());
            dailyPlan.setTodoId(todoId);
            todosToday.add(dailyPlan);
            Todo todo = todoRepository.findById(todoId).get();
            todos.add(todo);
        }
        List<DailyPlan> savedTodos = dailyPlanRepository.saveAll(todosToday);
        return todos;
    }
    @Transactional
    public void removeFromToday(Long todoId) {
    //public boolean removeFromToday(ApiTodoController.TodoDto todoDto) {
        dailyPlanRepository.deleteByTodoIdAndDayEquals(todoId, LocalDate.now());
        return;
    }

    @Transactional(readOnly = true)
    public List<Todo> getTodayTodos() {
        List<DailyPlan> todays = dailyPlanRepository.findTodoIdByDayEquals(LocalDate.now());
        System.out.println(todays.size());
        List<Todo> todayTodos = new ArrayList<>();
        for (DailyPlan todo : todays) {
            Optional<Todo> todayTodo = todoRepository.findById(todo.getTodoId());
            if(!todayTodo.isPresent()){  //삭제된 오늘할일
                //return null;
            }else{
                todayTodos.add(todayTodo.get()); //unwrap Optional
                System.out.println(todayTodo.get().getId() + todayTodo.get().getName());
            }

        }
        return todayTodos;
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
}
