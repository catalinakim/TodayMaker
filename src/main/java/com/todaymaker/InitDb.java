package com.todaymaker;

import com.todaymaker.domain.Category;
import com.todaymaker.domain.Todo;
import com.todaymaker.domain.User;
import com.todaymaker.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Slf4j
@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init() {
        //initService.initTestUser();
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
        public void initTestUser() {
            User tester = createMember("test", "2022");
            em.persist(tester);
        }
        public void initCategory() {
            Category basicCategory1 = createCategory("취업준비");
            Category basicCategory2 = createCategory("건강관리");
            em.persist(basicCategory1);
            em.persist(basicCategory2);
            cateId1 = basicCategory1.getId();
            cateId2 = basicCategory2.getId();
            Category sub1 = createSubCategory("MVP 완성", basicCategory1);
            Category sub2 = createSubCategory("운동", basicCategory2);
            em.persist(sub1);
            em.persist(sub2);
            Todo todo1 = createTodo("배포", basicCategory1);
            Todo todo1sub = createTodo("API 명세/컨트롤러 일치", sub1);
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
            category.setUserId(Long.valueOf(3));
            return category;
        }
        private Category createSubCategory(String name, Category parent){
            Category category = new Category();
            category.setName(name);
            category.setParent(parent);
            category.setUserId(Long.valueOf(3));
            return category;
        }

        private Todo createTodo(String name, Category category){
            User user = em.find(User.class, Long.valueOf(3));
            Todo todo = Todo.createTodo(user, category, name);
            return todo;
        }
    }
}
