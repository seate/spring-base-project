package com.example.ExSite.Member.domain;

import com.example.ExSite.Common.domain.BaseTimeEntity;
import com.example.ExSite.MemberToStudy.domain.MemberToStudy;
import com.example.ExSite.Study.domain.Study;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Getter
@Setter
@NoArgsConstructor
@Entity // @Entity는 데이터베이스의 테이블과 1:1로 매핑되는 객체
public class Member extends BaseTimeEntity {

    @Id
    @Column(name = "memberId", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userId;

    @Column
    private String passwd;

    @Column(nullable = false)
    private String name;

    @Column
    private String picture;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    //Member를 삭제하기 전에 leader를 변경하고 지우기 때문에 지워지는 study는 이 멤버만 존재하는 study
    @OneToMany(mappedBy = "leader", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Study> myLeaderStudyList = new ArrayList<>();

    //MeberToStudy는 Member와 Study에 종속적이기 때문에
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberToStudy> studyList = new ArrayList<>();


    //TODO Builder 공부 필요
    @Builder
    public Member(String userId, String passwd, String name, String picture, Role role){
        this.userId = userId;
        this.passwd = passwd;
        this.name = name;
        this.picture = picture;
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return id.equals(member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }



    public Member update(String name, String picture){
        this.name = name;
        this.picture = picture;

        return this; // 여기 블로그랑 좀 다름
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}
