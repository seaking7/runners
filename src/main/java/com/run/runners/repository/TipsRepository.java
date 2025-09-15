package com.run.runners.repository;

import com.run.runners.entity.Tips;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TipsRepository extends JpaRepository<Tips, Long> {
    
    // 최신순으로 모든 팁 조회
    List<Tips> findAllByOrderByCreatedAtDesc();
    
    // 제목으로 검색
    List<Tips> findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(String title);
    
    // 작성자로 검색
    List<Tips> findByAuthorContainingIgnoreCaseOrderByCreatedAtDesc(String author);
    
    // 제목 또는 내용으로 검색
    @Query("SELECT t FROM Tips t WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(t.content) LIKE LOWER(CONCAT('%', :keyword, '%')) ORDER BY t.createdAt DESC")
    List<Tips> findByTitleOrContentContaining(@Param("keyword") String keyword);
    
    // 인기순 정렬 (조회수 높은 순)
    List<Tips> findAllByOrderByViewCountDesc();
    
    // 좋아요 많은 순 정렬
    List<Tips> findAllByOrderByLikeCountDesc();
}