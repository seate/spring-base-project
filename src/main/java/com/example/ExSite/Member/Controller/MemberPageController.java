package com.example.ExSite.Member.Controller;

import com.example.ExSite.Member.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.SessionAttributes;

@SessionAttributes("member")
@Controller
public class MemberPageController {

    private final MemberService memberService;
    public MemberPageController(MemberService memberService) {
        this.memberService = memberService;
    }


    //CREATE <- OAuth2로 service단의 loadUser에서 자동으로 함
    /*@GetMapping("/members/new")
    public String createMemberForm(){
        return "members/createMemberForm";
    }

    @PostMapping(value = "/members/new")
    public String createMember(MemberForm form) {


        Member member = new Member();
        member.setName(form.getName());
        member.setUserId(form.getUserId());
        member.setPasswd(form.getPasswd());
        memberService.saveOrUpdate(member);

        return "redirect:/";
    }*/

    //READ

    /*@GetMapping(value = "/member/list")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    public String list(Model model) {
        List<MemberResponseDTO> members = memberService.findAll();
        model.addAttribute("members", members);
        return "members/memberList";
    }


    //UPDATE 이건 oauth 로그인이라 일단 보류

    //DELETE
    @GetMapping("/member/delete")
    public String delete(Model model, MemberRequestDTO memberRequestDTO){
        memberService.withdraw(memberRequestDTO);
        return "redirect:/";
    }*/




    /*
    @GetMapping("/members/memberLogin")
    public String loginForm(){ return "members/memberLogin"; }


    @PostMapping(value = "/members/memberLogin")
    public String login(MemberForm form, Model model) {
        Optional<Member> loginMember = memberServiceImplement.login(form.getUserId(), form.getPasswd());

        if(loginMember.isPresent()){ //로그인 성공
            model.addAttribute("member", loginMember.get());
            return "redirect:/";
        }
        else return "members/memberLogin"; //로그인 실패
    }
    */

    /*
    @GetMapping("/members/memberPasswordChange")
    public String passwordChangeForm(){ return "members/memberPasswordChange"; }

    @PostMapping(value = "/members/memberPasswordChange")
    public String passwordChange(Model model) {
        Member member = (Member) model.getAttribute("member");
        memberService.changePassword(member);
        return "redirect:/";
    }

    @GetMapping("members/memberLogout")
    public String logout(SessionStatus status){
        status.setComplete();
        return "redirect:/";
    }
    */


}
