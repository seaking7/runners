package com.run.runners.service;

import com.run.runners.entity.Competition;
import com.run.runners.repository.CompetitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompetitionService {
    
    private final CompetitionRepository competitionRepository;
    
    @Transactional
    public Competition saveCompetition(Competition competition) {
        return competitionRepository.save(competition);
    }
    
    public List<Competition> getAllCompetitions() {
        return competitionRepository.findAllOrderByCreatedAtDesc();
    }
    
    public List<Competition> getUpcomingCompetitions() {
        return competitionRepository.findUpcomingCompetitions(LocalDateTime.now());
    }
    
    public Optional<Competition> getCompetitionById(Long id) {
        return competitionRepository.findById(id);
    }
    
    public List<Competition> searchByLocation(String location) {
        return competitionRepository.findByLocationContainingIgnoreCase(location);
    }
    
    @Transactional
    public void deleteCompetition(Long id) {
        competitionRepository.deleteById(id);
    }
    
    @Transactional
    public Competition updateCompetition(Competition competition) {
        return competitionRepository.save(competition);
    }
}