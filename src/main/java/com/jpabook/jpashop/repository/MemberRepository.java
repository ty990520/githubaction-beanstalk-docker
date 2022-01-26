package com.jpabook.jpashop.repository;

import com.jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor    //@PersistenceContext를 대체함
public class MemberRepository/* extends JpaRepository<Member, Long> */{

    //원래 스프링에서 EntityManager는 의존성 주입시에 Autowired사용x -> @PersistenceContext사용해야함
    //스프링부트가 Autowired를 지원해줘서 생성자 주입도 가능함
    //생성자 주입을 @RequiredArgsConstructor가 함
    private final EntityManager em;

    //저장
    public void save(Member member){
        em.persist(member);
    }

    //단건 조회
    public Member findOne(Long id){
        return em.find(Member.class,id);    //Member.class:반환타입
    }

    //리스트 조회
    public List<Member> findAll(){  //JPQL작성 필요
        return em.createQuery("select m from Member m",Member.class).getResultList();
    }

    //검색
    public List<Member> findByName(String name){
        return em.createQuery("select m from Member m where m.name = :name",Member.class)
                .setParameter("name",name)  //파라미터에 바인딩
                .getResultList();
    }
}
