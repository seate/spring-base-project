package com.example.ExSite.Study.repository;

import com.example.ExSite.Study.domain.Study;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;


public class JpaStudyRepository implements StudyRepository {

    private final EntityManager em;

    public JpaStudyRepository(EntityManager em) {
        this.em = em;
    }


    //CREATE

    @Override
    public void saveStudy(Study study) {
        em.persist(study);
    }

    //DELETE

    @Override
    public Optional<Study> deleteStudy(Study study) {
        Optional<Study> findStudy = findById(study.getId());
        findStudy.ifPresent(study1 -> em.remove(study1));

        return findStudy;
    }

    //READ

    @Override
    public Optional<Study> findById(long id) {
        Study study = em.find(Study.class, id);
        return Optional.ofNullable(study);
    }

    public List<Study> findByName(String keyword) {
        return em.createQuery("select s from Study s where s.name like concat('%', :keyword, '%') ", Study.class)
                .setParameter("keyword", keyword).getResultList();
    }


    @Override
    public List<Study> findAll() {
        return em.createQuery("select s from Study s", Study.class).getResultList();
    }


    //UPDATE
    public void updateStudy(Study study) {
        em.createQuery("update Study set name = :name, curUserCount = :curUserCount," +
                "maxUserCount = maxUserCount, goal = :goal, details = :details where id = :id")
                .setParameter("name", study.getName())
                .setParameter("curUserCount", study.getCurUserCount())
                .setParameter("maxUserCount", study.getMaxUserCount())
                .setParameter("goal", study.getGoal())
                .setParameter("details", study.getDetails())
                .setParameter("id", study.getId());
    }
}
