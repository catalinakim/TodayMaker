package com.todaymaker.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.todaymaker.domain.Category;
import com.todaymaker.domain.QCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CategoryRepository {

    private final EntityManager em;

    public void save(Category category) {
        if (category.getId() == null) {
            em.persist(category);
        } else {
            //카테고리 수정시
            em.merge(category);  //기존 엔티티를 새 엔티티로 변경
        }
    }
    public List<Category> findAll() {
        return em.createQuery("select c from Category c", Category.class).getResultList();
    }


    public List<Category> findSub(Long id) {
        return em.createQuery("select c from Category c where c.parent.id = :id", Category.class)
                .setParameter("id", id)
                .getResultList();
    }

    public List<Category> findRootCategories() {
        QCategory category = QCategory.category;

        JPAQueryFactory query = new JPAQueryFactory(em);

        return query
                .select(category)
                .from(category)
                .where(category.parent.isNull())
                .fetch();

    }
}
