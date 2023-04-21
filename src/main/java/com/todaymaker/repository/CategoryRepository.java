package com.todaymaker.repository;

import com.todaymaker.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository <Category, Long> {
    Category findByName(String name);

    @Query("SELECT c FROM Category c JOIN c.todos t WHERE t.user.id = :userId")
    List<Category> findByUser(@Param("userId") Long userId);  //카테고리에 userId 필드 추가 전

    List<Category> findByUserId(Long userId);

    List<Category> findByUserIdAndParentIsNull(Long userId);

    Long countByParentId(Long id);
}
