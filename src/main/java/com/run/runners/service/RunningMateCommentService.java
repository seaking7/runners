package com.run.runners.service;

import com.run.runners.entity.RunningMateComment;
import com.run.runners.repository.RunningMateCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RunningMateCommentService {
    
    private final RunningMateCommentRepository runningMateCommentRepository;
    
    public RunningMateComment saveComment(RunningMateComment comment) {
        return runningMateCommentRepository.save(comment);
    }
    
    @Transactional(readOnly = true)
    public List<RunningMateComment> getCommentsByRunningMateId(Long runningMateId) {
        return runningMateCommentRepository.findByRunningMateIdOrderByCreatedAtAsc(runningMateId);
    }
    
    public void deleteComment(Long id) {
        runningMateCommentRepository.deleteById(id);
    }
    
    @Transactional(readOnly = true)
    public long getCommentCount(Long runningMateId) {
        return runningMateCommentRepository.countByRunningMateId(runningMateId);
    }
}