package com.todaymaker.service;

import com.todaymaker.api.ApiTodoController;
import com.todaymaker.domain.Category;
import com.todaymaker.domain.DailyPlan;
import com.todaymaker.domain.Todo;
import com.todaymaker.repository.CategoryRepository;
import com.todaymaker.repository.DailyPlanRepository;
import com.todaymaker.repository.TodoJpaRepository;
import com.todaymaker.repository.TodoRepository;
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
        List<Todo> todayTodos = new ArrayList<>();
        for (DailyPlan todo : todays) {
            Optional<Todo> todayTodo = todoRepository.findById(todo.getTodoId());
            if(!todayTodo.isPresent()){
                System.out.println("오늘 할일 기등록 없음");
                return null;
            }else{
                todayTodos.add(todayTodo.get()); //unwrap Optional
            }

        }
        return todayTodos;
    }
}
