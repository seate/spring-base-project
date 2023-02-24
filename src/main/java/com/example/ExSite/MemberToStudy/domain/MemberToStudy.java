package com.example.ExSite.MemberToStudy.domain;


import com.example.ExSite.Common.domain.BaseTimeEntity;
import com.example.ExSite.Member.domain.Member;
import com.example.ExSite.Study.domain.Study;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;


@Getter
@Entity
@NoArgsConstructor
public class MemberToStudy extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "studyId")
    private Study study;

    @Column(nullable = false)
    private boolean approved;


    public MemberToStudy(Member member, Study study, boolean approved) {
        this.member = member;
        this.study = study;
        this.approved = approved;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberToStudy that = (MemberToStudy) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
