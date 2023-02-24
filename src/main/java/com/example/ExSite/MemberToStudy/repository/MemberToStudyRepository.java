package com.example.ExSite.MemberToStudy.repository;

import com.example.ExSite.Member.domain.Member;
import com.example.ExSite.MemberToStudy.domain.MemberToStudy;
import com.example.ExSite.Study.domain.Study;

import java.util.List;
import java.util.Optional;

public interface MemberToStudyRepository {
    void saveMemberToStudy(Member member, Study study, boolean approved);

    Optional<MemberToStudy> deleteMemberToStudy(Member member, Study study, boolean approved);

    void AllMemberDisjoinFromStudy(Study study);

    Optional<MemberToStudy> findById(long id);
    List<Member> findMembersByStudy(Study study, boolean approved);
    List<Study> findStudiesByMember(Member member, boolean approved);

    Optional<MemberToStudy> findByMemberAndStudy(Member member, Study study, boolean approved);
    List<MemberToStudy> findRequestsByStudy(Study study);
}
