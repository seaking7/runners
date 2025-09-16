package com.run.runners.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "r_running_mates")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RunningMate {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
    
    @Column(nullable = false)
    private String author;
    
    @Column(nullable = false)
    private String location;
    
    @Column(nullable = false)
    private Integer maxParticipants;
    
    @Column(nullable = false)
    private LocalDateTime runningDateTime;
    
    @Column(nullable = false)
    private Integer viewCount = 0;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "runningMate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RunningMateComment> comments = new ArrayList<>();
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public int getCommentCount() {
        return comments != null ? comments.size() : 0;
    }
    
    public int getCurrentParticipants() {
        return comments != null ? comments.size() : 0;
    }
    
    public boolean isFullyBooked() {
        return getCurrentParticipants() >= maxParticipants;
    }
}