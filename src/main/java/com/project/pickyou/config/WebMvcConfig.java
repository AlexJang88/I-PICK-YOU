
package com.project.pickyou.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {  //사진 넣는 경로
    @Value("${pimg.upload.path}")  //프로퍼티스의 이름이 이거다
    private String imgUploadPath;


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){  //사진을 찾아오는 경로
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:///C:/Users/upload/");  ///upload/** 경로로 요청된 리소스를 C:/Users/upload/ 경로에 있는 파일로 매핑
    }
}
