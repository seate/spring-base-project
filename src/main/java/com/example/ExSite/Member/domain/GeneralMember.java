package com.example.ExSite.Member.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GeneralMember implements OAuth2User, UserDetails {

    private Member member;
    private Map<String, Object> oauthUserAttributes;

    //생성자를 private로 지정하고 public인 create 2가지를 통해 생성하는 방식
    private GeneralMember(Member member, Map<String, Object> oauthUserAttributes) {
        this.member = member;
        this.oauthUserAttributes = oauthUserAttributes;
    }

    //TODO default 로그인, 아직 구현 안함
    public static GeneralMember create(Member member){
        return new GeneralMember(member, new HashMap<>());
    }

    // oauth 로그인
    public static GeneralMember create(Member member, Map<String, Object> oauthUserAttributes){
        return new GeneralMember(member, oauthUserAttributes);
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return member.getRoleKey();
            }
        });

        return collection;
    }

    public Member getMember() {
        return member;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oauthUserAttributes;
    }

    @Override
    public String getName() {
        return member.getName();
    }


    @Override
    public String getUsername() {
        return member.getUserId();
    }


    @Override
    public String getPassword() {
        return member.getPasswd();
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
