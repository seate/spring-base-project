package com.example.ExSite.Member.repository;

import com.example.ExSite.Member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);
    Optional<Member> delete(Member member);
    Optional<Member> findById(Long id);
    List<Member> findByName(String name);
    Optional<Member> findByUserId(String userId);
    //public Optional<Member> findByUserIdAndPassword(String userId, String passwd);
    List<Member> findAll();

    //void changePassword(Member member);
    //Member delete(Member member);


}
