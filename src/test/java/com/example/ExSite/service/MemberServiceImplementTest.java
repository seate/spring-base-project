package com.example.ExSite.service;

class MemberServiceImplementTest {

    /*MemberServiceImplement memberServiceImplement;
    JpaMemberRepository memberRepository;*/

    /*
    @BeforeEach
    public void beforeEach() {
        memberRepository = new JpaMemberRepository();
        memberService = new MemberService(memberRepository);
    }

    @AfterEach
    public void afterEach() {
        memberRepository.clearStore();
    }
    */

    /*@Test
    void join() {
        //given
        Member member = new Member();
        member.setName("hello");

        //when
        Long saveId = memberServiceImplement.join(member);

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
    void findMembers() {
    }

    @Test
    void findOne() {
    }*/
}