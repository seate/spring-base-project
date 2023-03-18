package com.example.ExSite.Study.Controller;

import com.example.ExSite.Study.service.StudyService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class StudyPageController {

    private final StudyService studyService;
    private final ModelMapper modelMapper;

    @Autowired
    public StudyPageController(StudyService studyService, ModelMapper modelMapper) {
        this.studyService = studyService;
        this.modelMapper = modelMapper;
    }

    //CREATE

    /*@GetMapping("study/create")
    public String studyCreate() {
        return "study/studyCreate";
    }

    @PostMapping("/study/create")
    public String studyCreate(@AuthenticationPrincipal GeneralMember generalMember, StudyRequestDTO studyRequestDTO) {
        studyService.create(modelMapper.map(generalMember.getMember(), MemberRequestDTO.class), studyRequestDTO);
        return "redirect:/";
    }

    //DELETE

    *//*@GetMapping("/study/delete")
    public String studyDelete(Model model) {
        return "/study/studyDelete";
    }

    @PostMapping("/study/delete")
    public String studyDelete(Model model, @AuthenticationPrincipal GeneralMember generalMember, long studyId) {
        Study studyById = studyService.findById(studyId);
        studyService.delete(generalMember.getMember(), studyById);

        return "redirect:/";
    }*//*



    //READ

    @GetMapping("/study/search")
    public String search(Model model) {
        return "/study/studySearch";
    }

    @PostMapping("/study/search")
    public String search(Model model, String keyword) {
        List<StudyResponseDTO> search = studyService.search(keyword);
        model.addAttribute("studySearchResult", search);

        return "/study/studySearchResult";
    }


    @GetMapping("/study/list")
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    public String studyList(Model model) {

        return "/study/studyList";
    }*/

    //UPDATE
}
