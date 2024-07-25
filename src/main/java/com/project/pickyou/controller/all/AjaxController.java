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
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    //알람 컨트롤러
    @PostMapping("/alarmnumber/number")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getAlertCount(@RequestBody Map<String, String> requestBody) {
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

    @PostMapping("/emailcheck")
    @ResponseBody
    public ResponseEntity<Map<String, String>> emailcheck(@RequestBody MemberDTO dto){

        System.out.println("받은 아이디: " + dto.getEmail());
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

    @PostMapping("/corpnocheck")
    @ResponseBody
    public ResponseEntity<Map<String, String>> corpnocheck(@RequestBody CompanyInfoDTO dto){

        System.out.println("받은 사업자번호: " + dto.getCorpno());
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
        System.out.println("-------------------------memberId"+memberId);
        JsonObject list = calendarService.getCalendarData(memberId);
        System.out.println("-------------------------list"+list);
        Map<String,Object> result = new Gson().fromJson(list,Map.class);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/test")
    @ResponseBody
    public String testasdf(){
        String result="";
        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE,-1);
        result = sf.format(cal.getTime());

    return result;
            }


}
