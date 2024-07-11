package com.project.pickyou.controller.all;


import com.project.pickyou.dto.MemberDTO;
import com.project.pickyou.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/ajax")
@RequiredArgsConstructor
public class AjaxController {


    private final LoginService loginService;
    //아이디 중복값 체크

    @PostMapping("/idcheck")
    @ResponseBody
    public ResponseEntity<Map<String, String>> idcheck(@RequestBody MemberDTO dto){

        System.out.println("받은 아이디: " + dto.getId());
        /*제이슨 객체생성*/

        boolean isExist = loginService.checkIfIdExists(dto.getId());
        Map<String, String> result = new HashMap<>();
        if (isExist) {
            result.put("message", "중복된 아이디가 있습니다.");
        } else {
            result.put("message", "사용할 수 있는 아이디입니다.");
        }


        return ResponseEntity.ok(result);
    }




}
