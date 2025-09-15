package com.run.runners.controller;

import com.run.runners.entity.Competition;
import com.run.runners.entity.Post;
import com.run.runners.service.CompetitionService;
import com.run.runners.service.PostService;
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
    private final PostService postService;

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

    // 커뮤니티 자유게시판 관련 매핑
    @GetMapping("/community/board")
    public String communityBoard(Model model) {
        model.addAttribute("posts", postService.getAllPosts());
        return "community/board";
    }

    @GetMapping("/community/board/write")
    public String postWriteForm(Model model) {
        model.addAttribute("post", new Post());
        return "community/write";
    }

    @PostMapping("/community/board/write")
    public String postWrite(@ModelAttribute Post post, RedirectAttributes redirectAttributes) {
        try {
            postService.savePost(post);
            redirectAttributes.addFlashAttribute("successMessage", "게시글이 성공적으로 등록되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "게시글 등록 중 오류가 발생했습니다.");
        }
        return "redirect:/community/board";
    }

    @GetMapping("/community/board/{id}")
    public String postDetail(@PathVariable Long id, Model model) {
        Optional<Post> post = postService.getPostByIdAndIncrementView(id);
        if (post.isPresent()) {
            model.addAttribute("post", post.get());
            return "community/detail";
        } else {
            return "redirect:/community/board";
        }
    }

    @GetMapping("/community/board/search")
    public String postSearch(@RequestParam(required = false) String keyword, 
                           @RequestParam(required = false) String type,
                           Model model) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            if ("title".equals(type)) {
                model.addAttribute("posts", postService.searchByTitle(keyword));
            } else if ("author".equals(type)) {
                model.addAttribute("posts", postService.searchByAuthor(keyword));
            } else {
                model.addAttribute("posts", postService.searchByTitleOrContent(keyword));
            }
            model.addAttribute("keyword", keyword);
            model.addAttribute("type", type);
        } else {
            model.addAttribute("posts", postService.getAllPosts());
        }
        return "community/board";
    }
}