package com.run.runners.controller;

import com.run.runners.entity.Competition;
import com.run.runners.entity.Post;
import com.run.runners.entity.Comment;
import com.run.runners.entity.Tips;
import com.run.runners.entity.Review;
import com.run.runners.service.CompetitionService;
import com.run.runners.service.PostService;
import com.run.runners.service.CommentService;
import com.run.runners.service.LikeService;
import com.run.runners.service.TipsService;
import com.run.runners.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class WebController {
    
    private final CompetitionService competitionService;
    private final PostService postService;
    private final CommentService commentService;
    private final LikeService likeService;
    private final TipsService tipsService;
    private final ReviewService reviewService;

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
    public String postDetail(@PathVariable Long id, Model model, HttpServletRequest request) {
        Optional<Post> post = postService.getPostByIdAndIncrementView(id);
        if (post.isPresent()) {
            Post currentPost = post.get();
            String userIdentifier = request.getRemoteAddr(); // Use IP address as user identifier
            
            model.addAttribute("post", currentPost);
            model.addAttribute("comments", commentService.getCommentsByPostId(id));
            model.addAttribute("newComment", new Comment());
            model.addAttribute("isLiked", likeService.isLikedByUser(id, userIdentifier));
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

    // 댓글 관련 매핑
    @PostMapping("/community/board/{postId}/comments")
    public String addComment(@PathVariable Long postId, 
                           @ModelAttribute Comment comment,
                           RedirectAttributes redirectAttributes) {
        try {
            Optional<Post> post = postService.getPostById(postId);
            if (post.isPresent()) {
                comment.setPost(post.get());
                commentService.saveComment(comment);
                redirectAttributes.addFlashAttribute("successMessage", "댓글이 성공적으로 등록되었습니다.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "게시글을 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "댓글 등록 중 오류가 발생했습니다.");
        }
        return "redirect:/community/board/" + postId;
    }

    @PostMapping("/community/board/{postId}/comments/{commentId}/delete")
    public String deleteComment(@PathVariable Long postId, 
                              @PathVariable Long commentId,
                              RedirectAttributes redirectAttributes) {
        try {
            commentService.deleteComment(commentId);
            redirectAttributes.addFlashAttribute("successMessage", "댓글이 삭제되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "댓글 삭제 중 오류가 발생했습니다.");
        }
        return "redirect:/community/board/" + postId;
    }

    // 좋아요 관련 매핑
    @PostMapping("/community/board/{postId}/like")
    @ResponseBody
    public ResponseEntity<?> toggleLike(@PathVariable Long postId, HttpServletRequest request) {
        try {
            String userIdentifier = request.getRemoteAddr();
            boolean isLiked = likeService.toggleLike(postId, userIdentifier);
            long likeCount = likeService.getLikeCount(postId);
            
            return ResponseEntity.ok().body(new LikeResponse(isLiked, likeCount));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("좋아요 처리 중 오류가 발생했습니다.");
        }
    }
    
    // 좋아요 응답 클래스
    public static class LikeResponse {
        private boolean liked;
        private long likeCount;
        
        public LikeResponse(boolean liked, long likeCount) {
            this.liked = liked;
            this.likeCount = likeCount;
        }
        
        public boolean isLiked() {
            return liked;
        }
        
        public long getLikeCount() {
            return likeCount;
        }
    }
    
    // 팁&노하우 게시판 관련 매핑
    @GetMapping("/community/tips")
    public String tipsBoard(Model model) {
        model.addAttribute("tips", tipsService.getAllTips());
        return "community/tips";
    }

    @GetMapping("/community/tips/write")
    public String tipsWriteForm(Model model) {
        model.addAttribute("tips", new Tips());
        return "community/tips-write";
    }

    @PostMapping("/community/tips/write")
    public String tipsWrite(@ModelAttribute Tips tips, RedirectAttributes redirectAttributes) {
        try {
            tipsService.saveTips(tips);
            redirectAttributes.addFlashAttribute("successMessage", "팁이 성공적으로 등록되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "팁 등록 중 오류가 발생했습니다.");
        }
        return "redirect:/community/tips";
    }

    @GetMapping("/community/tips/{id}")
    public String tipsDetail(@PathVariable Long id, Model model) {
        Optional<Tips> tips = tipsService.getTipsByIdAndIncrementView(id);
        if (tips.isPresent()) {
            model.addAttribute("tips", tips.get());
            return "community/tips-detail";
        } else {
            return "redirect:/community/tips";
        }
    }

    @GetMapping("/community/tips/search")
    public String tipsSearch(@RequestParam(required = false) String keyword, 
                           @RequestParam(required = false) String type,
                           Model model) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            if ("title".equals(type)) {
                model.addAttribute("tips", tipsService.searchByTitle(keyword));
            } else if ("author".equals(type)) {
                model.addAttribute("tips", tipsService.searchByAuthor(keyword));
            } else {
                model.addAttribute("tips", tipsService.searchByTitleOrContent(keyword));
            }
            model.addAttribute("keyword", keyword);
            model.addAttribute("type", type);
        } else {
            model.addAttribute("tips", tipsService.getAllTips());
        }
        return "community/tips";
    }
    
    // 달리기 후기 게시판 관련 매핑
    @GetMapping("/community/reviews")
    public String reviewsBoard(Model model) {
        model.addAttribute("reviews", reviewService.getAllReviews());
        return "community/reviews";
    }

    @GetMapping("/community/reviews/write")
    public String reviewWriteForm(Model model) {
        model.addAttribute("review", new Review());
        return "community/reviews-write";
    }

    @PostMapping("/community/reviews/write")
    public String reviewWrite(@ModelAttribute Review review, RedirectAttributes redirectAttributes) {
        try {
            reviewService.saveReview(review);
            redirectAttributes.addFlashAttribute("successMessage", "달리기 후기가 성공적으로 등록되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "달리기 후기 등록 중 오류가 발생했습니다.");
        }
        return "redirect:/community/reviews";
    }

    @GetMapping("/community/reviews/{id}")
    public String reviewDetail(@PathVariable Long id, Model model) {
        Optional<Review> review = reviewService.getReviewByIdAndIncrementView(id);
        if (review.isPresent()) {
            model.addAttribute("review", review.get());
            return "community/reviews-detail";
        } else {
            return "redirect:/community/reviews";
        }
    }

    @GetMapping("/community/reviews/search")
    public String reviewSearch(@RequestParam(required = false) String keyword, 
                             @RequestParam(required = false) String type,
                             Model model) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            if ("title".equals(type)) {
                model.addAttribute("reviews", reviewService.searchByTitle(keyword));
            } else if ("author".equals(type)) {
                model.addAttribute("reviews", reviewService.searchByAuthor(keyword));
            } else {
                model.addAttribute("reviews", reviewService.searchByTitleOrContent(keyword));
            }
            model.addAttribute("keyword", keyword);
            model.addAttribute("type", type);
        } else {
            model.addAttribute("reviews", reviewService.getAllReviews());
        }
        return "community/reviews";
    }
}