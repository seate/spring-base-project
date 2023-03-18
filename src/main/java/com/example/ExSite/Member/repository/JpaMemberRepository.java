package com.example.ExSite.Member.repository;

import com.example.ExSite.Member.domain.Member;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class JpaMemberRepository implements MemberRepository {

    private final EntityManager em;


    //CREATE

    @Override
    public Member save(Member member) {
        em.persist(member);
        return member;
    }



    @Override
    public Member delete(Member member) {
        Member byId = findById(member.getId())
                .orElseThrow(() -> new RuntimeException("찾는 member가 없습니다."));

        em.remove(byId);
        return byId;
    }

    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(em.find(Member.class, id));
    }

    @Override
    public Optional<Member> findByUserId(String userId) {
        return em.createQuery("select m from Member m where m.userId = :userId", Member.class)
                .setParameter("userId", userId).getResultStream().findAny();
    }

    @Override
    public List<Member> findByKeyword(String keyword) {
        return em.createQuery("select m from Member m where m.name = :keyword", Member.class)
                .setParameter("keyword", keyword).getResultList();
    }

    @Override
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }


    //update는 service단에서 saveOrUpdate로 해결
}
