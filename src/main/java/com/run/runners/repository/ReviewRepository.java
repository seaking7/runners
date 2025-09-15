package com.run.runners.repository;

import com.run.runners.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    
    List<Review> findAllByOrderByCreatedAtDesc();
    
    List<Review> findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(String title);
    
    List<Review> findByAuthorContainingIgnoreCaseOrderByCreatedAtDesc(String author);
    
    @Query("SELECT r FROM Review r WHERE LOWER(r.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(r.content) LIKE LOWER(CONCAT('%', :keyword, '%')) ORDER BY r.createdAt DESC")
    List<Review> findByTitleOrContentContaining(@Param("keyword") String keyword);
    
    List<Review> findAllByOrderByViewCountDesc();
    
    List<Review> findAllByOrderByLikeCountDesc();
}