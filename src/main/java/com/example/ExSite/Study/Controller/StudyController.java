package com.example.ExSite.Study.Controller;

import com.example.ExSite.Member.domain.GeneralMember;
import com.example.ExSite.Member.dto.MemberRequestDTO;
import com.example.ExSite.Study.dto.StudyRequestDTO;
import com.example.ExSite.Study.dto.StudyResponseDTO;
import com.example.ExSite.Study.service.StudyService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/study")
@RequiredArgsConstructor
public class StudyController {
    private final StudyService studyService;
    private final ModelMapper modelMapper;


    @RequestMapping(method = RequestMethod.POST, value = "/create")
    public ResponseEntity<StudyResponseDTO> create(@AuthenticationPrincipal GeneralMember generalMember, StudyRequestDTO studyRequestDTO) {
        MemberRequestDTO memberRequestDTO = modelMapper.map(generalMember.getMember(), MemberRequestDTO.class);
        StudyResponseDTO studyResponseDTO = studyService.create(memberRequestDTO, studyRequestDTO);
        return new ResponseEntity<>(studyResponseDTO, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/delete")
    public ResponseEntity<StudyResponseDTO> delete(@AuthenticationPrincipal GeneralMember generalMember, StudyRequestDTO studyRequestDTO) {
        MemberRequestDTO memberRequestDTO = modelMapper.map(generalMember.getMember(), MemberRequestDTO.class);
        StudyResponseDTO studyResponseDTO = studyService.delete(memberRequestDTO, studyRequestDTO);
        return new ResponseEntity<>(studyResponseDTO, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value ="/search")
    public ResponseEntity<List<StudyResponseDTO>> search(@RequestParam String keyword) {
        return new ResponseEntity<>(studyService.search(keyword), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/list")
    public ResponseEntity<List<StudyResponseDTO>> findAll() {
        return new ResponseEntity<>(studyService.findAll(), HttpStatus.OK);
    }
}
