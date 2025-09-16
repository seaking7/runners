package com.run.runners.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "running_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RunningRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String runnerName;
    
    @Column(nullable = false)
    private Integer runTimeMinutes;
    
    @Column(nullable = false)
    private Double distanceKm;
    
    @Column(nullable = false)
    private Integer cadence;
    
    @Column(nullable = false)
    private Integer maxHeartRate;
    
    @Column(nullable = false)
    private Double maxSpeedKmh;
    
    @Column(nullable = false)
    private LocalDateTime recordDate;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (recordDate == null) {
            recordDate = LocalDateTime.now();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public Double getAverageSpeedKmh() {
        if (runTimeMinutes == null || runTimeMinutes == 0 || distanceKm == null) {
            return 0.0;
        }
        return (distanceKm / runTimeMinutes) * 60;
    }
    
    public String getFormattedRunTime() {
        if (runTimeMinutes == null) {
            return "0분";
        }
        int hours = runTimeMinutes / 60;
        int minutes = runTimeMinutes % 60;
        if (hours > 0) {
            return hours + "시간 " + minutes + "분";
        }
        return minutes + "분";
    }
    
    public String getFormattedPace() {
        if (runTimeMinutes == null || runTimeMinutes == 0 || distanceKm == null || distanceKm == 0) {
            return "0:00/km";
        }
        double paceMinutesPerKm = runTimeMinutes / distanceKm;
        int minutes = (int) paceMinutesPerKm;
        int seconds = (int) ((paceMinutesPerKm - minutes) * 60);
        return String.format("%d:%02d/km", minutes, seconds);
    }
}