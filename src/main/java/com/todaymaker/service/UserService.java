package com.todaymaker.service;

import com.todaymaker.domain.User;
import com.todaymaker.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserJpaRepository userJpaRepository;

    //회원 조회
    public User findTester() {
        return userJpaRepository.findOne(1L);
    }

    public User findUser(String loginId) {
        return (User) userJpaRepository.findByLoginId(loginId);
    }
}
