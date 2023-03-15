package com.example.ExSite.MemberToStudy.repository;

import com.example.ExSite.Member.dto.MemberRequestDTO;
import com.example.ExSite.MemberToStudy.domain.MemberToStudy;
import com.example.ExSite.Study.dto.StudyRequestDTO;

import java.util.List;
import java.util.Optional;

public interface MemberToStudyRepository {
    void saveMemberToStudy(MemberRequestDTO memberRequestDTO, StudyRequestDTO studyRequestDTO, boolean approved);

    void deleteMemberToStudy(MemberToStudy memberToStudy);

    void AllMemberDisjoinFromStudy(StudyRequestDTO studyRequestDTO);

    Optional<MemberToStudy> findById(long id);
    List<MemberToStudy> findByStudy(StudyRequestDTO studyRequestDTO, boolean approved);
    List<MemberToStudy> findByMember(MemberRequestDTO memberRequestDTO, boolean approved);

    Optional<MemberToStudy> findByMemberAndStudy(MemberRequestDTO memberRequestDTO, StudyRequestDTO studyRequestDTO, boolean approved);
}
