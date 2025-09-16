package com.run.runners.repository;

import com.run.runners.entity.RunningMate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RunningMateRepository extends JpaRepository<RunningMate, Long> {
    
    @Query("SELECT r FROM RunningMate r ORDER BY r.createdAt DESC")
    List<RunningMate> findAllOrderByCreatedAtDesc();
    
    @Query("SELECT r FROM RunningMate r WHERE r.title LIKE %:keyword% ORDER BY r.createdAt DESC")
    List<RunningMate> findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(@Param("keyword") String keyword);
    
    @Query("SELECT r FROM RunningMate r WHERE r.author LIKE %:keyword% ORDER BY r.createdAt DESC")
    List<RunningMate> findByAuthorContainingIgnoreCaseOrderByCreatedAtDesc(@Param("keyword") String keyword);
    
    @Query("SELECT r FROM RunningMate r WHERE r.title LIKE %:keyword% OR r.content LIKE %:keyword% ORDER BY r.createdAt DESC")
    List<RunningMate> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCaseOrderByCreatedAtDesc(@Param("keyword") String keyword1, @Param("keyword") String keyword2);
    
    @Query("SELECT r FROM RunningMate r WHERE r.location LIKE %:location% ORDER BY r.createdAt DESC")
    List<RunningMate> findByLocationContainingIgnoreCaseOrderByCreatedAtDesc(@Param("location") String location);
    
    @Modifying
    @Query("UPDATE RunningMate r SET r.viewCount = r.viewCount + 1 WHERE r.id = :id")
    void incrementViewCount(@Param("id") Long id);
}