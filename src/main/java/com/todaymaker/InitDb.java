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

    //@PostConstruct
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
            return category;
        }
        private Category createSubCategory(String name, Category parent){
            Category category = new Category();
            category.setName(name);
            category.setParent(parent);
            return category;
        }
    }
}
