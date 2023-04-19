package com.todaymaker.api;

import com.todaymaker.service.DailyPlanService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ApiDailyPlanController {

    private final DailyPlanService dailyPlanService;

    @PutMapping("/today")
    public Long setImportant(@RequestBody TodayImpDto dto){
        dailyPlanService.setImportant(dto.getTodoId(), dto.isImportant());
        return dto.getTodoId();
    }
    @PutMapping("/today/priority")
    public Long setImportant(@RequestBody TodayPriorityDto dto){
        dailyPlanService.setPriority(dto.getId(), dto.getPriority());
        return dto.getId();
    }
    @Data
    static class TodayImpDto{
        private Long todoId;
        private boolean important;
    }

    @Data
    static class TodayPriorityDto {
        private Long id;
        private int priority;
    }
}
