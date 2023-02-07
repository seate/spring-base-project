package com.example.ExSite.MemberToStudy.service;

import com.example.ExSite.Member.domain.Member;
import com.example.ExSite.MemberToStudy.domain.MemberToStudy;
import com.example.ExSite.MemberToStudy.repository.MemberToStudyRepository;
import com.example.ExSite.Study.domain.Study;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MemberToStudyService {

    private final MemberToStudyRepository memberToStudyRepository;

    @Autowired
    public MemberToStudyService(MemberToStudyRepository memberToStudyRepository) {
        this.memberToStudyRepository = memberToStudyRepository;
    }

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

    //member가 지워졌을 때와 study가 지워졌을 때는 membertostudy를 지우는 건 on delete cascade로 대체함
    public void memberDeleted(Member member) throws Exception {

        for (Study study : findMembersStudies(member)) {
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
                            .filter(memberToStudy -> memberToStudy.getMember().equals(member)).findAny();


                    if (!nextLeadersMemberToStudy.isPresent()) {
                        throw new Exception("leader 권한을 양도하려는 다음 leader가 없습니다.");
                    }

                    study.setLeader(nextLeadersMemberToStudy.get().getMember());
                }

                //이 멤버가 들어가 있는 스터디에서 탈퇴 처리
                DisjoinFromStudy(member, study);
            }
        }
    }
}
