package com.project.pickyou.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ContractDTO {
    private String startdate;
    private String enddate;
    private String location;
    private String job;
    private int workStartHour;
    private int workStartMinute;
    private int workEndHour;
    private int workEndMinute;
    private int worksShedule;
    private String wageType;
    private int wage;
    private String payDateTyope;
    private int payDate;
    private String wageInto;
    private String [] insurance;
    private String contractDate;
    private String companyCEO;
    private String companyAddress;
    private String companyName;
    private String companyContact;
    private String memberAddress;
    private String memberContact;
    private String memberName;




}
