package com.example.ExSite.Study.repository;

import com.example.ExSite.Study.domain.Study;

import java.util.List;
import java.util.Optional;

public interface StudyRepository {
    //create
    void saveStudy(Study study);
    //delete
    Study deleteStudy(Study study);

    //read
    Optional<Study> findById(long id);
    List<Study> findByName(String name);

    List<Study> findAll();

    //update
    void updateStudy(Study study);


}
