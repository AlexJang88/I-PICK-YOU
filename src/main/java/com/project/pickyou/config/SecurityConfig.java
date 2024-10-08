package com.project.pickyou.config;

import com.project.pickyou.handler.SecurityHandler;
import org.springframework.beans.factory.annotation.Autowired;
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
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy("ROLE_ADMIN>ROLE_USER");
        hierarchy.setHierarchy("ROLE_ADMIN>ROLE_BUSINESS");
        return hierarchy;
    }


    //에러처리를 위해 필요한 코드
    private final SecurityHandler securityHandler;

    @Autowired
    public SecurityConfig(SecurityHandler securityHandler) {
        this.securityHandler = securityHandler;
    }


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http


                .authorizeHttpRequests((auth)->auth



                        .requestMatchers("/","/login","/joinCompany","/forgot/**","/mail/**","/posts/**","/join","/joinProc","/assets/**", "/img/**","/register","/css/**", "/js/**").permitAll()
                        .requestMatchers("/login","/ajax/**","/forgotId/**","/forgotPw/**","/notice/**","/payProcess/**").permitAll()
                        .requestMatchers("/qa/**").permitAll()
                                .requestMatchers("/educations/posts").permitAll()
                                .requestMatchers("/recruit/posts").permitAll()
                                .requestMatchers("/teams/posts").permitAll()
                                .requestMatchers("recruit/posts/{boardNum}").permitAll()
                                .requestMatchers("educations/posts/{boardNum}").permitAll()
                                .requestMatchers("teams/posts/{boardNum}").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/trainning/posts/new","/agency/posts/new","/agency/posts/*/edit","/trainning/posts/*/edit").hasRole("COMPANY")
                        .requestMatchers("/agency/**","/trainning/**").permitAll()
                        .requestMatchers("/foodMap/new").hasAnyRole("USER","COMPANY")
                        .requestMatchers("/resume/new","/mypage/user/receive/posts").hasAnyRole("USER")
                                .requestMatchers("/mypage/**").hasAnyRole("USER","ADMIN","COMPANY")
                                .requestMatchers("/foodMap/**","/resume/**").permitAll()
                        .requestMatchers("/pick/**").authenticated()
                        .requestMatchers("/recruitState/**").authenticated()
                        .requestMatchers("/satisfaction/**").authenticated()
                        .anyRequest().authenticated()




                        /*.anyRequest().permitAll()*/
                );

        http
                .formLogin(auth -> auth.loginPage("/login")
                        .loginProcessingUrl("/loginProc")
                        .permitAll()
                );



        http
                .csrf((auth) -> auth.disable());


        //세션 보호 목적
        http.sessionManagement((auth) -> auth
                .sessionFixation().changeSessionId());

        //Logout
        http.logout((auth) -> auth.logoutUrl("/logout")
                .logoutSuccessUrl("/"));


        //error
        http.exceptionHandling((auth) -> auth
                .accessDeniedHandler(securityHandler)
                .authenticationEntryPoint((request, response, authException) ->
                        response.sendRedirect("/login?error=true"))
        );


        return http.build();
    }


}