package com.example.ExSite.MemberToStudy.repository;

import com.example.ExSite.Member.domain.Member;
import com.example.ExSite.MemberToStudy.domain.MemberToStudy;
import com.example.ExSite.Study.domain.Study;

import java.util.List;
import java.util.Optional;

public interface MemberToStudyRepository {
    void saveMemberToStudy(Member member, Study study);

    Optional<MemberToStudy> deleteMemberToStudy(Member member, Study study);

    void AllMemberDisjoinFromStudy(Study study);

    Optional<MemberToStudy> findById(long id);
    List<Member> findMembersByStudy(Study study);
    List<Study> findStudiesByMember(Member member);

    Optional<MemberToStudy> findByMemberAndStudy(Member member, Study study);
}
