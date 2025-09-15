package com.run.runners.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "competitions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Competition {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private LocalDateTime eventDateTime;
    
    @Column(nullable = false)
    private String location;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String course;
    
    @Column(columnDefinition = "TEXT")
    private String gifts;
    
    @Column(nullable = false)
    private Integer participationFee;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}