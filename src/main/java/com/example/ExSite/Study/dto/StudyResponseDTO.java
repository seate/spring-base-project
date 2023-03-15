package com.example.ExSite.Study.dto;

import com.example.ExSite.Member.domain.Member;
import com.example.ExSite.Study.domain.Study;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudyResponseDTO {

    private Long id;
    @NotBlank
    @Size(min = 3, max = 30)
    private String name;
    @NotBlank
    @Min(value = 2)
    private int curUserCount;
    @Max(20)
    private int maxUserCount;
    @Size(min = 10, max = 50)
    private String goal;
    @Size(min = 10, max = 200)
    private String details;
    @NotBlank
    private Member leader;

    @Builder
    public StudyResponseDTO(Study study) {
        this.id = study.getId();
        this.name = study.getName();
        this.curUserCount = study.getCurUserCount();
        this.maxUserCount = study.getMaxUserCount();
        this.goal = study.getGoal();
        this.details = study.getDetails();
        this.leader = study.getLeader();
    }
}
