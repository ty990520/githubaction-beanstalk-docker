package com.jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Delivery {
    @Id
    @GeneratedValue
    @Column(name = "delivery_id")  //DB칼럼명이랑 매핑
    private Long id;

    @OneToOne(mappedBy = "delivery",fetch = FetchType.LAZY)
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)   //enum을 사용하는 경우에 붙여줌
    //EnumType.ORDINAL은 숫자값으로 들어가는데 상태가 3개가 넘어가면 문제발생함
    //항상 EnumType.STRING으로 사용해야 함!
    private DeliveryStatus status;  //ready, comp

}
