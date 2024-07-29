package com.project.pickyou.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.project.pickyou.entity.ConfirmEntity;
import com.project.pickyou.entity.MemberEntity;
import com.project.pickyou.entity.RecruitEntity;
import com.project.pickyou.entity.RecruitStateEntity;
import com.project.pickyou.repository.ConfirmJPARepository;
import com.project.pickyou.repository.MemberJPARepository;
import com.project.pickyou.repository.RecruitJPARepository;
import com.project.pickyou.repository.RecruitStateJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CalendarServiceImpl implements CalendarService{

    private final RecruitStateJPARepository recruitStateJPA;
    private final ConfirmJPARepository confirmJPA;
    private final MemberJPARepository memberJPA;
    private final RecruitJPARepository recruitJPA;

    @Override
    public JsonObject getCalendarData(String id) {
        JsonObject jsonObject = new JsonObject();
        JsonArray confirmList = new JsonArray();
        JsonArray applyList = new JsonArray();
        int auth=0;
        List<RecruitStateEntity> reList = recruitStateJPA.findByMemberId(id);

        Optional<MemberEntity> opmem = memberJPA.findById(id);
        if(opmem.isPresent()){
            if(opmem.get().getAuth().contains("USER")){
                auth=1;
            } else if (opmem.get().getAuth().contains("COMPANY")) {
                auth=2;
            }

        }
        if(auth==1) {
            if (!reList.isEmpty()) {
                for (RecruitStateEntity rse : reList) {
                    JsonObject obj = new JsonObject();
                    String date = rse.getReg().toString();
                    obj.addProperty("auth",auth);
                    obj.addProperty("title", "공고 지원(" + rse.getRecruit().getTitle() + ")");
                    obj.addProperty("date", date);
                    applyList.add(obj);
                }
            }
            List<ConfirmEntity> conList = confirmJPA.findByMemberId(id);
            if (!conList.isEmpty()) {
                for (ConfirmEntity ce : conList) {
                    JsonObject obj = new JsonObject();
                    String date = ce.getReg().toString();
                    obj.addProperty("auth",auth);
                    obj.addProperty("title", "채용 확정("+ce.getCompanyId()+")");
                    obj.addProperty("date", date);
                    obj.addProperty("url","mypage/confirm/posts");
                    confirmList.add(obj);
                }
            }

        } else if (auth==2) {
            List<ConfirmEntity> conList = confirmJPA.findByCompanyId(id);
            List<RecruitEntity> rList = recruitJPA.findByMemberId(id);
            if(!rList.isEmpty()){
                for(RecruitEntity re:rList){
                    JsonObject obj= new JsonObject();
                    String date = re.getReg().toString();
                    obj.addProperty("auth",auth);
                    obj.addProperty("title","공고등록("+re.getTitle()+")");
                    obj.addProperty("recruitId",re.getId());
                    obj.addProperty("date",date);
                    applyList.add(obj);
                }
            }  if (!conList.isEmpty()) {
                for (ConfirmEntity ce : conList) {
                    JsonObject obj = new JsonObject();
                    String date = ce.getReg().toString();
                    obj.addProperty("auth",auth);
                    obj.addProperty("title", "채용 확정("+ce.getMemberId()+")");
                    obj.addProperty("date", date);
                    obj.addProperty("url","mypage/company/confirmation/posts");
                    confirmList.add(obj);
                }
            }
        }
        jsonObject.add("confirm", confirmList);
        jsonObject.add("apply", applyList);
        return jsonObject;
    }

}
