package com.example.ExSite.MemberToStudy.repository;

import com.example.ExSite.Member.dto.MemberRequestDTO;
import com.example.ExSite.MemberToStudy.domain.MemberToStudy;
import com.example.ExSite.Study.dto.StudyRequestDTO;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;


public class JpaMemberToStudyRepository implements MemberToStudyRepository {

    private final EntityManager em;

    public JpaMemberToStudyRepository(EntityManager em) {
        this.em = em;
    }

    //CREATE

    public void saveMemberToStudy(MemberRequestDTO memberRequestDTO, StudyRequestDTO studyRequestDTO, boolean approved) {
        findByMemberAndStudy(memberRequestDTO, studyRequestDTO, approved);
        em.persist(new MemberToStudy(memberRequestDTO.toEntity(), studyRequestDTO.toEntity(), approved));
    }


    //DELETE

    public void deleteMemberToStudy(MemberToStudy memberToStudy) {
        em.remove(memberToStudy);
    }

    public void AllMemberDisjoinFromStudy(StudyRequestDTO studyRequestDTO) {
        em.createQuery("delete from MemberToStudy where Study = :study")
                .setParameter("study", studyRequestDTO.toEntity());
    }


    //READ

    public Optional<MemberToStudy> findById(long id) {
        MemberToStudy memberToStudy = em.find(MemberToStudy.class, id);
        return Optional.ofNullable(memberToStudy);
    }

    public List<MemberToStudy> findByStudy(StudyRequestDTO studyRequestDTO, boolean approved) {
        return em.createQuery("select m from MemberToStudy m where m.study = :study and m.approved = :approved", MemberToStudy.class)
                .setParameter("study", studyRequestDTO.toEntity())
                .setParameter("approved", approved)
                .getResultList();
    }



    @Override
    public List<MemberToStudy> findByMember(MemberRequestDTO memberRequestDTO, boolean approved) {
        return em.createQuery("select s from MemberToStudy s where s.member = :member and s.approved = :approved", MemberToStudy.class)
                .setParameter("member", memberRequestDTO.toEntity())
                .setParameter("approved", approved)
                .getResultList();
    }

    public Optional<MemberToStudy> findByMemberAndStudy(MemberRequestDTO memberRequestDTO, StudyRequestDTO studyRequestDTO, boolean approved) {
        return em.createQuery("select ms from MemberToStudy ms where ms.member = :member and ms.study = :study and ms.approved = :approved", MemberToStudy.class)
                .setParameter("member", memberRequestDTO.toEntity())
                .setParameter("study", studyRequestDTO.toEntity())
                .setParameter("approved", approved)
                .getResultStream().findAny();
    }
}
