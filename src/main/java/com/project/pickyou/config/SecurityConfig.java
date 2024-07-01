package com.project.pickyou.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public RoleHierarchy roleHierarchy(){
        RoleHierarchyImpl hierarchy=new RoleHierarchyImpl();
        hierarchy.setHierarchy("ROLE_ADMIN>ROLE_USER");
        hierarchy.setHierarchy("ROLE_ADMIN>ROLE_BUSINESS");
        return hierarchy;
    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){

        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .authorizeHttpRequests((auth)->auth
                        .requestMatchers("/login","/join").permitAll()
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/uploadPath/**").permitAll()
                        .requestMatchers("/my/**").hasAnyRole("USER","ADMIN")
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .anyRequest().permitAll()
                );

        http
                .formLogin(auth->auth.loginPage("/login")
                        .loginProcessingUrl("/loginProc")
                        .permitAll()
                );
        http
                .csrf((auth)->auth.disable());

        http.sessionManagement((auth) -> auth
                .sessionFixation().changeSessionId());

        //Logout
        http.logout((auth) -> auth.logoutUrl("/logout")
                .logoutSuccessUrl("/"));
        return http.build();
    }
}
