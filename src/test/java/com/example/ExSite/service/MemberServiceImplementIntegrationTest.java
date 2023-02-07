package com.example.ExSite.service;

import com.example.ExSite.Member.repository.MemberRepository;
import com.example.ExSite.Member.service.MemberServiceImplement;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class MemberServiceImplementIntegrationTest {

    @Autowired
    MemberServiceImplement memberServiceImplement;
    @Autowired
    MemberRepository memberRepository;

    /*@Test
    void join() {
        //given
        Member member = new Member();
        member.setName("hello");

        //when
        Long saveId = memberServiceImplement.saveOrUpdate(member);

        //then
        Member findmember = memberServiceImplement.findOne(saveId).get();
        Assertions.assertThat(member.getName()).isEqualTo(findmember.getName());
    }

    @Test
    public void 중복_회원_예외() throws Exception {
        //Given
        Member member1 = new Member();
        member1.setName("spring");
        Member member2 = new Member();
        member2.setName("spring");
        //When
        memberServiceImplement.join(member1);
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> memberServiceImplement.join(member2));//예외가 발생해야 한다.
        Assertions.assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원");
    }

    @Test
    public void changePasswordTest(){
        //given
        Member member = new Member();
        member.setName("spring");
        member.setUserId("qwwwww");
        member.setPasswd("originalPassword");

        //when
        memberServiceImplement.join(member);
        //memberServiceImplement.changePassword(member);


    }*/

    @Test
    void findMembers() {
    }

    @Test
    void findOne() {
    }
}