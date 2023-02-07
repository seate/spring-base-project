package com.example.ExSite.MemberToStudy.repository;

import com.example.ExSite.Member.domain.Member;
import com.example.ExSite.MemberToStudy.domain.MemberToStudy;
import com.example.ExSite.Study.domain.Study;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class JpaMemberToStudyRepository implements MemberToStudyRepository {

    private final EntityManager em;

    public JpaMemberToStudyRepository(EntityManager em) {
        this.em = em;
    }


    //CREATE

    public void saveMemberToStudy(Member member, Study study) {
        em.persist(new MemberToStudy(member, study));
    }


    //DELETE

    public Optional<MemberToStudy> deleteMemberToStudy(Member member, Study study) {
        Optional<MemberToStudy> findMemberToStudy = findById(new MemberToStudy(member, study).getId());
        findMemberToStudy.ifPresent(memberToStudy1 -> em.remove(memberToStudy1));

        return findMemberToStudy;
    }

    public void AllMemberDisjoinFromStudy(Study study) {
        em.createQuery("delete from MemberToStudy where Study = :study").setParameter("study", study);
    }


    //READ

    public Optional<MemberToStudy> findById(long id) {
        MemberToStudy memberToStudy = em.find(MemberToStudy.class, id);
        return Optional.ofNullable(memberToStudy);
    }

    public List<Member> findMembersByStudy(Study study) {
        return em.createQuery("select m from MemberToStudy m where m.study = :study", MemberToStudy.class)
                .setParameter("study", study).getResultStream()
                .map(MemberToStudy::getMember).collect(Collectors.toList());
    }

    public List<Study> findStudiesByMember(Member member) {
        return em.createQuery("select s from MemberToStudy s where s.member = :member", MemberToStudy.class)
                .setParameter("member", member).getResultStream()
                .map(MemberToStudy::getStudy).collect(Collectors.toList());
    }

    public Optional<MemberToStudy> findByMemberAndStudy(Member member, Study study) {
        return em.createQuery("select ms from MemberToStudy ms where ms.member = : member and ms.study = : study", MemberToStudy.class)
                .setParameter("member", member)
                .setParameter("study", study)
                .getResultStream().findAny();
    }
}
