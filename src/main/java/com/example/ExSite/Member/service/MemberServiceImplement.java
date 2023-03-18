package com.example.ExSite.Member.service;

import com.example.ExSite.Member.domain.GeneralMember;
import com.example.ExSite.Member.domain.Member;
import com.example.ExSite.Member.dto.MemberRequestDTO;
import com.example.ExSite.Member.dto.MemberResponseDTO;
import com.example.ExSite.Member.dto.OAuthAttributes;
import com.example.ExSite.Member.repository.MemberRepository;
import com.example.ExSite.Study.service.StudyService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
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


@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImplement implements MemberService {

    private final MemberRepository memberRepository;
    private final StudyService studyService;
    private final HttpSession httpSession;



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
    public MemberResponseDTO withdraw(MemberRequestDTO memberRequestDTO) {
        memberRepository.findByUserId(memberRequestDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("삭제하려는 회원이 존재하지 않음"));

        Member member = memberRequestDTO.toEntity();

        revokeGoogleToken(); //TODO kakao 토큰도 작성해야함
        studyService.memberDeleted(memberRequestDTO); //이 멤버가 지워졌을 때의 study 등의 처리
        memberRepository.delete(member);
        return new MemberResponseDTO(member);
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

    /*public Optional<Member> findOne(Long memberId){
        return memberRepository.findById(memberId);
    }*/

    public MemberResponseDTO findByUserId(String userId) {
        Member byUserId = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("찾으려는 member의 userId가 없습니다"));

        return new MemberResponseDTO(byUserId);
    }

    public List<MemberResponseDTO> findByKeyword(String keyword) {
        return memberRepository
                .findByKeyword(keyword)
                .stream().map(MemberResponseDTO::new)
                .toList();
    }

    public Long findIdByToken(Object isToken) {
        if (!(isToken instanceof OAuth2AuthenticationToken)) throw new RuntimeException("토큰이 OAuth2토큰의 요소가 아닙니다.");
        MemberResponseDTO memberResponseDTO = findByUserId(((OAuth2AuthenticationToken) isToken).getPrincipal().getAttribute("email"));
        return memberResponseDTO.getId();
    }

    public List<MemberResponseDTO> findAll(){
        return memberRepository.findAll().stream().map(MemberResponseDTO::new).toList();
    }
}
