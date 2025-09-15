package com.run.runners.service;

import com.run.runners.entity.Review;
import com.run.runners.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {
    
    private final ReviewRepository reviewRepository;
    
    public List<Review> getAllReviews() {
        return reviewRepository.findAllByOrderByCreatedAtDesc();
    }
    
    public Optional<Review> getReviewById(Long id) {
        return reviewRepository.findById(id);
    }
    
    @Transactional
    public Optional<Review> getReviewByIdAndIncrementView(Long id) {
        Optional<Review> reviewOptional = reviewRepository.findById(id);
        if (reviewOptional.isPresent()) {
            Review review = reviewOptional.get();
            review.setViewCount(review.getViewCount() + 1);
            reviewRepository.save(review);
        }
        return reviewOptional;
    }
    
    @Transactional
    public Review saveReview(Review review) {
        return reviewRepository.save(review);
    }
    
    @Transactional
    public Review updateReview(Review review) {
        return reviewRepository.save(review);
    }
    
    @Transactional
    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }
    
    public List<Review> searchByTitle(String title) {
        return reviewRepository.findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(title);
    }
    
    public List<Review> searchByAuthor(String author) {
        return reviewRepository.findByAuthorContainingIgnoreCaseOrderByCreatedAtDesc(author);
    }
    
    public List<Review> searchByTitleOrContent(String keyword) {
        return reviewRepository.findByTitleOrContentContaining(keyword);
    }
    
    public List<Review> getReviewsByPopularity() {
        return reviewRepository.findAllByOrderByViewCountDesc();
    }
    
    public List<Review> getReviewsByLikes() {
        return reviewRepository.findAllByOrderByLikeCountDesc();
    }
}