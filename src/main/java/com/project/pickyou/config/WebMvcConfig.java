package com.project.pickyou.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${img.upload.path}")
    private String imgUploadPath;

    @Value("${contract.upload.path}")
    private String contractUploadPath;

    @Value("${profile.upload.path}")
    private String profileUploadPath;

    @Value("${upload.path}")
    private String uploadPath;

    @Override

    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler("/kjwupload/**")
                .addResourceLocations("file:///C:/Users/upload/");
       registry.addResourceHandler("/upload/**").
               addResourceLocations("file:///Users/jang-uiseog/Documents/upload/");

        // registry.addResourceHandler("/upload/**")
       //         .addResourceLocations("file:///C:/Users/senar/Desktop/upload/");

    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:///" + uploadPath + "/");


    }
}
