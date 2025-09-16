package com.run.runners.service;

import com.run.runners.entity.RunningRecord;
import com.run.runners.entity.RunningStatistics;
import com.run.runners.entity.RunningStatistics.StatisticsPeriod;
import com.run.runners.repository.RunningRecordRepository;
import com.run.runners.repository.RunningStatisticsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.IsoFields;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RunningStatisticsService {
    
    private final RunningRecordRepository runningRecordRepository;
    private final RunningStatisticsRepository runningStatisticsRepository;
    
    @Scheduled(fixedDelay = 600000) // Every 10 minutes (600,000 milliseconds)
    @Transactional
    public void generateStatistics() {
        log.info("Starting automatic statistics generation...");
        
        List<String> runnerNames = runningRecordRepository.findDistinctRunnerNames();
        LocalDateTime now = LocalDateTime.now();
        
        for (String runnerName : runnerNames) {
            try {
                generateWeeklyStatistics(runnerName, now);
                generateMonthlyStatistics(runnerName, now);
                generateYearlyStatistics(runnerName, now);
                log.info("Generated statistics for runner: {}", runnerName);
            } catch (Exception e) {
                log.error("Error generating statistics for runner: {}", runnerName, e);
            }
        }
        
        log.info("Completed automatic statistics generation");
    }
    
    @Transactional
    public void generateWeeklyStatistics(String runnerName, LocalDateTime now) {
        int year = now.getYear();
        int week = now.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
        
        List<RunningRecord> weeklyRecords = runningRecordRepository.findByRunnerNameAndWeek(runnerName, year, week);
        
        Optional<RunningStatistics> existingStats = runningStatisticsRepository
            .findByRunnerNameAndPeriodAndPeriodYearAndPeriodWeek(runnerName, StatisticsPeriod.WEEKLY, year, week);
        
        RunningStatistics stats = existingStats.orElse(new RunningStatistics());
        
        stats.setRunnerName(runnerName);
        stats.setPeriod(StatisticsPeriod.WEEKLY);
        stats.setPeriodYear(year);
        stats.setPeriodWeek(week);
        
        calculateStatistics(stats, weeklyRecords);
        
        runningStatisticsRepository.save(stats);
    }
    
    @Transactional
    public void generateMonthlyStatistics(String runnerName, LocalDateTime now) {
        int year = now.getYear();
        int month = now.getMonthValue();
        
        List<RunningRecord> monthlyRecords = runningRecordRepository.findByRunnerNameAndMonth(runnerName, year, month);
        
        Optional<RunningStatistics> existingStats = runningStatisticsRepository
            .findByRunnerNameAndPeriodAndPeriodYearAndPeriodMonth(runnerName, StatisticsPeriod.MONTHLY, year, month);
        
        RunningStatistics stats = existingStats.orElse(new RunningStatistics());
        
        stats.setRunnerName(runnerName);
        stats.setPeriod(StatisticsPeriod.MONTHLY);
        stats.setPeriodYear(year);
        stats.setPeriodMonth(month);
        
        calculateStatistics(stats, monthlyRecords);
        
        runningStatisticsRepository.save(stats);
    }
    
    @Transactional
    public void generateYearlyStatistics(String runnerName, LocalDateTime now) {
        int year = now.getYear();
        
        List<RunningRecord> yearlyRecords = runningRecordRepository.findByRunnerNameAndYear(runnerName, year);
        
        Optional<RunningStatistics> existingStats = runningStatisticsRepository
            .findByRunnerNameAndPeriodAndPeriodYear(runnerName, StatisticsPeriod.YEARLY, year);
        
        RunningStatistics stats = existingStats.orElse(new RunningStatistics());
        
        stats.setRunnerName(runnerName);
        stats.setPeriod(StatisticsPeriod.YEARLY);
        stats.setPeriodYear(year);
        
        calculateStatistics(stats, yearlyRecords);
        
        runningStatisticsRepository.save(stats);
    }
    
    private void calculateStatistics(RunningStatistics stats, List<RunningRecord> records) {
        if (records.isEmpty()) {
            stats.setTotalRuns(0);
            stats.setTotalDistanceKm(0.0);
            stats.setTotalTimeMinutes(0);
            stats.setTotalCaloriesBurned(0.0);
            stats.setAverageSpeedKmh(0.0);
            stats.setAveragePaceMinutesPerKm(0.0);
            stats.setAverageHeartRate(0);
            stats.setAverageCadence(0);
            stats.setMaxSpeedKmh(0.0);
            stats.setMaxDistanceKm(0.0);
            stats.setMaxTimeMinutes(0);
            return;
        }
        
        stats.setTotalRuns(records.size());
        
        double totalDistance = records.stream()
            .mapToDouble(RunningRecord::getDistanceKm)
            .sum();
        stats.setTotalDistanceKm(Math.round(totalDistance * 100.0) / 100.0);
        
        int totalTime = records.stream()
            .mapToInt(RunningRecord::getRunTimeMinutes)
            .sum();
        stats.setTotalTimeMinutes(totalTime);
        
        double totalCalories = records.stream()
            .mapToDouble(RunningRecord::getCaloriesBurned)
            .sum();
        stats.setTotalCaloriesBurned(Math.round(totalCalories * 10.0) / 10.0);
        
        double averageSpeed = totalTime > 0 ? (totalDistance / totalTime) * 60 : 0.0;
        stats.setAverageSpeedKmh(Math.round(averageSpeed * 100.0) / 100.0);
        
        double averagePace = totalDistance > 0 ? totalTime / totalDistance : 0.0;
        stats.setAveragePaceMinutesPerKm(Math.round(averagePace * 100.0) / 100.0);
        
        double averageHeartRate = records.stream()
            .mapToInt(RunningRecord::getMaxHeartRate)
            .average()
            .orElse(0.0);
        stats.setAverageHeartRate((int) Math.round(averageHeartRate));
        
        double averageCadence = records.stream()
            .mapToInt(RunningRecord::getCadence)
            .average()
            .orElse(0.0);
        stats.setAverageCadence((int) Math.round(averageCadence));
        
        double maxSpeed = records.stream()
            .mapToDouble(RunningRecord::getMaxSpeedKmh)
            .max()
            .orElse(0.0);
        stats.setMaxSpeedKmh(Math.round(maxSpeed * 100.0) / 100.0);
        
        double maxDistance = records.stream()
            .mapToDouble(RunningRecord::getDistanceKm)
            .max()
            .orElse(0.0);
        stats.setMaxDistanceKm(Math.round(maxDistance * 100.0) / 100.0);
        
        int maxTime = records.stream()
            .mapToInt(RunningRecord::getRunTimeMinutes)
            .max()
            .orElse(0);
        stats.setMaxTimeMinutes(maxTime);
    }
    
    public List<RunningStatistics> getStatisticsByRunner(String runnerName) {
        return runningStatisticsRepository.findByRunnerNameOrderByPeriodYearDescPeriodWeekDescPeriodMonthDesc(runnerName);
    }
    
    public List<RunningStatistics> getStatisticsByRunnerAndPeriod(String runnerName, StatisticsPeriod period) {
        return runningStatisticsRepository.findByRunnerNameAndPeriodOrderByPeriodYearDescPeriodWeekDescPeriodMonthDesc(runnerName, period);
    }
    
    public List<String> getAllRunnerNames() {
        return runningStatisticsRepository.findAllRunnerNames();
    }
    
    public List<RunningStatistics> getTopRunnersByPeriod(StatisticsPeriod period, Integer year, Integer month, Integer week) {
        switch (period) {
            case WEEKLY:
                return runningStatisticsRepository.findTopRunnersByPeriodAndYearAndWeek(period, year, week);
            case MONTHLY:
                return runningStatisticsRepository.findTopRunnersByPeriodAndYearAndMonth(period, year, month);
            case YEARLY:
                return runningStatisticsRepository.findTopRunnersByPeriodAndYear(period, year);
            default:
                return List.of();
        }
    }
    
    @Transactional
    public void generateImmediateStatistics() {
        log.info("Generating immediate statistics for all runners...");
        generateStatistics();
    }
}