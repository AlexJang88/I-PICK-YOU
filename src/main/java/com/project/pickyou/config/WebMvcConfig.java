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

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/image/**")
                .addResourceLocations("file:///" + imgUploadPath + "/");

        registry.addResourceHandler("/upload/contract/**")
                .addResourceLocations("file:///" + contractUploadPath + "/");

        registry.addResourceHandler("/upload/profile/**")
                .addResourceLocations("file:///" + profileUploadPath + "/");
    }
}
