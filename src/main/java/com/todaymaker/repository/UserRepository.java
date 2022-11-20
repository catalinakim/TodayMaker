package com.todaymaker.repository;


import com.todaymaker.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

//public interface UserRepository {
public interface UserRepository extends JpaRepository<User, Long> {
}
