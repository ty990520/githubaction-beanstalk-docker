package com.jpabook.jpashop.web;

import com.jpabook.jpashop.domain.OrderSearch;
import com.jpabook.jpashop.service.ItemService;
import com.jpabook.jpashop.service.MemberService;
import com.jpabook.jpashop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping("/order")
    public String createForm(Model model){
        model.addAttribute("members",memberService.findMembers());
        model.addAttribute("items",itemService.findItems());
        return "order/orderForm";
    }

    @PostMapping("/order")
    public String order(@RequestParam("memberId")Long memberId,
                        @RequestParam("itemId")Long itemId,
                        @RequestParam("count")int count){
        orderService.order(memberId,itemId,count);
        return "redirect:/orders";
    }

    @GetMapping("/orders")
    public String orderList(@ModelAttribute("orderSearch")OrderSearch orderSearch, Model model){
        model.addAttribute("orders",orderService.findOrders(orderSearch));
        return "order/orderList";
    }

    @PostMapping("/orders/{orderId}/cancel")
    public String cancelOrder(@PathVariable("orderId")Long orderId){
        orderService.cancel(orderId);
        return "redirect:/orders";
    }
}
