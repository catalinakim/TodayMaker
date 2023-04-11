package com.todaymaker.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "name"})  //연관관계 필드 제외
public class Todo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //할일:사용자 = N:1
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "owner")
    @JsonIgnore
    private User user;

    //할일:카테고리 = N:1
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="categoryId")
    @JsonIgnore
    private Category category;

    private String name;

    //==연관관계 메서드==//
    public void setUser(User user) {
        this.user = user;
        user.getTodos().add(this);
    }

    public void setCategory(Category category) {
        this.category = category;
        category.getTodos().add(this);
    }

    public void setCategoryNull(){
        //category.getTodos().remove(this);
        this.category = null;
    }

    //==생성 메서드==//
    public static Todo createTodo(User user, Category category, String name) {
        Todo todo = new Todo();
        todo.setUser(user);
        todo.setCategory(category);
        todo.setName(name);
        return todo;
    }
    public static Todo createTodoWOCate(User user, String name) {
        Todo todo = new Todo();
        todo.setUser(user);
        todo.setCategoryNull();
        todo.setName(name);
        return todo;
    }


}
