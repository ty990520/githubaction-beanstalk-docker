package com.jpabook.jpashop.service;

import com.jpabook.jpashop.domain.Item;
import com.jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)  //서비스는 항상 트랜잭션 붙이기
@RequiredArgsConstructor
public class ItemService {  //상품 서비스는 상품 리포지토리에 단순히 위임만 하는 클래스
    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item){
        itemRepository.save(item);
    }

    public List<Item> findItems(){
        return itemRepository.findAll();
    }

    public Item findOne(Long id){
        return itemRepository.findOne(id);
    }

    @Transactional  //영속성 컨텍스트가 자동으로 변경
    public void updateItem(Long id, String name, int price){
        Item item = itemRepository.findOne(id);
        item.setName(name);
        item.setPrice(price);
    }
}
