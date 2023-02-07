package com.example.ExSite.Study.service;

import com.example.ExSite.Member.domain.Member;
import com.example.ExSite.Study.domain.Study;

import java.util.List;

public interface StudyService {
    void create(String name, int maxUserCount, String goal, String details, Member leader);
    void delete(Member member, Study study);
    Study findById(long id);
    List<Study> search(String keyword);
    List<Study> list();




}
