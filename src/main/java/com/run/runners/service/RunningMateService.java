package com.run.runners.service;

import com.run.runners.entity.RunningMate;
import com.run.runners.repository.RunningMateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RunningMateService {
    
    private final RunningMateRepository runningMateRepository;
    
    public RunningMate saveRunningMate(RunningMate runningMate) {
        return runningMateRepository.save(runningMate);
    }
    
    @Transactional(readOnly = true)
    public List<RunningMate> getAllRunningMates() {
        return runningMateRepository.findAllOrderByCreatedAtDesc();
    }
    
    @Transactional(readOnly = true)
    public Optional<RunningMate> getRunningMateById(Long id) {
        return runningMateRepository.findById(id);
    }
    
    public Optional<RunningMate> getRunningMateByIdAndIncrementView(Long id) {
        Optional<RunningMate> runningMate = runningMateRepository.findById(id);
        if (runningMate.isPresent()) {
            runningMateRepository.incrementViewCount(id);
            return runningMateRepository.findById(id);
        }
        return runningMate;
    }
    
    @Transactional(readOnly = true)
    public List<RunningMate> searchByTitle(String keyword) {
        return runningMateRepository.findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(keyword);
    }
    
    @Transactional(readOnly = true)
    public List<RunningMate> searchByAuthor(String keyword) {
        return runningMateRepository.findByAuthorContainingIgnoreCaseOrderByCreatedAtDesc(keyword);
    }
    
    @Transactional(readOnly = true)
    public List<RunningMate> searchByTitleOrContent(String keyword) {
        return runningMateRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCaseOrderByCreatedAtDesc(keyword, keyword);
    }
    
    @Transactional(readOnly = true)
    public List<RunningMate> searchByLocation(String location) {
        return runningMateRepository.findByLocationContainingIgnoreCaseOrderByCreatedAtDesc(location);
    }
    
    public void deleteRunningMate(Long id) {
        runningMateRepository.deleteById(id);
    }
    
    public RunningMate updateRunningMate(RunningMate runningMate) {
        return runningMateRepository.save(runningMate);
    }
}