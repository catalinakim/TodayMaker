package com.todaymaker.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Todo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //할일:사용자 = N:1
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "owner")
    private User user;

    //할일:카테고리 = N:1
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="categoryId")
    private Category category;

    private String name;

    public Todo(User user, Category category) {
        this.user = user;
        this.category = category;
    }

    //==연관관계 메서드==//
    public void setTodo(User user) {
        this.user = user;
        user.getTodos().add(this);
    }

}
