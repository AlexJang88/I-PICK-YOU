package com.project.pickyou.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ItextPdfDto {
    private Long contractId; // pdf 종류가 여러개일 경우 html 을 구분하기 위한 코드
    private String pdfFilePath; // pdf 파일이 저장될 경로
    private String pdfFileName; // pdf 파일명
}
