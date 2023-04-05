package com.todaymaker.service;

import com.todaymaker.domain.Category;
import com.todaymaker.domain.Todo;
import com.todaymaker.repository.CategoryRepository;
import com.todaymaker.repository.TodoJpaRepository;
import com.todaymaker.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TodoService {

    private final TodoJpaRepository todoJpaRepository;
    private final TodoRepository todoRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public void saveTodo(Todo todo) {
        todoJpaRepository.save(todo);
    }

    public List<Todo> findTodos() {
        return todoJpaRepository.findAll();
    }

    public List<Todo> findTodosByCategory(Long id) {
        Category category = categoryRepository.findById(id).get();//추후 optional로
        List<Todo> todosInCategory = todoRepository.findByCategory(category);
        System.out.println("todosInCategory = " + todosInCategory);
        System.out.println("todosInCategory.size() = " + todosInCategory.size());
        return todosInCategory;
    }
}
