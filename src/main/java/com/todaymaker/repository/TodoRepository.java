package com.todaymaker.repository;

import com.todaymaker.domain.Todo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TodoRepository {

    private final EntityManager em;

    public void save(Todo todo) {
        if (todo.getId() == null) {
            System.out.println("TodoRepository.save");
            em.persist(todo);
        } else {
            //할일 수정시
            System.out.println("TodoRepository.save.merge");
            em.merge(todo);  //병합(준영속 엔티티를 영속 상태로 변경)
        }
    }

    public List<Todo> findAll() {
        return em.createQuery("select t from Todo t", Todo.class).getResultList();
    }
}
