package com.todaymaker.repository;

import com.todaymaker.domain.DailyPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;


public interface DailyPlanRepository extends JpaRepository<DailyPlan, Long> {

    void deleteByTodoIdAndDayEquals(Long todoId, LocalDate now);

    List<DailyPlan> findTodoIdByDayEquals(LocalDate now); //List<Long>
}
