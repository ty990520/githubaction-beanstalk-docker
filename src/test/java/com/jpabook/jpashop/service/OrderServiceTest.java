package com.jpabook.jpashop.service;

import com.jpabook.jpashop.domain.*;
import com.jpabook.jpashop.domain.item.Book;
import com.jpabook.jpashop.exception.NotEnoughStockException;
import com.jpabook.jpashop.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @PersistenceContext
    EntityManager em;

    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception{
        //given -> 테스트를 위한 회원과 상품을 만들기
        Member member = createMember();
        Item item = createBook("시골 jpa",10000,10);
        int orderCount = 2;

        //when -> 실제 상품을 주문하기
        Long orderId = orderService.order(member.getId(), item.getId(),orderCount);

        //then -> 주문 가격이 올바른지, 주문 후 재고 수량이 정확히 줄었는지 검증하기
        Order order = orderRepository.findOne(orderId);
        assertEquals("상품 주문시 상태는 ORDER", OrderStatus.ORDER, order.getStatus());
        assertEquals("주문한 상품 종류 수가 정확한지?",1,order.getOrderItems().size());
        assertEquals("총 가격 = 가격 * 수량", 10000*2,order.getTotalPrice());
        assertEquals("주문 수량만큼 재고가 줄어야 함",10-2,item.getStockQuantity());

    }

    @Test(expected = NotEnoughStockException.class)
    public void 상품주문_재고수량초과() throws Exception {
        //given
        Member member = createMember();
        Item item = createBook("시골 jpa",10000,10);
        int orderCount = 11;

        //when
        orderService.order(member.getId(), item.getId(),orderCount);

        //then
        fail("재고 수량 부족으로 예외가 발생해야 함");
    }

    @Test
    public void 주문취소() {
        //given -> 일단 주문이 존재해야 함
        Member member = createMember();
        Item item = createBook("시골 jpa",10000,10);
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), item.getId(),orderCount);

        //when
        orderService.cancel(orderId);

        //then
        Order order = orderRepository.findOne(orderId);
        assertEquals("주문 취소 상태는 CANCEL",OrderStatus.CANCEL,order.getStatus());
        assertEquals("재고 수량 증가 확인",10,item.getStockQuantity());
    }

    public Member createMember(){
        Member member = new Member();
        member.setName("태영");
        member.setAddress(new Address("인천","송도문화로","28-27"));
        em.persist(member); //저장
        return member;
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setStockQuantity(stockQuantity);
        book.setPrice(price);
        em.persist(book);
        return book;
    }
}