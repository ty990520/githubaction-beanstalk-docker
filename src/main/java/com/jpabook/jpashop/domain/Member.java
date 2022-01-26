package com.jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded   //내장타입을 포함했다
    private Address address;

    //mappedBy = "member" 매핑된 거울쪽 (읽기 전용)
    @OneToMany(mappedBy = "member")  //order쪽이 일, 주문내역(orderItems)이 다
    private List<Order> orders = new ArrayList<>();
}
