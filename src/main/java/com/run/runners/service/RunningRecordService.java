package com.run.runners.service;

import com.run.runners.entity.RunningRecord;
import com.run.runners.repository.RunningRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RunningRecordService {
    
    private final RunningRecordRepository runningRecordRepository;
    
    @Transactional
    public RunningRecord saveRunningRecord(RunningRecord runningRecord) {
        return runningRecordRepository.save(runningRecord);
    }
    
    public List<RunningRecord> getAllRunningRecords() {
        return runningRecordRepository.findAllOrderByRecordDateDesc();
    }
    
    public Optional<RunningRecord> getRunningRecordById(Long id) {
        return runningRecordRepository.findById(id);
    }
    
    public List<RunningRecord> getRunningRecordsByRunner(String runnerName) {
        return runningRecordRepository.findByRunnerNameOrderByRecordDateDesc(runnerName);
    }
    
    public List<RunningRecord> searchByRunnerName(String runnerName) {
        return runningRecordRepository.findByRunnerNameContainingIgnoreCaseOrderByRecordDateDesc(runnerName);
    }
    
    public List<RunningRecord> getRunningRecordsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return runningRecordRepository.findByRecordDateBetweenOrderByRecordDateDesc(startDate, endDate);
    }
    
    public Long getRecordCountByRunner(String runnerName) {
        return runningRecordRepository.countByRunnerName(runnerName);
    }
    
    public Double getTotalDistanceByRunner(String runnerName) {
        Double totalDistance = runningRecordRepository.getTotalDistanceByRunnerName(runnerName);
        return totalDistance != null ? totalDistance : 0.0;
    }
    
    public Integer getTotalRunTimeByRunner(String runnerName) {
        Integer totalRunTime = runningRecordRepository.getTotalRunTimeByRunnerName(runnerName);
        return totalRunTime != null ? totalRunTime : 0;
    }
    
    public Double getAverageDistanceByRunner(String runnerName) {
        Long recordCount = getRecordCountByRunner(runnerName);
        if (recordCount == 0) return 0.0;
        
        Double totalDistance = getTotalDistanceByRunner(runnerName);
        return totalDistance / recordCount;
    }
    
    public Double getAverageSpeedByRunner(String runnerName) {
        Integer totalRunTime = getTotalRunTimeByRunner(runnerName);
        Double totalDistance = getTotalDistanceByRunner(runnerName);
        
        if (totalRunTime == 0 || totalDistance == 0) return 0.0;
        
        return (totalDistance / totalRunTime) * 60; // km/h
    }
    
    @Transactional
    public void deleteRunningRecord(Long id) {
        runningRecordRepository.deleteById(id);
    }
    
    @Transactional
    public RunningRecord updateRunningRecord(RunningRecord runningRecord) {
        return runningRecordRepository.save(runningRecord);
    }
}