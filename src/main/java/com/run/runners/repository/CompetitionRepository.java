package com.run.runners.repository;

import com.run.runners.entity.Competition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CompetitionRepository extends JpaRepository<Competition, Long> {
    
    List<Competition> findByEventDateTimeAfterOrderByEventDateTimeAsc(LocalDateTime dateTime);
    
    List<Competition> findByLocationContainingIgnoreCase(String location);
    
    @Query("SELECT c FROM Competition c WHERE c.eventDateTime >= :startDate ORDER BY c.eventDateTime ASC")
    List<Competition> findUpcomingCompetitions(LocalDateTime startDate);
    
    @Query("SELECT c FROM Competition c ORDER BY c.createdAt DESC")
    List<Competition> findAllOrderByCreatedAtDesc();
}