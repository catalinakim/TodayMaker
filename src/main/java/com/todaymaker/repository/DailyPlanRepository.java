package com.todaymaker.repository;

import com.todaymaker.domain.DailyPlan;
import com.todaymaker.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;


public interface DailyPlanRepository extends JpaRepository<DailyPlan, Long> {

    void deleteByTodoIdAndDayEquals(Long todoId, LocalDate now);

    List<DailyPlan> findTodoIdByDayEquals(LocalDate now); //List<Long>

    void deleteByTodoId(Long id);

    DailyPlan findByTodoIdAndDayEquals(Long todoId, LocalDate now);

    List<DailyPlan> findByDay(LocalDate now);

    List<DailyPlan> findByUserAndDay(User user, LocalDate now);
}
