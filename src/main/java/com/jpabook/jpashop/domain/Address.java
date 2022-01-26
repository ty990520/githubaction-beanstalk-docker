package com.jpabook.jpashop.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable //어딘가에 내장이 될 수 있음
@Getter //'값 타입'은 생성할 때만 값이 생성되고 setter는 제공하지 않음 -> 값을 변경할 수 없게 함
public class Address {

    private String city;
    private String street;
    private String zipcode;

    protected Address(){

    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
