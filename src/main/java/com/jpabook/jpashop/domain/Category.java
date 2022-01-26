package com.jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Category  {
    @Id
    @GeneratedValue
    @Column(name = "category_id")  //DB칼럼명이랑 매핑
    private Long id;

    private String name;

    @ManyToMany //n:m관계에서는 @JoinTable이 필요
    @JoinTable(name = "category_item",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<Item> items = new ArrayList<>();

    /*부모자식 관계*/

    //1) 부모인 경우
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    //2) 자식인 경우
    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();   //한 부모는 여러 자식을 가질 수 있음

}
