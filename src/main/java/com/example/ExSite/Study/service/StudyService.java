package com.example.ExSite.Study.service;

import com.example.ExSite.Member.dto.MemberRequestDTO;
import com.example.ExSite.Study.dto.StudyRequestDTO;
import com.example.ExSite.Study.dto.StudyResponseDTO;

import java.util.List;

public interface StudyService {
    StudyResponseDTO create(MemberRequestDTO memberRequestDTO, StudyRequestDTO studyRequestDTO);
    StudyResponseDTO delete(MemberRequestDTO isLeader, StudyRequestDTO studyRequestDTO);
    void memberDeleted(MemberRequestDTO memberRequestDTO);
    //Study findById(long id);
    List<StudyResponseDTO> search(String keyword);
    boolean isContained(Long studyId, Long memberId);
    List<StudyResponseDTO> findAll();

    void update(StudyRequestDTO studyRequestDTO);
}
