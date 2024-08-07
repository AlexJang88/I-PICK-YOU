package com.project.pickyou.controller.all;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.project.pickyou.dto.AlarmDTO;

import com.project.pickyou.dto.CompanyInfoDTO;
import com.project.pickyou.dto.MemberDTO;
import com.project.pickyou.service.AlarmService;

import com.project.pickyou.service.CalendarService;
import com.project.pickyou.service.LoginService;
import com.project.pickyou.service.RecruitStateService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/ajax")
@RequiredArgsConstructor
public class AjaxController {


    private final LoginService loginService;
    private final AlarmService alarmService;
    private final CalendarService calendarService;

    //알람 컨트롤러 (회원별 알람 수)
    @PostMapping("/alarmnumber/number")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getAlertCount(@RequestBody Map<String, String> requestBody) {
        String id = requestBody.get("id"); // JSON 데이터에서 "id" 값을 추출

        JsonObject names = alarmService.getAlarmCount(id);

        // JSON 객체를 Map으로 변환
        Map<String, Object> response = new Gson().fromJson(names, Map.class);

        return ResponseEntity.ok(response);
    }

    //아이디중복체크
   @PostMapping("/idcheck")
   @ResponseBody
    public ResponseEntity<Map<String, String>> idcheck(@RequestBody MemberDTO dto){

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


    //알람보낼 시 회원 유무 체크
    @PostMapping("/usercheck")
    @ResponseBody
    public ResponseEntity<Map<String, String>> usercheck(@RequestBody MemberDTO dto){

        /*제이슨 객체생성*/

        boolean isExist = loginService.userCheckIFIdExists(dto.getId());
        Map<String, String> result = new HashMap<>();
        if (isExist) {
            result.put("message", "존재하는 회원입니다.");
        } else {
            result.put("message", "존재하지 않는 회원입니다.");
        }

        return ResponseEntity.ok(result);
    }



    //이메일 중복체크
    @PostMapping("/emailcheck")
    @ResponseBody
    public ResponseEntity<Map<String, String>> emailcheck(@RequestBody MemberDTO dto){

        /*제이슨 객체생성*/

        boolean isExist = loginService.checkIfEmailExists(dto.getEmail());
        Map<String, String> result = new HashMap<>();
        if (isExist) {
            result.put("message", "중복된 이메일이 있습니다.");
        } else {
            result.put("message", "사용할 수 있는 이메일입니다.");
        }


        return ResponseEntity.ok(result);
    }

    //사업자번호체크
    @PostMapping("/corpnocheck")
    @ResponseBody
    public ResponseEntity<Map<String, String>> corpnocheck(@RequestBody CompanyInfoDTO dto){

        /*제이슨 객체생성*/

        boolean isExist = loginService.checkIfcorpnocheck(dto.getCorpno());
        Map<String, String> result = new HashMap<>();
        if (isExist) {
            result.put("message", "중복된 사업자가 있습니다.");
        } else {
            result.put("message", "사용할 수 있는 사업자입니다.");
        }


        return ResponseEntity.ok(result);
    }

    @PostMapping("/calendar")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> calendar(@RequestParam("memberId") String memberId ){
        JsonObject list = calendarService.getCalendarData(memberId);
        Map<String,Object> result = new Gson().fromJson(list,Map.class);

        return ResponseEntity.ok(result);
    }



}
