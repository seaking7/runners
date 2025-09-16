package com.run.runners.repository;

import com.run.runners.entity.RunningStatistics;
import com.run.runners.entity.RunningStatistics.StatisticsPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RunningStatisticsRepository extends JpaRepository<RunningStatistics, Long> {
    
    List<RunningStatistics> findByRunnerNameOrderByPeriodYearDescPeriodWeekDescPeriodMonthDesc(String runnerName);
    
    List<RunningStatistics> findByRunnerNameAndPeriodOrderByPeriodYearDescPeriodWeekDescPeriodMonthDesc(String runnerName, StatisticsPeriod period);
    
    Optional<RunningStatistics> findByRunnerNameAndPeriodAndPeriodYear(String runnerName, StatisticsPeriod period, Integer periodYear);
    
    Optional<RunningStatistics> findByRunnerNameAndPeriodAndPeriodYearAndPeriodMonth(String runnerName, StatisticsPeriod period, Integer periodYear, Integer periodMonth);
    
    Optional<RunningStatistics> findByRunnerNameAndPeriodAndPeriodYearAndPeriodWeek(String runnerName, StatisticsPeriod period, Integer periodYear, Integer periodWeek);
    
    @Query("SELECT DISTINCT rs.runnerName FROM RunningStatistics rs ORDER BY rs.runnerName")
    List<String> findAllRunnerNames();
    
    @Query("SELECT rs FROM RunningStatistics rs WHERE rs.period = :period AND rs.periodYear = :year ORDER BY rs.totalDistanceKm DESC")
    List<RunningStatistics> findTopRunnersByPeriodAndYear(@Param("period") StatisticsPeriod period, @Param("year") Integer year);
    
    @Query("SELECT rs FROM RunningStatistics rs WHERE rs.period = :period AND rs.periodYear = :year AND rs.periodMonth = :month ORDER BY rs.totalDistanceKm DESC")
    List<RunningStatistics> findTopRunnersByPeriodAndYearAndMonth(@Param("period") StatisticsPeriod period, @Param("year") Integer year, @Param("month") Integer month);
    
    @Query("SELECT rs FROM RunningStatistics rs WHERE rs.period = :period AND rs.periodYear = :year AND rs.periodWeek = :week ORDER BY rs.totalDistanceKm DESC")
    List<RunningStatistics> findTopRunnersByPeriodAndYearAndWeek(@Param("period") StatisticsPeriod period, @Param("year") Integer year, @Param("week") Integer week);
}