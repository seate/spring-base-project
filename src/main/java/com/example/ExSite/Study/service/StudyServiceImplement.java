package com.example.ExSite.Study.service;

import com.example.ExSite.Member.domain.Member;
import com.example.ExSite.MemberToStudy.domain.MemberToStudy;
import com.example.ExSite.MemberToStudy.service.MemberToStudyService;
import com.example.ExSite.Study.domain.Study;
import com.example.ExSite.Study.repository.StudyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudyServiceImplement implements StudyService {

    private final StudyRepository studyRepository;
    private final MemberToStudyService memberToStudyService;

    public StudyServiceImplement(StudyRepository studyRepository, MemberToStudyService memberToStudyService) {
        this.studyRepository = studyRepository;
        this.memberToStudyService = memberToStudyService;
    }

    //CREATE

    @Override
    public void create(String name, int maxUserCount, String goal, String details, Member leader) {
        Study study = Study.builder()
                .name(name)
                .maxUserCount(maxUserCount)
                .goal(goal)
                .details(details)
                .leader(leader)
                .build();


        studyRepository.saveStudy(study);

        try {
            memberToStudyService.joinStudy(leader, study, true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //DELETE

    @Override
    public void delete(Member member, Study study) {
        //delete leader만 가능하게
        if (!study.getLeader().equals(member)){
            try {
                throw new Exception("이 member가 지우려는 study의 leader가 아닙니다.");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        studyRepository.deleteStudy(study);
    }

    //member가 지워졌을 때와 study가 지워졌을 때는 membertostudy를 지우는 건 on delete cascade로 대체함
    public void memberDeleted(Member member) {

        for (Study study : memberToStudyService.findStudiesByMember(member)) {
            if (study.getCurUserCount() == 1) { //이 멤버만 있는 스터디가 있으면
                //study 삭제는 study의 leader가 삭제되면 on delete cascade로 알아서 삭제됨
                //memberToStudy 삭제는 스터디 삭제하면 on delete cascade로 알아서 삭제됨
            }
            else {
                if (study.getLeader().equals(member)) { //이 멤버가 리더인 스터디 권한 위임
                    Optional<MemberToStudy> nextLeadersMemberToStudy =
                            //스터디의 멤버들 중에
                            study.getMemberToStudyList().stream()
                                    // 삭제하려는 member가 아닌 아무나
                                    .filter(memberToStudy -> !memberToStudy.getMember().equals(member)).findAny();


                    if (!nextLeadersMemberToStudy.isPresent()) {
                        try {
                            throw new Exception("leader 권한을 양도하려는 다음 leader가 없습니다.");
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }

                    study.setLeader(nextLeadersMemberToStudy.get().getMember());
                    update(study);
                }

                //이 멤버가 들어가 있는 스터디에서 탈퇴 처리
                try {
                    memberToStudyService.disjoinStudy(member, study, true);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    //READ

    public List<Study> search(String keyword) {
        return studyRepository.findByName(keyword);
    }

    public Study findById(long id) {
        Optional<Study> studyById = studyRepository.findById(id);

        if (!studyById.isPresent()) {
            throw new RuntimeException("이 id의 study가 없습니다.");
        }

        return studyById.get();
    }

    public List<Study> list() {
        return studyRepository.findAll();
    }

    public boolean isContained(Long studyId, Member member) {
        return findById(studyId).getMemberToStudyList()
                .stream().map(MemberToStudy::getMember)
                .anyMatch(member1 -> member1.equals(member));
    }



    //UPDATE

    public void update(Study study) {
        studyRepository.updateStudy(study);
    }
}
