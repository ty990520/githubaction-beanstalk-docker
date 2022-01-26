package com.jpabook.jpashop.repository;

import com.jpabook.jpashop.domain.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor    //final이랑 한 세트라고 생각하기
public class ItemRepository {
    private final EntityManager em;

    public void save(Item item){
        if(item.getId()==null){ //신규 아이템 등록
            em.persist(item);   //Persist는 최초 생성된 Entity를 영속화
        }else{  //이미 데이터베이스에 저장된 엔티티를 수정
            em.merge(item); //Detached 상태의 Entity를 다시 영속화
        }
    }

    public Item findOne(Long id){
        return em.find(Item.class, id);
    }

    public List<Item> findAll(){    //전체조회는 createQuery사용! ("jpql",리턴타입).getResultList()
        return em.createQuery("select i from Item i", Item.class).getResultList();
    }
}
