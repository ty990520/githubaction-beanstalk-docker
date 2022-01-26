package com.jpabook.jpashop.service;

import com.jpabook.jpashop.domain.Member;
import com.jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)  /*데이터 변경이 일어나는 경우는 항상 트랜잭션 붙임*/
//조회가 일어나는 경우는 readOnly로 하면 성능 최적화
@RequiredArgsConstructor    //@RequiredArgsConstructor => final필드나 nonNull필드에 대해 생성자 만듦
//@RequiredArgsConstructor사용하면 @Autowired필요x
public class MemberService {
    //@Autowired  //--> 변경 불가능 (애플리케이션 동작 시에 사용)
    private final MemberRepository memberRepository;

    /*setter injection --> 변경 가능 (테스트 시점에 사용)
    @Autowired
    public void setMemberRepository(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }*/

    /*생성자 injection --> 생성할 때 변경 (애플리케이션 + 테스트 시에 유용)*/
    /*public MemberService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    } => @RequiredArgsConstructor로 대체 가능*/

    //회원가입
    @Transactional
    public Long join(Member member){
        //1. 중복회원 검증
        validateDuplicateMember(member);    //중복회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    //(1) 중복회원 검증
    private void validateDuplicateMember(Member member){
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //회원 전체 조회
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    //회원 한 건 조회
    public Member findMember(Long memberId){
        return memberRepository.findOne(memberId);
    }


}
