package com.example.ExSite.Member.repository;

import com.example.ExSite.Member.domain.Member;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;


public class JpaMemberRepository implements MemberRepository {

    private final EntityManager em;
    public JpaMemberRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    @Override
    //TODO study에서 삭제된 멤버를 전부 탈퇴시켜야함
    public Optional<Member> delete(Member member) {
        Optional<Member> findMember = findById(member.getId());

        findMember.ifPresent(member1 -> {

            em.remove(member1);
        });

        return findMember;
    }

    @Override
    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    @Override
    public Optional<Member> findByUserId(String userId) {
        List<Member> result = em.createQuery("select m from Member m where m.userId = :userId", Member.class)
                .setParameter("userId", userId)
                .getResultList();
        return result.stream().findAny();
    }

    @Override
    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }

    @Override
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }


    //TODO update는 service단에서 saveOrUpdate로 해결하는 듯 일관성을 위해 고쳐야 하나
}
