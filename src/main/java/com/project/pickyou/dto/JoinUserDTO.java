package com.project.pickyou.dto;

import com.project.pickyou.entity.MemberEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class JoinUserDTO implements UserDetails {

    private MemberEntity memberEntity;

    public JoinUserDTO(MemberEntity memberEntity){
        this.memberEntity = memberEntity;
    }


    //권한과 아이디를 확인해서 준다
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {

                return memberEntity.getAuth();   //디비속 권한을 찾아서 준다
            }
        });
        return collection;
    }

    @Override
    public String getPassword() {
        return memberEntity.getPw();
    }

    @Override
    public String getUsername() {
        return memberEntity.getId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
