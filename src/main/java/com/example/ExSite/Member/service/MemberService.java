package com.example.ExSite.Member.service;

import com.example.ExSite.Member.domain.Member;
import com.example.ExSite.Member.dto.OAuthAttributes;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;

public interface MemberService extends OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    @Override
    OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException;
    Member saveOrUpdate(OAuthAttributes attributes);
    Long withdraw(Member member);
    List<Member> findMembers();

    Member findByUserId(String userId);
    public Long findIdByToken(Object isToken);
}
