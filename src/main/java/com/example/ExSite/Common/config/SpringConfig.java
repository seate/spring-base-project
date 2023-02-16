package com.example.ExSite.Common.config;

import com.example.ExSite.Chatting.Repository.ChatRepository;
import com.example.ExSite.Member.repository.JpaMemberRepository;
import com.example.ExSite.Member.repository.MemberRepository;
import com.example.ExSite.MemberToStudy.repository.JpaMemberToStudyRepository;
import com.example.ExSite.MemberToStudy.repository.MemberToStudyRepository;
import com.example.ExSite.Study.repository.JpaStudyRepository;
import com.example.ExSite.Study.repository.StudyRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    public SpringConfig(EntityManager em) {
        this.em = em;
    }

    //쓸데없는 에러나서 그냥 써둠
    @Bean
    public EntityManager entityManager(){
        return em;
    }

    @Bean
    public MemberRepository memberRepository() {
        return new JpaMemberRepository(em);
    }

    @Bean
    public MemberToStudyRepository memberToStudyRepository() {
        return new JpaMemberToStudyRepository(em);
    }

    @Bean
    public StudyRepository studyRepository() {
        return new JpaStudyRepository(em);
    }

    @Bean
    public ChatRepository chatRepository() {
        return new ChatRepository(em);
    }
}
