package com.run.runners.controller;

import com.run.runners.entity.Competition;
import com.run.runners.service.CompetitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class WebController {
    
    private final CompetitionService competitionService;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/competitions")
    public String competitions(Model model) {
        model.addAttribute("competitions", competitionService.getAllCompetitions());
        return "competitions/list";
    }

    @GetMapping("/competitions/register")
    public String competitionRegisterForm(Model model) {
        model.addAttribute("competition", new Competition());
        return "competitions/register";
    }

    @PostMapping("/competitions/register")
    public String competitionRegister(@ModelAttribute Competition competition, RedirectAttributes redirectAttributes) {
        try {
            competitionService.saveCompetition(competition);
            redirectAttributes.addFlashAttribute("successMessage", "대회가 성공적으로 등록되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "대회 등록 중 오류가 발생했습니다.");
        }
        return "redirect:/competitions";
    }

    @GetMapping("/competitions/{id}")
    public String competitionDetail(@PathVariable Long id, Model model) {
        Optional<Competition> competition = competitionService.getCompetitionById(id);
        if (competition.isPresent()) {
            model.addAttribute("competition", competition.get());
            return "competitions/detail";
        } else {
            return "redirect:/competitions";
        }
    }

    @GetMapping("/my-running")
    public String myRunning() {
        return "my-running";
    }

    @GetMapping("/community")
    public String community() {
        return "community";
    }

    @GetMapping("/help")
    public String help() {
        return "help";
    }
}