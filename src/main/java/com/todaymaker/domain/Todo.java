package com.todaymaker.domain;

import javax.persistence.*;

@Entity
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //할일 입장에서 할일:사용자 = N:1 (한 사용자가 여러 할일)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner")
    private User user;

    //할일:카테고리 = N:1 (한 카테고리에 여러 할일)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="categoryId")
    private Category category;

    private String name;

    public Todo(User user, Category category) {
        this.user = user;
        this.category = category;
    }

    public Todo() {

    }
}
