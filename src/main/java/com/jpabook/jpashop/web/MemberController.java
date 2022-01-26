package com.jpabook.jpashop.web;

import com.jpabook.jpashop.domain.Address;
import com.jpabook.jpashop.domain.Member;
import com.jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping(value = "/members/new")
    public String createForm(Model model){
        model.addAttribute("memberForm",new MemberForm());
        return "members/createMemberForm";

    }

    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult result){
        if(result.hasErrors()){
            return "members/createMemberForm";  //에러 발생시 폼으로 다시 이동
        }
        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());
        Member member = new Member();
        member.setAddress(address);
        member.setName(form.getName());
        memberService.join(member);
        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model){
        model.addAttribute("members",memberService.findMembers());
        return "members/memberList";
    }
}
