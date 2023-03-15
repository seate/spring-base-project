package com.example.ExSite.MemberToStudy.service;

import com.example.ExSite.Member.dto.MemberRequestDTO;
import com.example.ExSite.MemberToStudy.domain.MemberToStudy;
import com.example.ExSite.MemberToStudy.repository.MemberToStudyRepository;
import com.example.ExSite.Study.dto.StudyRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberToStudyService {

    private final MemberToStudyRepository memberToStudyRepository;


    //CREATE

    public StudyRequestDTO requestJoinStudy(MemberRequestDTO memberRequestDTO, StudyRequestDTO studyRequestDTO) {
        return joinStudy(memberRequestDTO, studyRequestDTO, false);
    }

    public StudyRequestDTO approveJoinStudy(MemberRequestDTO memberRequestDTO, StudyRequestDTO studyRequestDTO) {
        return joinStudy(memberRequestDTO, studyRequestDTO, true);
    }

    private StudyRequestDTO joinStudy(MemberRequestDTO memberRequestDTO, StudyRequestDTO studyRequestDTO, boolean approved) {
        //가입 요청: approved = false
        //가입을 수락: approved = true

        if (memberToStudyRepository.findByMemberAndStudy(memberRequestDTO, studyRequestDTO, approved).isPresent())
            throw new RuntimeException("이미 생성된 MemberToStudy입니다.");

        if (approved) { //승인하는 join이면
            //study의 curUserCount update
            if (studyRequestDTO.getCurUserCount() + 1 > studyRequestDTO.getMaxUserCount())
                throw new RuntimeException("member가 추가되려는 study의 최대 인원보다 많습니다.");

            studyRequestDTO.setCurUserCount(studyRequestDTO.getCurUserCount() + 1); //TODO 문제 이빠이
        }

        memberToStudyRepository.saveMemberToStudy(memberRequestDTO, studyRequestDTO, approved);
        return studyRequestDTO;
    }



    //DELETE
    public StudyRequestDTO rejectRequest(MemberRequestDTO memberRequestDTO, StudyRequestDTO studyRequestDTO) {
        return disjoinStudy(memberRequestDTO, studyRequestDTO, false);
    }

    public StudyRequestDTO withdrawMember(MemberRequestDTO memberRequestDTO, StudyRequestDTO studyRequestDTO) {
        return disjoinStudy(memberRequestDTO, studyRequestDTO, true);
    }



    private StudyRequestDTO disjoinStudy(MemberRequestDTO memberRequestDTO, StudyRequestDTO studyRequestDTO, boolean approved) {
        //가입 요청을 거절: approved = false
        //가입된 member의 탈퇴: approved = true
        Optional<MemberToStudy> byMemberAndStudy = memberToStudyRepository.findByMemberAndStudy(memberRequestDTO, studyRequestDTO, approved);

        if (byMemberAndStudy.isEmpty()) throw new RuntimeException("지우려는 MemberToStudy가 없습니다.");

        if (approved) {
            //study의 curUserCount update
            if (studyRequestDTO.getCurUserCount() == 1) throw new RuntimeException("탈퇴하려는 study의 현재 인원이 1명입니다.");
            studyRequestDTO.setCurUserCount(studyRequestDTO.getCurUserCount() - 1); //TODO 여기 문제 이빠이
        }

        memberToStudyRepository.deleteMemberToStudy(byMemberAndStudy.get());
        return studyRequestDTO;
    }

    //READ
    public List<MemberToStudy> findStudiesByMember(MemberRequestDTO memberRequestDTO) {
        return memberToStudyRepository.findByMember(memberRequestDTO, true);
    }

    public List<MemberToStudy> findMembersByStudy(StudyRequestDTO studyRequestDTO) {
        return memberToStudyRepository.findByStudy(studyRequestDTO, true);
    }


    public Optional<MemberToStudy> findByMemberAndStudy(MemberRequestDTO memberRequestDTO, StudyRequestDTO studyRequestDTO, boolean approved) {
        return memberToStudyRepository.findByMemberAndStudy(memberRequestDTO, studyRequestDTO, approved);
    }


    //UPDATE


}
