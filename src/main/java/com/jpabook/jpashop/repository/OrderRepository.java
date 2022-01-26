package com.jpabook.jpashop.repository;

import com.jpabook.jpashop.domain.Order;
import com.jpabook.jpashop.domain.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order){ //상품 주문
        em.persist(order);
    }

    //주문 내역 조회
    public Order findOne(Long id){
        return em.find(Order.class, id);
    }


    //주문 검색 -> 검색 조건에 동적으로 쿼리를 생성해서 주문 엔티티를 조회
    //JPQL사용
    public List<Order> findAll(OrderSearch orderSearch){
        String jpql = "select o From Order o join o.member m";
        boolean isFirstCondition = true;

        //jpql세팅1 : 주문 상태로 검색
        if(orderSearch.getOrderStatus() != null){
            if(isFirstCondition){
                jpql += " where";
                isFirstCondition = false;
            }else{
                jpql += " and";
            }
            jpql += "o.status = :status";
        }

        //jpql세팅2 : 회원 이름으로 검색
        if(StringUtils.hasText(orderSearch.getMemberName())){
            if(isFirstCondition){
                jpql += " where";
                isFirstCondition = false;
            }else{
                jpql += " and";
            }
            jpql += "m.name like :name";
        }

        TypedQuery<Order> query = em.createQuery(jpql, Order.class).setMaxResults(1000); //최대 1000건

        if (orderSearch.getOrderStatus() != null) {
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            query = query.setParameter("name", orderSearch.getMemberName());
        }
        return query.getResultList();
   }
}
