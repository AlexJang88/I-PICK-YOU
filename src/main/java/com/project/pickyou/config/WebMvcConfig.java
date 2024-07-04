package com.project.pickyou.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Value("${img.upload.path}")      //프로퍼티스의 이름이 이거다
    private String imgUploadPath;    //imgUploadPath 필드를 선언하고, @Value 애노테이션을 사용하여 프로퍼티 파일에서 img.upload.path 값을 주입 [출처] jpa 사진 넣기 꺼내기

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) { //사진을 찾아오는 경로
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:///C:/Users/upload/"); ///upload/** 경로로 요청된 리소스를 C:/Users/upload/ 경로에 있는 파일로 매핑
    }
}