package com.run.runners.service;

import com.run.runners.entity.Comment;
import com.run.runners.entity.Post;
import com.run.runners.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    
    private final CommentRepository commentRepository;
    
    @Transactional
    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }
    
    public List<Comment> getCommentsByPost(Post post) {
        return commentRepository.findByPostOrderByCreatedAtAsc(post);
    }
    
    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostIdOrderByCreatedAtAsc(postId);
    }
    
    public Optional<Comment> getCommentById(Long id) {
        return commentRepository.findById(id);
    }
    
    public long getCommentCountByPostId(Long postId) {
        return commentRepository.countByPostId(postId);
    }
    
    @Transactional
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
    
    @Transactional
    public void deleteCommentsByPost(Post post) {
        commentRepository.deleteByPost(post);
    }
    
    @Transactional
    public Comment updateComment(Comment comment) {
        return commentRepository.save(comment);
    }
}