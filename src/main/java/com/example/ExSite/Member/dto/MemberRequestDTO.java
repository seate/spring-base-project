package com.example.ExSite.Member.dto;

import com.example.ExSite.Member.domain.Member;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class MemberRequestDTO {

    @NotBlank
    private Long id;
    @NotBlank
    private String name;

    private String userId;

    private String passwd;

    public Member toEntity() {
        return Member.builder()
                .name(name)
                .userId(userId)
                .passwd(passwd)
                .build();
    }
}
