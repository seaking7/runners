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
    
    @Column(nullable = false)
    private Double caloriesBurned;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (recordDate == null) {
            recordDate = LocalDateTime.now();
        }
        // Automatically calculate calories when record is created
        this.caloriesBurned = calculateCalories();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        // Recalculate calories when record is updated
        this.caloriesBurned = calculateCalories();
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
    
    /**
     * Calculate calories burned based on running speed, distance, and time
     * Uses MET (Metabolic Equivalent of Task) formula with standard body weight
     * Formula: Calories = (Time × MET × 3.5 × Weight) / 200
     * Standard values: 70kg body weight (average adult)
     */
    public Double calculateCalories() {
        if (runTimeMinutes == null || runTimeMinutes == 0 || distanceKm == null || distanceKm == 0) {
            return 0.0;
        }
        
        // Standard body weight in kg (70kg = average adult)
        double standardWeightKg = 70.0;
        
        // Calculate average speed in km/h
        double averageSpeedKmh = getAverageSpeedKmh();
        
        // Get MET value based on running speed
        double metValue = getMetValueFromSpeed(averageSpeedKmh);
        
        // Calculate calories using MET formula
        // Calories = (Time in minutes × MET × 3.5 × Weight in kg) / 200
        double calories = (runTimeMinutes * metValue * 3.5 * standardWeightKg) / 200;
        
        return Math.round(calories * 10.0) / 10.0; // Round to 1 decimal place
    }
    
    /**
     * Get MET value based on running speed in km/h
     * Based on Compendium of Physical Activities data
     */
    private double getMetValueFromSpeed(double speedKmh) {
        // Convert km/h to mph for MET lookup
        double speedMph = speedKmh * 0.621371;
        
        // MET values based on running speed (mph)
        if (speedMph < 4.0) {
            return 6.0; // Very slow jog
        } else if (speedMph < 4.3) {
            return 6.5; // 4.0-4.2 mph
        } else if (speedMph < 5.0) {
            return 7.8; // 4.3-4.8 mph
        } else if (speedMph < 5.5) {
            return 8.5; // 5.0-5.2 mph
        } else if (speedMph < 6.0) {
            return 9.0; // 5.5-5.8 mph
        } else if (speedMph < 6.7) {
            return 9.3; // 6.0-6.3 mph
        } else if (speedMph < 7.0) {
            return 10.5; // 6.7 mph
        } else if (speedMph < 7.5) {
            return 11.0; // 7.0 mph
        } else if (speedMph < 8.0) {
            return 11.5; // 7.5 mph
        } else if (speedMph < 8.6) {
            return 12.3; // 8.0 mph
        } else if (speedMph < 9.0) {
            return 12.8; // 8.6 mph
        } else if (speedMph < 10.0) {
            return 14.5; // 9.0 mph
        } else if (speedMph < 10.9) {
            return 15.3; // 10.0 mph
        } else if (speedMph < 12.0) {
            return 16.0; // 10.9 mph
        } else if (speedMph < 14.0) {
            return 19.0; // 12.0 mph
        } else if (speedMph < 16.0) {
            return 19.8; // 14.0 mph
        } else {
            return 23.0; // 16+ mph (very fast)
        }
    }
    
    /**
     * Get formatted calories string for display
     */
    public String getFormattedCalories() {
        if (caloriesBurned == null) {
            return "0 cal";
        }
        return Math.round(caloriesBurned) + " cal";
    }
}