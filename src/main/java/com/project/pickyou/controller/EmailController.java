package com.project.pickyou.controller;

import com.project.pickyou.dto.MailDto;
import com.project.pickyou.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class EmailController {

    private final MailService mailService;

    @GetMapping("/mail/send")
    public String main() {
        return "mail/SendMail"; // 파일 확장자는 적지 않습니다. Spring Boot가 자동으로 .html을 추가합니다.
    }



    @PostMapping("/mail/sendAuthCode")   ///메일로 인증번호 보내기
    @ResponseBody
    public ResponseEntity<?> sendAuthCode(@RequestBody MailDto mailDto) {
        mailService.sendSimpleMessage(mailDto);
        return ResponseEntity.ok(Map.of("message", "인증 코드가 이메일로 전송되었습니다."));
    }


    @PostMapping("/mail/verifyAuthCode")
    @ResponseBody
    public ResponseEntity<?> verifyAuthCode(@RequestBody Map<String, String> request) {
        boolean isVerified = mailService.verifyAuthCode(request.get("email"), request.get("authCode"));
        if (isVerified) {
            return ResponseEntity.ok(Map.of("message", "인증에 성공했습니다."));
        } else {
            return ResponseEntity.status(400).body(Map.of("message", "인증 코드가 틀렸습니다."));
        }
    }





}
