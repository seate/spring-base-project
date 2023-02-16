package com.example.ExSite.Member.service;

import com.example.ExSite.Member.domain.GeneralMember;
import com.example.ExSite.Member.domain.Member;
import com.example.ExSite.Member.dto.OAuthAttributes;
import com.example.ExSite.Member.repository.MemberRepository;
import com.example.ExSite.MemberToStudy.service.MemberToStudyService;
import com.example.ExSite.Study.domain.Study;
import jakarta.servlet.http.HttpSession;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class MemberServiceImplement implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberToStudyService memberToStudyService;
    private final HttpSession httpSession;

    @Autowired
    public MemberServiceImplement(MemberRepository memberRepository, MemberToStudyService memberToStudyService, HttpSession httpSession) {
        this.memberRepository = memberRepository;
        this.memberToStudyService = memberToStudyService;
        this.httpSession = httpSession;
    }

    //CREATE, UPDATE

    @Override // OAuth2에서 로그인을 담당
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);


        // OAuth2 서비스 id 구분코드 (구글, 네이버, 카카오 등)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        //OAuth2 로그인 시 키가 되는 필드값? (pk) (구글: "sub")
        String userNameAttributeName = userRequest
                .getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        Member member = saveOrUpdate(attributes);

        //TODO token 부분 개선?
        httpSession.setAttribute("tokenValue", userRequest.getAccessToken().getTokenValue());

        System.out.println();
        System.out.println(userRequest.getAccessToken().getExpiresAt());
        System.out.println(userRequest.getAccessToken().getTokenType());
        System.out.println(userRequest.getAccessToken().getScopes());
        System.out.println(userRequest.getAdditionalParameters());
        System.out.println();
        System.out.println(oAuth2User.getName());
        System.out.println(oAuth2User.getAttributes());

        return GeneralMember.create(member, attributes.getAttributes()); // @Authentication 등록
    }

    @Override
    public Member saveOrUpdate(OAuthAttributes attributes){
        Member member = memberRepository.findByUserId(attributes.getEmail())
                .map(member1 -> member1.update(attributes.getName(), attributes.getPicture()))
                .orElse(attributes.toEntity());

        return memberRepository.save(member);
    }

    //DELETE

    @Override
    public Long withdraw(Member member) {
        memberRepository.findByUserId(member.getUserId()).ifPresentOrElse(
                member1 -> {
                    revokeGoogleToken();

                    //이 멤버가 지워졌을 때의 study 등의 처리
                    try {
                        memberToStudyService.memberDeleted(member1);
                    }
                    catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    memberRepository.delete(member1);
                },
                () -> {
                    throw new IllegalStateException("삭제하려는 회원이 존재하지 않음");
                }
        );

        return member.getId();
    }

    private void revokeGoogleToken(){
        String revoking = "https://oauth2.googleapis.com/revoke?token=" + httpSession.getAttribute("tokenValue");

        try {
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost(revoking);
            CloseableHttpResponse response = client.execute(post);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //READ

    public Optional<Member> findOne(Long memberId){
        return memberRepository.findById(memberId);
    }

    public Member findByUserId(String userId) {
        Optional<Member> findMember = memberRepository.findByUserId(userId);
        if (!findMember.isPresent()) {
            try {
                throw new Exception("찾으려는 member가 없습니다.");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return findMember.get();
    }

    public Long findIdByToken(Object isToken) {
        if (!(isToken instanceof OAuth2AuthenticationToken)){
            try {
                throw new Exception("토큰이 OAuth2토큰의 요소가 아닙니다.");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) isToken;
        return findByUserId(token.getPrincipal().getAttribute("email")).getId();
    }

    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    public List<Study> findMyStudies(Member member) {
        return memberToStudyService.findMembersStudies(member);
    }
}
