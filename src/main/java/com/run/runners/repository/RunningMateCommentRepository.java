package com.run.runners.repository;

import com.run.runners.entity.RunningMateComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RunningMateCommentRepository extends JpaRepository<RunningMateComment, Long> {
    
    @Query("SELECT c FROM RunningMateComment c WHERE c.runningMate.id = :runningMateId ORDER BY c.createdAt ASC")
    List<RunningMateComment> findByRunningMateIdOrderByCreatedAtAsc(@Param("runningMateId") Long runningMateId);
    
    @Query("SELECT COUNT(c) FROM RunningMateComment c WHERE c.runningMate.id = :runningMateId")
    Long countByRunningMateId(@Param("runningMateId") Long runningMateId);
}