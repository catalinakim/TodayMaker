package com.todaymaker.repository;

import com.todaymaker.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@SpringBootTest
@Transactional
class UserJpaRepositoryTest {

    @Autowired
    private UserJpaRepository userJpaRepository;

    @AfterEach
    void clear(){
        userJpaRepository.clear();
    }

    @Test
    void save() {
        User user = new User();
        user.setLoginId("abcd");
        user.setPassword("1234");

        User savedUser = userJpaRepository.save(user);

        Assertions.assertThat(savedUser.getId()).isNotNull();
        Assertions.assertThat(user).isEqualTo(savedUser);
    }

    @Test
    void findOne() {
        User user = new User();
        user.setLoginId("abcd");
        user.setPassword("1234");
        User savedUser = userJpaRepository.save(user);
        User findUser = userJpaRepository.findOne(savedUser.getId());
        Assertions.assertThat(findUser).isEqualTo(savedUser);
    }

    @Test
    void findByLoginId() {
        User user = new User();
        user.setLoginId("abcd");
        user.setPassword("1234");
        User savedUser = userJpaRepository.save(user);
        List<User> users = userJpaRepository.findByLoginId(user.getLoginId());
        Assertions.assertThat(users.size()).isEqualTo(1);
    }
}