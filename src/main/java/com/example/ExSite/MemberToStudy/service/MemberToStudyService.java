package com.example.ExSite.MemberToStudy.service;

import com.example.ExSite.Member.domain.Member;
import com.example.ExSite.MemberToStudy.repository.MemberToStudyRepository;
import com.example.ExSite.Study.domain.Study;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberToStudyService {

    private final MemberToStudyRepository memberToStudyRepository;

    //CREATE

    public void joinToStudy(Member member, Study study) throws Exception {
        if (memberToStudyRepository.findByMemberAndStudy(member, study).isPresent()) {
            throw new Exception("이미 생성된 MemberToStudy입니다.");
        }

        //study의 curUserCount update
        if (study.getCurUserCount() + 1 > study.getMaxUserCount()) {
            throw new Exception("member가 추가되려는 study의 최대 인원보다 많습니다.");
        }
        study.setCurUserCount(study.getCurUserCount() + 1);

        memberToStudyRepository.saveMemberToStudy(member, study);
    }

    //DELETE

    public void DisjoinFromStudy(Member member, Study study) throws Exception {
        if (!memberToStudyRepository.findByMemberAndStudy(member, study).isPresent()) {
            throw new Exception("지우려는 MemberToStudy가 없습니다.");
        }

        //study의 curUserCount update
        if (study.getCurUserCount() == 1) {
            throw new Exception("탈퇴하려는 study의 현재 인원이 1명입니다.");
        }
        study.setCurUserCount(study.getCurUserCount() - 1);

        memberToStudyRepository.deleteMemberToStudy(member, study);
    }

    //READ
    public List<Study> findMembersStudies(Member member) {
        return memberToStudyRepository.findStudiesByMember(member);
    }


    //UPDATE


}
