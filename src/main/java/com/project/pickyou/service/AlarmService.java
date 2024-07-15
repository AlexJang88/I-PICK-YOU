package com.project.pickyou.service;


import com.project.pickyou.dto.AlarmDTO;
import org.springframework.ui.Model;

public interface AlarmService {


    public void findList(Model model, String id, int pageNum);  //쪽지 받은 리스트

    public void findContent(Model model, String id, Long num ); //쪽지상세내용

    public void adminSpend(Model model, String id, int pageNum);  //관리자가 보낸 쪽지 내용 리스트

    public void adminSpendContent(Model model, String id, Long num);  //관리자가 보낸쪽지 상세내용

    public void insertOneUser(String id, String name, String message); //특정유저한테만 쪽지

    public void insertManyUser(String id, String name, String message, String type); //다양한 유정한테 보내기
}
