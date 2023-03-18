package com.example.ExSite.Member.Controller;

import com.example.ExSite.Member.domain.GeneralMember;
import com.example.ExSite.Member.dto.MemberRequestDTO;
import com.example.ExSite.Member.dto.MemberResponseDTO;
import com.example.ExSite.Member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/member")
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final ModelMapper modelMapper;


    //CREATE X

    //DELETE

    @RequestMapping(method = RequestMethod.POST, value = "/delete")
    public ResponseEntity<MemberResponseDTO> delete(@AuthenticationPrincipal GeneralMember generalMember) {
        MemberResponseDTO memberResponseDTO = memberService.withdraw(modelMapper.map(generalMember.getMember(), MemberRequestDTO.class));
        return new ResponseEntity<>(memberResponseDTO, HttpStatus.OK);
    }

    //READ

    @RequestMapping(method = RequestMethod.GET, value = "/findByUserId")
    public ResponseEntity<MemberResponseDTO> findByUserId(String userId) {
        MemberResponseDTO byUserId = memberService.findByUserId(userId);
        return new ResponseEntity<>(byUserId, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/findByKeyword")
    public ResponseEntity<List<MemberResponseDTO>> findByKeyword(String keyword) {
        return new ResponseEntity<>(memberService.findByKeyword(keyword), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/findAll")
    public ResponseEntity<List<MemberResponseDTO>> findAll() {
        List<MemberResponseDTO> members = memberService.findAll();
        return new ResponseEntity<>(members, HttpStatus.OK);
    }


    //UPDATE X

}
