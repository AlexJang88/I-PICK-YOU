package com.project.pickyou.controller.company;


import com.project.pickyou.dto.KakaoPayDTO;
import com.project.pickyou.entity.PaymentEntity;
import com.project.pickyou.service.KakaoPayService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Log
@RequestMapping("/payProcess/*")
public class KakaoController {

    private final KakaoPayService kakaoPay;


    @GetMapping("/Pay/{user}")
    public String kakaoPayGet(@PathVariable String user, Model model, Principal pc){  //결제 페이지
        String prid = pc.getName();

        if(user.equals(prid)){
            int savedMoney =  kakaoPay.savedMoney(user); //본인의 적릭금*/
            model.addAttribute("user",user);
            model.addAttribute("savedMoney",savedMoney);
            return "payProcess/Pay";
        }else {
            return "/login";
        }

    }


    @PostMapping("/Pay")  //카카오결제되는 부분
    public String kakaoPay(KakaoPayDTO kakaoPayDTO, HttpSession session){

        return "redirect:" + kakaoPay.kakaoPayReady(kakaoPayDTO, session);
    }

    @GetMapping("/PaySuccess")   //성공시
    public void kakaoPaySuccess(@RequestParam("pg_token")String pg_token, Model model, HttpSession session) {

        PaymentEntity paymentEntity = (PaymentEntity) session.getAttribute("paymentEntity");  //세션으로 값 가져옴
        kakaoPay.savepay(paymentEntity); //결제 넣기

        if(paymentEntity.getPoint() > 0){
            kakaoPay.usePoint(paymentEntity);//포인트 차감
        }

        String user = paymentEntity.getMemberId(); // 회원아이디
        int money = paymentEntity.getMoney(); // 결제금액
        String title= paymentEntity.getTitle();  // 결제제목

        model.addAttribute("user",user);
        model.addAttribute("money",money);
        model.addAttribute("title",title);
    }


    @GetMapping("/PayCancle")  //실패시
    public void kakaoPayCancle(@RequestParam("pg_token")String pg_token, Model model) {


    }


}
