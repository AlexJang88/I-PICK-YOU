package com.project.pickyou.service;

import com.project.pickyou.dto.MailDto;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class MailServiceImpl implements MailService{

    @Autowired
    private JavaMailSender emailSender;


    private final Map<String, Integer> emailAuthCodes = new HashMap<>();

    @Override
    public void sendSimpleMessage(MailDto mailDto) {  //메세지 보내기

        int checkNum = makeRandomNumber();
        emailAuthCodes.put(mailDto.getEmail(), checkNum);  //이메일 주소와 랜덤값을 같이 담아서 보냄 (보낼때 인스턴스에 임시적으로 저장가능)

        SimpleMailMessage message = new SimpleMailMessage();
        String id= "jihwang3047@gmail.com";
        String title = "아이디/비밀번호 찾기 메일입니다."; // 이메일 제목
        String content =
                "홈페이지를 방문해주셔서 감사합니다." +
                        "인증 번호는 " + checkNum + "입니다." +
                        "해당 인증번호를 인증번호 확인란에 기입하여 주세요."; //이메일 내용 삽입


        message.setFrom(id); // `application.properties` 파일의 email과 동일해야 합니다.
        message.setTo(mailDto.getEmail());
        message.setSubject(title);
        message.setText(content);
        emailSender.send(message);  //메세지 보냄
    }


    // 6자리 랜덤 숫자를 생성하는 메서드
    public int makeRandomNumber() {
        Random r = new Random();
        int checkNum = r.nextInt(900000) + 100000; // 100000 ~ 999999 범위의 숫자 생성

        System.out.println("><<><>"+checkNum);

        return checkNum;
    }


    @Override
    public boolean verifyAuthCode(String email, String authCode) {
        Integer storedCode = emailAuthCodes.get(email);  //위에서 저장된 이메일과 인증번호 값을 가져옴
        return storedCode != null && storedCode.equals(Integer.parseInt(authCode));  //뷰에서 가져온 값이 랑 비교해서 인증
    }

}
