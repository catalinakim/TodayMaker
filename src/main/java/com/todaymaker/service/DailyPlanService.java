package com.todaymaker.service;

import com.todaymaker.domain.DailyPlan;
import com.todaymaker.repository.DailyPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class DailyPlanService {
    private final DailyPlanRepository dailyPlanRepository;

    @Transactional
    public Long setImportant(Long todoId, boolean important){
        DailyPlan dailyPlan = dailyPlanRepository.findByTodoIdAndDayEquals(todoId, LocalDate.now());
        dailyPlan.setImportant(important);
        return todoId;
    }

    public List<DailyPlan.Imporant> getImpList() {
        List<DailyPlan.Imporant> todays = new ArrayList<>();
        List<DailyPlan> dailyPlans = dailyPlanRepository.findByDay(LocalDate.now());
        for (DailyPlan todo : dailyPlans) {
            DailyPlan.Imporant today = new DailyPlan.Imporant();
            today.setTodoId(todo.getTodoId());
            today.setImportant(todo.isImportant());
            todays.add(today);
        }
        return todays;
    }
}
