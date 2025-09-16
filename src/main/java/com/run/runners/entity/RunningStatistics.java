package com.run.runners.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "running_statistics")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RunningStatistics {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String runnerName;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatisticsPeriod period;
    
    @Column(nullable = false)
    private Integer periodYear;
    
    @Column
    private Integer periodWeek;
    
    @Column
    private Integer periodMonth;
    
    @Column(nullable = false)
    private Integer totalRuns = 0;
    
    @Column(nullable = false)
    private Double totalDistanceKm = 0.0;
    
    @Column(nullable = false)
    private Integer totalTimeMinutes = 0;
    
    @Column(nullable = false)
    private Double totalCaloriesBurned = 0.0;
    
    @Column
    private Double averageSpeedKmh = 0.0;
    
    @Column
    private Double averagePaceMinutesPerKm = 0.0;
    
    @Column
    private Integer averageHeartRate = 0;
    
    @Column
    private Integer averageCadence = 0;
    
    @Column
    private Double maxSpeedKmh = 0.0;
    
    @Column
    private Double maxDistanceKm = 0.0;
    
    @Column
    private Integer maxTimeMinutes = 0;
    
    @Column(nullable = false)
    private LocalDateTime lastCalculatedAt;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        lastCalculatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        lastCalculatedAt = LocalDateTime.now();
    }
    
    public enum StatisticsPeriod {
        WEEKLY, MONTHLY, YEARLY
    }
    
    public String getFormattedTotalTime() {
        if (totalTimeMinutes == null || totalTimeMinutes == 0) {
            return "0분";
        }
        int hours = totalTimeMinutes / 60;
        int minutes = totalTimeMinutes % 60;
        if (hours > 0) {
            return hours + "시간 " + minutes + "분";
        }
        return minutes + "분";
    }
    
    public String getFormattedAveragePace() {
        if (averagePaceMinutesPerKm == null || averagePaceMinutesPerKm == 0) {
            return "0:00/km";
        }
        int minutes = averagePaceMinutesPerKm.intValue();
        int seconds = (int) ((averagePaceMinutesPerKm - minutes) * 60);
        return String.format("%d:%02d/km", minutes, seconds);
    }
    
    public String getPeriodDisplay() {
        switch (period) {
            case WEEKLY:
                return periodYear + "년 " + periodWeek + "주차";
            case MONTHLY:
                return periodYear + "년 " + periodMonth + "월";
            case YEARLY:
                return periodYear + "년";
            default:
                return "";
        }
    }
}