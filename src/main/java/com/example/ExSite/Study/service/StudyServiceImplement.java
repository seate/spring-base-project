package com.example.ExSite.Study.service;

import com.example.ExSite.Member.domain.Member;
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
            memberToStudyService.joinToStudy(leader, study);
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


    //UPDATE
}
