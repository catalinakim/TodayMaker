package com.todaymaker.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Category {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = LAZY)
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();

    @OneToMany(mappedBy = "category")
    private List<Todo> todos = new ArrayList<>();

    public Category(String name) {
        this.name = name;
    }

    public Long userId;

    public void setParent(Category category) {
        this.parent = category;
        parent.getChild().add(this);
    }

    public void setParentNull() {
        this.parent = null;
    }


}
