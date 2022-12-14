package com.todaymaker.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Todo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //할일:사용자 = N:1
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "owner")
    private User user;

    //할일:카테고리 = N:1
    @ManyToOne(fetch = LAZY)
    //@ManyToOne(fetch = LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name="categoryId")
    private Category category;

    private String name;

    //public Todo(User user, Category category) {
    //    this.user = user;
    //    this.category = category;
    //}

    //==연관관계 메서드==//
    public void setUser(User user) {
        this.user = user;
        user.getTodos().add(this);
    }

    public void setCategory(Category category) {
        this.category = category;
        category.getTodos().add(this);
    }

    //==생성 메서드==//
    public static Todo createTodo(User user, Category category, String name) {
        Todo todo = new Todo();
        todo.setUser(user);
        todo.setCategory(category);
        todo.setName(name);
        return todo;
    }

}
