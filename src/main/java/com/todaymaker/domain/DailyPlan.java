package com.todaymaker.domain;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

import static javax.persistence.FetchType.LAZY;

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

    @ManyToOne(fetch = LAZY)
    private User user;

    @Data
    public static class Imporant{
        private Long todoId;
        private boolean important;
    }
}


