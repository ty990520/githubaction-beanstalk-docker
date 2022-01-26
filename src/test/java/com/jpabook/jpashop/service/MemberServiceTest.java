package com.jpabook.jpashop.service;

import com.jpabook.jpashop.domain.Member;
import com.jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)    //junit+spring 사용하는 경우
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager em;

    //@Rollback(false)    /*테스트 케이스에서는 트랜잭션이 끝나면 롤백해버림*/
    @Test
    public void 회원가입() throws Exception {
        //Given (input이 주어졌을 때)
        Member member = new Member();
        member.setName("taeong");

        //When (이렇게 하면)
        Long saveId = memberService.join(member);

        //Then (결과가 이렇게 된다 (검증))
        //em.flush(); //DB에 강제로 쿼리 전달
        assertEquals(member, memberRepository.findOne(saveId)); //저장한 멤버가 실제 DB에 있는 값과 같은지 확인
    }

    //@Rollback(false)
    @Test(expected = IllegalStateException.class)   //회원가입 테스트
    public void 중복_회원_예외() throws Exception{
        //given
        Member member1 = new Member();
        member1.setName("dong");

        Member member2 = new Member();
        member2.setName("dong");

        //when
        memberService.join(member1);
        /*try{
            memberService.join(member2);    //예외 발생해야함
        }catch (IllegalStateException e){
            return;
        }*/
        memberService.join(member2);

        //then
        fail("예외가 발생해야 한다.");   //이 코드가 실행되면 안됨!
    }
}