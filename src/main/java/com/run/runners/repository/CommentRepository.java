package com.run.runners.repository;

import com.run.runners.entity.Comment;
import com.run.runners.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    List<Comment> findByPostOrderByCreatedAtAsc(Post post);
    
    List<Comment> findByPostIdOrderByCreatedAtAsc(Long postId);
    
    List<Comment> findByAuthorContainingIgnoreCaseOrderByCreatedAtDesc(String author);
    
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.post.id = :postId")
    long countByPostId(Long postId);
    
    void deleteByPost(Post post);
}