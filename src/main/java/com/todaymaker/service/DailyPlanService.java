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
    public Long setImportant(Long dailyId, boolean important){
        DailyPlan dailyPlan = dailyPlanRepository.findById(dailyId).orElse(null);
        if(dailyPlan != null){
            dailyPlan.setImportant(important);
        }
        return dailyId;
    }

    public List<DailyPlan.Important> getImpList() {
        List<DailyPlan.Important> todays = new ArrayList<>();
        List<DailyPlan> dailyPlans = dailyPlanRepository.findByDay(LocalDate.now());
        for (DailyPlan todo : dailyPlans) {
            DailyPlan.Important today = new DailyPlan.Important();
            today.setTodoId(todo.getTodoId());
            today.setImportant(todo.isImportant());
            todays.add(today);
        }
        return todays;
    }
    @Transactional
    public void setPriority(Long id, int priority) {
        DailyPlan today = dailyPlanRepository.findById(id).orElse(null);
        if(today != null){
            today.changePriority(priority);
        }
    }
}
