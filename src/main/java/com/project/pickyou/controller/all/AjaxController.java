package com.project.pickyou.controller.all;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.project.pickyou.dto.AlarmDTO;

import com.project.pickyou.dto.MemberDTO;
import com.project.pickyou.service.AlarmService;

import com.project.pickyou.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/ajax")
@RequiredArgsConstructor
public class AjaxController {


    private final LoginService loginService;
    private final AlarmService alarmService;







    //알람 컨트롤러
    @PostMapping("/alarmnumber/number")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getAlertCount(@RequestBody Map<String, String> requestBody){
        String id = requestBody.get("id"); // JSON 데이터에서 "id" 값을 추출

       JsonObject names = alarmService.getAlarmCount(id);

        // JSON 객체를 Map으로 변환
        Map<String, Object> response = new Gson().fromJson(names, Map.class);

        return ResponseEntity.ok(response);
    }










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
