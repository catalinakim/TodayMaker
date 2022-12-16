package com.todaymaker.service;

import com.todaymaker.domain.User;
import com.todaymaker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    //회원 조회
    public User findTester() {
        return userRepository.findOne(1L);
    }

    public User findUser(String loginId) {
        return (User) userRepository.findByLoginId(loginId);
    }
}
