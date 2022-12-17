package com.todaymaker;

import com.todaymaker.domain.Category;
import com.todaymaker.domain.User;
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
        initService.initTestUser();
        log.info("init '테스트 유저 생성' 실행");
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    private static class InitService {
        private final EntityManager em;
        public void initTestUser() {
            User tester = createMember("test", "2022");
            em.persist(tester);
        }

        private User createMember(String loginId, String password) {
            User user = new User();
            user.setLoginId(loginId);
            user.setPassword(password);
            return user;
        }
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    private static class InitCategory {
        private final EntityManager em;
        public void sampleCategory() {
            Category basicCategory1 = createCategory("스터디");
            Category basicCategory2 = createCategory("건강관리");
            em.persist(basicCategory1);
            em.persist(basicCategory2);
        }

        private Category createCategory(String name) {
            Category category = new Category();
            category.setName(name);
            return category;
        }
    }
}
