package com.todaymaker;

import com.todaymaker.domain.Category;
import com.todaymaker.domain.Todo;
import com.todaymaker.domain.User;
import com.todaymaker.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Slf4j
@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    //@PostConstruct
    public void init() {
        initService.initTestUser();
        initService.initCategory();
        log.info("init '카테고리 생성' 실행");
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    private static class InitService {
        private final EntityManager em;
        private Long cateId1;
        private Long cateId2;
        @Value("${pw}")
        private String pw;
        public void initTestUser() {
            User tester = createMember("tester", pw);
            em.persist(tester);
        }
        public void initCategory() {
            Category basicCategory1 = createCategory("청소");
            Category basicCategory2 = createCategory("건강관리");
            em.persist(basicCategory1);
            em.persist(basicCategory2);
            cateId1 = basicCategory1.getId();
            cateId2 = basicCategory2.getId();
            Category sub1 = createSubCategory("주방정리", basicCategory1);
            Category sub2 = createSubCategory("운동", basicCategory2);
            em.persist(sub1);
            em.persist(sub2);
            Todo todo1 = createTodo("환기", basicCategory1);
            Todo todo1sub = createTodo("유통기한 지난 것 버리기", sub1);
            Todo todo2 = createTodo("샐러드", basicCategory2);
            Todo todo2sub = createTodo("코어", sub2);
            em.persist(todo1);
            em.persist(todo1sub);
            em.persist(todo2);
            em.persist(todo2sub);
        }

        private User createMember(String loginId, String password) {
            User user = new User();
            user.setLoginId(loginId);
            user.setPassword(password);
            return user;
        }
        private Category createCategory(String name) {
            Category category = new Category();
            category.setName(name);
            category.setUserId(Long.valueOf(1));
            return category;
        }
        private Category createSubCategory(String name, Category parent){
            Category category = new Category();
            category.setName(name);
            category.setParent(parent);
            category.setUserId(Long.valueOf(1));
            return category;
        }

        private Todo createTodo(String name, Category category){
            User user = em.find(User.class, Long.valueOf(1));
            Todo todo = Todo.createTodo(user, category, name);
            return todo;
        }
    }
}
