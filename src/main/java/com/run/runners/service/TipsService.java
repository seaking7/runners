package com.run.runners.service;

import com.run.runners.entity.Tips;
import com.run.runners.repository.TipsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TipsService {
    
    private final TipsRepository tipsRepository;
    
    // 모든 팁 조회 (최신순)
    public List<Tips> getAllTips() {
        return tipsRepository.findAllByOrderByCreatedAtDesc();
    }
    
    // 팁 ID로 조회
    public Optional<Tips> getTipsById(Long id) {
        return tipsRepository.findById(id);
    }
    
    // 팁 ID로 조회하고 조회수 증가
    @Transactional
    public Optional<Tips> getTipsByIdAndIncrementView(Long id) {
        Optional<Tips> tipsOptional = tipsRepository.findById(id);
        if (tipsOptional.isPresent()) {
            Tips tips = tipsOptional.get();
            tips.setViewCount(tips.getViewCount() + 1);
            tipsRepository.save(tips);
        }
        return tipsOptional;
    }
    
    // 새 팁 저장
    @Transactional
    public Tips saveTips(Tips tips) {
        return tipsRepository.save(tips);
    }
    
    // 팁 수정
    @Transactional
    public Tips updateTips(Tips tips) {
        return tipsRepository.save(tips);
    }
    
    // 팁 삭제
    @Transactional
    public void deleteTips(Long id) {
        tipsRepository.deleteById(id);
    }
    
    // 제목으로 검색
    public List<Tips> searchByTitle(String title) {
        return tipsRepository.findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(title);
    }
    
    // 작성자로 검색
    public List<Tips> searchByAuthor(String author) {
        return tipsRepository.findByAuthorContainingIgnoreCaseOrderByCreatedAtDesc(author);
    }
    
    // 제목 또는 내용으로 검색
    public List<Tips> searchByTitleOrContent(String keyword) {
        return tipsRepository.findByTitleOrContentContaining(keyword);
    }
    
    // 인기순으로 조회 (조회수 높은 순)
    public List<Tips> getTipsByPopularity() {
        return tipsRepository.findAllByOrderByViewCountDesc();
    }
    
    // 좋아요 많은 순으로 조회
    public List<Tips> getTipsByLikes() {
        return tipsRepository.findAllByOrderByLikeCountDesc();
    }
}