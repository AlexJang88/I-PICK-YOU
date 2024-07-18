package com.project.pickyou.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString

public class KakaoPayDTO {

    private String cid;  //테스트용아이디
    private String partnerOrderId; //주문번호
    private String partnerUserId; //회원이름
    private String itemName;  // 상품명
    private int quantity;    //수량
    private int totalAmount; // 상품가격
    private int taxFreeAmount;  //비과세금액
    private int point;

    private String next_redirect_pc_url; // web - 받는 결제 페이지


}
