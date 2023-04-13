package com.todaymaker.domain;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@Getter @Setter
@NoArgsConstructor
public class DailyPlan {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private LocalDate day;

    private Long todoId;

    private boolean done;

    private boolean important;

    @Data
    public static class TodayImp{
        private Long todoId;
        private boolean important;
    }
}


