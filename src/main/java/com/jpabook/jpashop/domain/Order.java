package com.jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity //jpa가 테이블 자동생성해줌
@Table(name="orders")
@Getter @Setter
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")  //DB칼럼명이랑 매핑
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)  //order쪽이 다, member쪽이 일
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="delivery_id") //일대일 관계에서 주인이 되는 경우
    private Delivery delivery;  //배송정보

    //Date타입 + 날짜관련 어노테이션 매핑 필요함
    //자바8부터 LocalDateTime를 사용하면 hibernate가 같은 기능을 지원해줌
    private LocalDateTime orderDate;    //주문 시간

    @Enumerated(EnumType.STRING)   //enum을 사용하는 경우에 붙여줌
    private OrderStatus status; //enum타입    //주문상태 [order, cancel]


    //연관관계 메소드
    public void setMember(Member member){
        this.member = member;
        member.getOrders().add(this);   //멤버의 주문정보에 새 주문정보 추가
    }

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);   //주문정보에 아이템 setting
    }

    public void addDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);    //주문정보에 배송정보 setting
    }


    //생성메소드
    public static Order createOrder(Member member,Delivery delivery,OrderItem... orderItems){   //이건 무슨 문법이지?
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for(OrderItem orderItem : orderItems){
            order.addOrderItem(orderItem);  //잘 모르겠음 왜 얘만 set이아니라 add로 넣지?
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());

        return order;
    }


    //비즈니스 로직
    public void cancel(){   //주문 취소
        //배송상태가 COMP(완료)인 경우 Exception 발생
        if(delivery.getStatus() == DeliveryStatus.COMP){
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }
        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem:orderItems){
            orderItem.cancel();
        }

    }

    // 조회 로직
    public int getTotalPrice(){ //전체 주문 가격 조회
        int totalPrice = 0;
        for(OrderItem orderItem : orderItems){
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }
}
