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

    private int priority;

    public void changePriority(int num){
        this.priority = num;
    }

    //==생성 메서드==//
    public static DailyPlan createDailyTodo(User user, Long todoId){
        DailyPlan daily = new DailyPlan();
        daily.setUser(user);
        daily.setTodoId(todoId);
        daily.setDay(LocalDate.now());
        return daily;
    }

    @Data
    public static class Important{
        private Long todoId;
        private boolean important;
    }

    @Data
    public static class TodayTodos {
        private Long id;
        private Long todoId;
        private String name;
        private boolean important;
        private int priority;

        public TodayTodos(DailyPlan daily, Todo todo) {
            this.id = daily.getId();
            this.todoId = todo.getId();
            this.name = todo.getName();
            this.important = daily.isImportant();
            this.priority = daily.getPriority();
        }
    }
}


