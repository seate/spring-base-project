package com.example.ExSite.Common.config;

import com.example.ExSite.Member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // @preAuthorize 설정을 위해
public class SecurityConfig {

    private final MemberService memberService;

    @Autowired
    public SecurityConfig(MemberService memberService) {
        this.memberService = memberService;
    }

    private static final String[] AUTH_ADMINLIST = {
            "/members" //TODO admin 접근 가능 페이지
    };

    private static final String[] AUTH_MEMBERLIST = {
            "/members/memberPage" //TODO member 접근 가능 페이지
    };

    private static final String[] AUTH_ANONYMOUSEMBERLIST = {
            "/members/new" //TODO anonymous 접근 가능 페이지
    };


    @Bean
    public PasswordEncoder passwordEncoder(){ // bean이면 spring security에서 자동으로 적용해줌
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/css/**"); //TODO requestMatchers, antMatchers
    }

    // 권한 계층 설정
    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_MEMBER > ROLE_GUEST");
        return roleHierarchy;
    }


    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        //TODO http csrf disable 설정
        http.csrf().disable().headers().frameOptions().disable();

        //TODO http oauth2login 설정
        http.oauth2Login().userInfoEndpoint().userService(memberService);


        // URL 접근 권한 블랙리스트 방식 설정(일반: 허용 / 지정 경로: 차단)
        http.authorizeHttpRequests(authorize -> authorize
                .shouldFilterAllDispatcherTypes(false)

                /*//TODO 접근 권한 주석 해제 필요 / role hierarchy 형태로 변환
                .requestMatchers(AUTH_ADMINLIST).hasRole(Role.ADMIN.getName()) // ADMIN 권한을 가진 사람만 접근할 수 있도록
                .requestMatchers(AUTH_MEMBERLIST).authenticated() // 권한이 증명된 사람만 접근할 수 있도록
                .requestMatchers(AUTH_ANONYMOUSEMBERLIST).anonymous() // 익명 사용자만 접근할 수 있도록*/

                .anyRequest().permitAll() // 그 외의 페이지는 전부 허용
        );

        // 로그인 설정
        http.formLogin()
                //.loginPage("/members/memberLogin") // 로그인하는 페이지를 지정할 때 사용, 일단 스프링 제공 기본 로그인 페이지 사용
                .usernameParameter("userId") // username을 userId로 지정
                .passwordParameter("passwd") // password를 passwd로 지정
                .loginProcessingUrl("/loginProc") //TODO 로그인 처리 페이지를 지정할 때 사용, 고쳐야함
                .defaultSuccessUrl("/") // 로그인에 성공했을 때 기본적으로 이동하는 페이지를 지정할 때 사용
                .failureUrl("/login"); //TODO 로그인에 실패 했을 때 이동하는 페이지, 일단 기본 로그인 페이지로 적용
        //TODO successHandler랑 failureHandler 해야함

        // 로그아웃 설정
        http.logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login") // 로그아웃 시 이동할 페이지 지정
                .invalidateHttpSession(true); // 로그아웃 시 세션 없앨 것인가

        //세션 설정
        http.sessionManagement(session -> session
                .invalidSessionUrl("/invalid") // 유효하지 않은 세션의 경우 이동하는 페이지
                .maximumSessions(10) // 세션 최대 연결 수
                .maxSessionsPreventsLogin(true) // 세션 최대 연결 수에서 세션을 더 생성하려고 하면 막음(true)
                .expiredUrl("/") // 세션 만료 시 이동하는 페이지

        );

        // 에러 설정
        http.exceptionHandling().accessDeniedPage("/denied"); // 에러 페이지 설정



        return http.build();
    }
}
