package com.todaymaker.service;

import com.todaymaker.domain.User;
import com.todaymaker.repository.UserJpaRepository;
import com.todaymaker.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired EntityManager em;
    @Autowired UserJpaRepository userJpaRepository;
    @Autowired UserRepository userRepository;
    @Autowired UserService userService;

    @Test
    void 회원가입() {
        User savedUser = userService.save(new User("aaa", "1234", "aaa@a.ai"));
        Assertions.assertEquals(savedUser, userJpaRepository.findOne(savedUser.getId()));
    }

    @Test
    void 중복ID_예외(){
        User user = userService.save(new User("aaa", "1234", "aaa@a.ai"));
        Assertions.assertThrows(IllegalStateException.class, ()-> userService.duplicate(user.getLoginId()));
    }
}