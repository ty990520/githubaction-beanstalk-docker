package com.jpabook.jpashop.service;

import com.jpabook.jpashop.domain.*;
import com.jpabook.jpashop.repository.ItemRepository;
import com.jpabook.jpashop.repository.MemberRepository;
import com.jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    //주문    - 한 번에 하나의 상품만 주문할 수 있다.
    @Transactional
    public Long order(Long memberId, Long itemId, int count){

        //엔터티 조회
        Member member = memberRepository.findOne(memberId);
        Item item =  itemRepository.findOne(itemId);

        //배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        delivery.setStatus(DeliveryStatus.READY);

        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item,item.getPrice(),count);
        //OrderItem.createOrderItem() -> static 메소드 호출 시, 클래스.함수로 사용해야함

        //주문 생성
        Order order = Order.createOrder(member,delivery,orderItem);

        //주문 저장
        orderRepository.save(order);
        return order.getId();
    }

    //주문 취소
    @Transactional
    public void cancel(Long orderId){
        Order order = orderRepository.findOne(orderId);
        order.cancel();
    }

    //주문 검색

    public List<Order> findOrders(OrderSearch orderSearch){
        return orderRepository.findAll(orderSearch);
    }
}
