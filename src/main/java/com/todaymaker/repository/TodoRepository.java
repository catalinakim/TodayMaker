package com.todaymaker.repository;

import com.todaymaker.domain.Category;
import com.todaymaker.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    List<Todo> findByCategory(Category category);
    //List<Todo> findByCategoryId(Long id);
}
