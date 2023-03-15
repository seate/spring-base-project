package com.example.ExSite.Study.service;

import com.example.ExSite.Member.dto.MemberRequestDTO;
import com.example.ExSite.MemberToStudy.domain.MemberToStudy;
import com.example.ExSite.MemberToStudy.service.MemberToStudyService;
import com.example.ExSite.Study.domain.Study;
import com.example.ExSite.Study.dto.StudyRequestDTO;
import com.example.ExSite.Study.dto.StudyResponseDTO;
import com.example.ExSite.Study.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class StudyServiceImplement implements StudyService {

    private final StudyRepository studyRepository;
    private final MemberToStudyService memberToStudyService;
    private final ModelMapper modelMapper;

    //CREATE

    @Override
    public StudyResponseDTO create(MemberRequestDTO memberRequestDTO, StudyRequestDTO studyRequestDTO) {
        Study study = studyRequestDTO.toEntity();
        studyRepository.saveStudy(study);
        memberToStudyService.approveJoinStudy(memberRequestDTO, studyRequestDTO);
        return new StudyResponseDTO(study);
    }

    //DELETE

    @Override
    public StudyResponseDTO delete(MemberRequestDTO isLeader, StudyRequestDTO studyRequestDTO) {
        //delete leader만 가능하게
        if (!studyRequestDTO.getLeader().getId().equals(isLeader.getId()))
            throw new RuntimeException("이 member가 지우려는 study의 leader가 아닙니다.");

        return new StudyResponseDTO(studyRepository.deleteStudy(studyRequestDTO.toEntity()));
    }

    //member가 지워졌을 때와 study가 지워졌을 때는 membertostudy를 지우는 건 on delete cascade로 대체함
    public void memberDeleted(MemberRequestDTO memberRequestDTO) {
        List<StudyResponseDTO> studyResponseDTOS = memberToStudyService.findStudiesByMember(memberRequestDTO).stream()
                .map(memberToStudy -> new StudyResponseDTO(findById(memberToStudy.getStudy().getId()))).toList();

        for (StudyResponseDTO studyResponseDTO : studyResponseDTOS) {
            //이 멤버만 있는 스터디가 있으면
            //study 삭제는 study의 leader가 삭제되면 on delete cascade로 알아서 삭제됨
            //memberToStudy 삭제는 스터디 삭제하면 on delete cascade로 알아서 삭제됨
            if (studyResponseDTO.getCurUserCount() == 1) continue;

            StudyRequestDTO studyRequestDTO = modelMapper.map(studyResponseDTO, StudyRequestDTO.class);

            if (studyResponseDTO.getLeader().getId().equals(memberRequestDTO.getId())) { //이 멤버가 리더인 스터디 권한 위임

                MemberToStudy nextLeaderToStudy = memberToStudyService
                        .findMembersByStudy(studyRequestDTO).stream()
                        .filter(memberToStudy -> !memberRequestDTO.getId().equals(memberToStudy.getMember().getId()))
                        .findAny()
                        .orElseThrow(() -> new RuntimeException("leader 권한을 양도하려는 다음 leader가 없습니다."));

                studyRequestDTO.setLeader(nextLeaderToStudy.getMember());
                update(studyRequestDTO);
            }

            //이 멤버가 들어가 있는 스터디에서 탈퇴 처리
            memberToStudyService.withdrawMember(memberRequestDTO, studyRequestDTO);
        }
    }


    //READ

    public List<StudyResponseDTO> search(String keyword) {
        return studyRepository.findByName(keyword).stream().map(StudyResponseDTO::new).toList();
    }

    public Study findById(long id) {
        Optional<Study> studyById = studyRepository.findById(id);
        if (studyById.isEmpty()) throw new RuntimeException("이 id의 study가 없습니다.");

        return studyById.get();
    }

    public List<StudyResponseDTO> findAll() {
        return studyRepository.findAll().stream().map(StudyResponseDTO::new).toList();
    }

    public boolean isContained(Long studyId, Long memberId) {
        return findById(studyId).getMemberToStudyList()
                .stream().map(MemberToStudy::getMember)
                .anyMatch(member1 -> member1.getId().equals(memberId));
    }



    //UPDATE

    public void update(StudyRequestDTO studyRequestDTO) {
        studyRepository.updateStudy(studyRequestDTO.toEntity());
    }


    public void requestRegister(MemberRequestDTO memberRequestDTO, StudyRequestDTO studyRequestDTO) {
        StudyRequestDTO requestDTO = memberToStudyService.requestJoinStudy(memberRequestDTO, studyRequestDTO);
        update(requestDTO);
    }

    public void approveRegister(MemberRequestDTO memberRequestDTO, StudyRequestDTO studyRequestDTO) {
        StudyRequestDTO requestDTO = memberToStudyService.approveJoinStudy(memberRequestDTO, studyRequestDTO);
        update(requestDTO);
    }

    public void rejectRequest(MemberRequestDTO memberRequestDTO, StudyRequestDTO studyRequestDTO) {
        StudyRequestDTO requestDTO = memberToStudyService.rejectRequest(memberRequestDTO, studyRequestDTO);
        update(requestDTO);
    }

    public void withdrawMember(MemberRequestDTO memberRequestDTO, StudyRequestDTO studyRequestDTO) {
        StudyRequestDTO requestDTO = memberToStudyService.withdrawMember(memberRequestDTO, studyRequestDTO);
        update(requestDTO);
    }
}
