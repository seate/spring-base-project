package com.example.ExSite.Study.domain;

import com.example.ExSite.Common.domain.BaseTimeEntity;
import com.example.ExSite.Member.domain.Member;
import com.example.ExSite.MemberToStudy.domain.MemberToStudy;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Study extends BaseTimeEntity {

    @Id
    @Column(name = "studyId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int curUserCount;

    @Column(nullable = false)
    private int maxUserCount;

    @Column
    private String goal;

    @Column
    private String details;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member leader;

    //MeberToStudy는 Member와 Study에 종속적이기 때문에
    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberToStudy> memberToStudyList = new ArrayList<>();




    @Builder
    public Study(String name, int maxUserCount, String goal, String details, Member leader) {
        this.name = name;
        this.curUserCount = 0;
        this.maxUserCount = maxUserCount;
        this.goal = goal;
        this.details = details;
        this.leader = leader;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Study study = (Study) o;
        return id == study.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
