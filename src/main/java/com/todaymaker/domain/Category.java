package com.todaymaker.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public Category(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "category")
    private List<Todo> todos = new ArrayList<>();

    public Category() {
    }
}
