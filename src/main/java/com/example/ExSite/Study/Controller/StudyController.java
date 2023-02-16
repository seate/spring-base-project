package com.example.ExSite.Study.Controller;

import com.example.ExSite.Member.domain.GeneralMember;
import com.example.ExSite.Study.domain.Study;
import com.example.ExSite.Study.service.StudyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class StudyController {

    private final StudyService studyService;

    @Autowired
    public StudyController(StudyService studyService) {
        this.studyService = studyService;
    }

    //CREATE

    @GetMapping("study/create")
    public String studyCreate() {
        return "study/studyCreate";
    }

    @PostMapping("/study/create")
    public String studyCreate(@AuthenticationPrincipal GeneralMember generalMember, Model model,
                              String studyName, int maxUserCount, String goal, String details) {
        studyService.create(studyName, maxUserCount, goal, details, generalMember.getMember());
        return "redirect:/";
    }

    //DELETE

    @GetMapping("/study/delete")
    public String studyDelete(Model model) {
        return "/study/studyDelete";
    }

    @PostMapping("/study/delete")
    public String studyDelete(Model model, @AuthenticationPrincipal GeneralMember generalMember, long studyId) {
        Study studyById = studyService.findById(studyId);
        studyService.delete(generalMember.getMember(), studyById);

        return "redirect:/";
    }



    //READ

    @GetMapping("/study/search")
    public String search(Model model) {
        return "/study/studySearch";
    }

    @PostMapping("/study/search")
    public String search(Model model, String keyword) {
        List<Study> search = studyService.search(keyword);
        model.addAttribute("studySearchResult", search);

        return "/study/studySearchResult";
    }


    @GetMapping("/study/list")
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    public String studyList(Model model) {
        List<Study> list = studyService.list();
        model.addAttribute("studies", list);

        return "/study/studyList";
    }

    //UPDATE
}
