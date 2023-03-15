package com.example.ExSite.Member.dto;

import com.example.ExSite.Member.domain.Member;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberResponseDTO {

    @NotBlank
    private Long id;

    @NotBlank
    private String name;

    private String userId;

    private String passwd;

    private String picture;

    @Builder
    public MemberResponseDTO(Member member) {
        id = member.getId();
        name = member.getName();
        userId = member.getUserId();
        passwd = member.getPasswd();
        picture = member.getPicture();
    }
}
