package com.run.runners.repository;

import com.run.runners.entity.Like;
import com.run.runners.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    
    Optional<Like> findByPostAndUserIdentifier(Post post, String userIdentifier);
    
    boolean existsByPostAndUserIdentifier(Post post, String userIdentifier);
    
    void deleteByPostAndUserIdentifier(Post post, String userIdentifier);
    
    long countByPost(Post post);
    
    long countByPostId(Long postId);
}