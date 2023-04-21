package com.todaymaker.repository;

import com.todaymaker.domain.DailyPlan;
import com.todaymaker.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;


public interface DailyPlanRepository extends JpaRepository<DailyPlan, Long> {

    void deleteByTodoIdAndDayEquals(Long todoId, LocalDate now);

    List<DailyPlan> findTodoIdByDayEquals(LocalDate now); //List<Long>

    void deleteByTodoId(Long id);

    DailyPlan findByTodoIdAndDayEquals(Long todoId, LocalDate now);

    List<DailyPlan> findByDay(LocalDate now);

    List<DailyPlan> findByUserAndDay(User user, LocalDate now);

    List<DailyPlan> findByUserAndDayOrderByPriority(User user, LocalDate now);

    @Query("SELECT d FROM DailyPlan d WHERE d.user = :user AND d.day = :now ORDER BY 0.1/d.priority DESC")
    List<DailyPlan> findTodayOrderByPriority(@Param("user") User user, @Param("now") LocalDate now);
}
