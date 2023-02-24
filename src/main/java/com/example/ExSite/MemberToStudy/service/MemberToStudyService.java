package com.example.ExSite.MemberToStudy.service;

import com.example.ExSite.Member.domain.Member;
import com.example.ExSite.MemberToStudy.domain.MemberToStudy;
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

    public void joinStudy(Member member, Study study, boolean approved) throws Exception {
        //가입 요청: approved = false
        //가입을 수락: approved = true

        if (memberToStudyRepository.findByMemberAndStudy(member, study, approved).isPresent()) {
            throw new Exception("이미 생성된 MemberToStudy입니다.");
        }

        if (approved){ //승인하는 join이면
            //study의 curUserCount update
            if (study.getCurUserCount() + 1 > study.getMaxUserCount()) {
                throw new Exception("member가 추가되려는 study의 최대 인원보다 많습니다.");
            }
            study.setCurUserCount(study.getCurUserCount() + 1);
        }

        memberToStudyRepository.saveMemberToStudy(member, study, approved);
    }

    //DELETE

    public void disjoinStudy(Member member, Study study, boolean approved) throws Exception {
        //가입 요청을 거절: approved = false
        //가입된 member의 탈퇴: approved = true

        if (!memberToStudyRepository.findByMemberAndStudy(member, study, approved).isPresent()) {
            throw new Exception("지우려는 MemberToStudy가 없습니다.");
        }

        if (approved){
            //study의 curUserCount update
            if (study.getCurUserCount() == 1) {
                throw new Exception("탈퇴하려는 study의 현재 인원이 1명입니다.");
            }
            study.setCurUserCount(study.getCurUserCount() - 1);
        }

        memberToStudyRepository.deleteMemberToStudy(member, study, approved);
    }

    //READ
    public List<Study> findStudiesByMember(Member member) {
        return memberToStudyRepository.findStudiesByMember(member, true);
    }

    public List<Member> findMembersByStudy(Study study) {
        return memberToStudyRepository.findMembersByStudy(study, true);
    }

    public List<MemberToStudy> findStudiesRequest(Study study) {
        return memberToStudyRepository.findRequestsByStudy(study);
    }


    //UPDATE


}
