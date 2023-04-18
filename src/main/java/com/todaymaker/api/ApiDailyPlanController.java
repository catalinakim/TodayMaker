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
    @Data
    static class TodayImpDto{
        private Long todoId;
        private boolean important;
    }
}
