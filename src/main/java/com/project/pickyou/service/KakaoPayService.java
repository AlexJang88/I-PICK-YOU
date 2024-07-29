package com.project.pickyou.service;


import com.project.pickyou.dto.KakaoPayDTO;
import com.project.pickyou.dto.PaymentDTO;
import com.project.pickyou.entity.PaymentEntity;
import com.project.pickyou.entity.PointEntity;
import com.project.pickyou.repository.PaymentJPARepository;
import com.project.pickyou.repository.PointJPARepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
@Log
public class KakaoPayService {

    private final PaymentJPARepository paymentJPARepository;
    private final PointJPARepository pointJPARepository;
    private static final String Host = "https://kapi.kakao.com";

    @Value("${kakao.admin}")
    private String kakaoAdminKey;

    private KakaoPayDTO kakaoPayDTO;

    public String kakaoPayReady(KakaoPayDTO kakaoPayDTO, HttpSession session) {
        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());

        // Server Request Header : 서버 요청 헤더
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK " + kakaoAdminKey); // 어드민 키
        headers.add("Accept", "application/json");
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // Server Request Body : 서버 요청 본문
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        PaymentEntity paymentEntity = createPaymentEntity(kakaoPayDTO); // paymentEntity 생성 이건 db에 설정할 값

        long totalCount = paymentJPARepository.count()+1; // 현재 번호를 설정하기 위해
        kakaoPayDTO.setPartnerOrderId(String.valueOf(totalCount)); //주문번호설정

        params.add("cid", "TC0ONETIME"); // 가맹점 코드 - 테스트용
        params.add("partner_order_id", kakaoPayDTO.getPartnerOrderId());  //주문번호
        params.add("partner_user_id", kakaoPayDTO.getPartnerUserId());    //회원아이디
        params.add("item_name", kakaoPayDTO.getItemName());        //상품 명
        params.add("quantity", String.valueOf(kakaoPayDTO.getQuantity()));    //상품수량
        params.add("total_amount", String.valueOf(kakaoPayDTO.getTotalAmount()));  //상품가격
        params.add("tax_free_amount", String.valueOf(kakaoPayDTO.getTaxFreeAmount()));   //비과세 금액
        params.add("approval_url", "http://3.39.50.114:8080/payProcess/PaySuccess"); // 성공시 url
        params.add("cancel_url", "http://3.39.50.114:8080/payProcess/PayCancle"); // 실패시 url
        params.add("fail_url", "http://3.39.50.114:8080/payProcess/PayFail");

        // 헤더와 바디 붙이기
        HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<>(params, headers);
        //이제진행
        try {
            kakaoPayDTO = restTemplate.postForObject(new URI(Host + "/v1/payment/ready"), body, KakaoPayDTO.class);
            session.setAttribute("paymentEntity", paymentEntity);  // 세션에 db 들어갈 정보 넣기

            return kakaoPayDTO.getNext_redirect_pc_url();

        } catch (RestClientException | URISyntaxException e) {
            e.printStackTrace();
        }
        return "payProcess/pay";
    }


    // paymentEntity 생성 메서드
    private PaymentEntity createPaymentEntity(KakaoPayDTO kakaoPayDTO) {
        int pointHistory = 1;  //1결제 / 2출금
        PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setMemberId(kakaoPayDTO.getPartnerUserId()); // 회원아이디
        paymentEntity.setMoney(kakaoPayDTO.getTotalAmount()); // 결제금액
        paymentEntity.setPoint(kakaoPayDTO.getPoint());   // 포인트금액
        paymentEntity.setTitle(kakaoPayDTO.getItemName());   // 결제제목
        paymentEntity.setPointHistory(pointHistory);  // 포인트사용내역

        return paymentEntity;
    }



    public void savepay(PaymentEntity paymentEntity) { //DB에 결제값 넣기
        paymentJPARepository.save(paymentEntity);
    }


    public void usePoint(PaymentEntity paymentEntity) {  //DB에 포인트 넣기
        PointEntity pointEntity = new PointEntity(); //생성
        int status = 2; //적립 1// 차감 2
        pointEntity.setMemberId(paymentEntity.getMemberId());
        pointEntity.setPoint(paymentEntity.getPoint());
        pointEntity.setStatus(status);
        pointJPARepository.save(pointEntity);  //포인트 차감 진행

    }



   public int savedMoney(String user) { //본인의 보유 포인트
       int save = 1;  //적립
       int deducted = 2;  //차감

       Integer savePoint = pointJPARepository.findTotalPointByMemberIdAndStatus(user, save);  // 적립된 포인트 합계
       Integer deductedPoint = pointJPARepository.findTotalPointByMemberIdAndStatus(user, deducted); // 차감된 포인트 합계

       if(savePoint == null){
           savePoint = 0;
       }

       if(deductedPoint == null){
           deductedPoint =0;
       }

        // 실제 보유 포인트
        int realPoint = savePoint - deductedPoint;

        return realPoint;
    }





}

