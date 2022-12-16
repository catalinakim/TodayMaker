package com.todaymaker;

import com.todaymaker.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.initTestUser();
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
}
