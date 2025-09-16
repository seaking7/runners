package com.run.runners.repository;

import com.run.runners.entity.RunningRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RunningRecordRepository extends JpaRepository<RunningRecord, Long> {
    
    List<RunningRecord> findByRunnerNameContainingIgnoreCaseOrderByRecordDateDesc(String runnerName);
    
    @Query("SELECT r FROM RunningRecord r ORDER BY r.recordDate DESC")
    List<RunningRecord> findAllOrderByRecordDateDesc();
    
    @Query("SELECT r FROM RunningRecord r WHERE r.recordDate BETWEEN :startDate AND :endDate ORDER BY r.recordDate DESC")
    List<RunningRecord> findByRecordDateBetweenOrderByRecordDateDesc(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT r FROM RunningRecord r WHERE r.runnerName = :runnerName ORDER BY r.recordDate DESC")
    List<RunningRecord> findByRunnerNameOrderByRecordDateDesc(@Param("runnerName") String runnerName);
    
    @Query("SELECT COUNT(r) FROM RunningRecord r WHERE r.runnerName = :runnerName")
    Long countByRunnerName(@Param("runnerName") String runnerName);
    
    @Query("SELECT SUM(r.distanceKm) FROM RunningRecord r WHERE r.runnerName = :runnerName")
    Double getTotalDistanceByRunnerName(@Param("runnerName") String runnerName);
    
    @Query("SELECT SUM(r.runTimeMinutes) FROM RunningRecord r WHERE r.runnerName = :runnerName")
    Integer getTotalRunTimeByRunnerName(@Param("runnerName") String runnerName);
    
    @Query("SELECT DISTINCT r.runnerName FROM RunningRecord r ORDER BY r.runnerName")
    List<String> findDistinctRunnerNames();
    
    @Query("SELECT r FROM RunningRecord r WHERE r.runnerName = :runnerName AND YEAR(r.recordDate) = :year AND WEEK(r.recordDate) = :week ORDER BY r.recordDate DESC")
    List<RunningRecord> findByRunnerNameAndWeek(@Param("runnerName") String runnerName, @Param("year") Integer year, @Param("week") Integer week);
    
    @Query("SELECT r FROM RunningRecord r WHERE r.runnerName = :runnerName AND YEAR(r.recordDate) = :year AND MONTH(r.recordDate) = :month ORDER BY r.recordDate DESC")
    List<RunningRecord> findByRunnerNameAndMonth(@Param("runnerName") String runnerName, @Param("year") Integer year, @Param("month") Integer month);
    
    @Query("SELECT r FROM RunningRecord r WHERE r.runnerName = :runnerName AND YEAR(r.recordDate) = :year ORDER BY r.recordDate DESC")
    List<RunningRecord> findByRunnerNameAndYear(@Param("runnerName") String runnerName, @Param("year") Integer year);
}