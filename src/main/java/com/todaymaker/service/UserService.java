package com.todaymaker.service;

import com.todaymaker.domain.User;
import com.todaymaker.repository.UserJpaRepository;
import com.todaymaker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserJpaRepository userJpaRepository;
    private final UserRepository userRepository;

    //회원 조회
    public User findTester() {
        return userJpaRepository.findOne(1L);
    }

    public User findUser(String loginId) {  //ID중복확인
        return (User) userJpaRepository.findByLoginId(loginId);
    }

    public void duplicate(String loginId) {
        Optional<User> findUser = userRepository.findByLoginId(loginId);
        if(!findUser.isEmpty()){
            throw new IllegalStateException("사용중인 ID");
        }
    }

    @Transactional
    public User save(User user) {
        return userJpaRepository.save(user);
    }

    public User login(String loginId, String password) {
        return userRepository.findByLoginId(loginId)
                .filter(m -> m.getPassword().equals(password))
                .orElse(null);
    }


}
