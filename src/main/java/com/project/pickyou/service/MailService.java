package com.project.pickyou.service;


import com.project.pickyou.dto.MailDto;

public interface MailService {


    public void sendSimpleMessage(MailDto mailDto);

    public boolean verifyAuthCode(String address, String authCode);
}
