package com.run.runners.service;

import com.run.runners.entity.Like;
import com.run.runners.entity.Post;
import com.run.runners.repository.LikeRepository;
import com.run.runners.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeService {
    
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    
    @Transactional
    public boolean toggleLike(Long postId, String userIdentifier) {
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isEmpty()) {
            throw new IllegalArgumentException("게시글을 찾을 수 없습니다.");
        }
        
        Post post = postOptional.get();
        boolean exists = likeRepository.existsByPostAndUserIdentifier(post, userIdentifier);
        
        if (exists) {
            // 좋아요 취소
            likeRepository.deleteByPostAndUserIdentifier(post, userIdentifier);
            post.setLikeCount(post.getLikeCount() - 1);
            postRepository.save(post);
            return false;
        } else {
            // 좋아요 추가
            Like like = new Like();
            like.setPost(post);
            like.setUserIdentifier(userIdentifier);
            likeRepository.save(like);
            
            post.setLikeCount(post.getLikeCount() + 1);
            postRepository.save(post);
            return true;
        }
    }
    
    public boolean isLikedByUser(Long postId, String userIdentifier) {
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isEmpty()) {
            return false;
        }
        return likeRepository.existsByPostAndUserIdentifier(postOptional.get(), userIdentifier);
    }
    
    public long getLikeCount(Long postId) {
        return likeRepository.countByPostId(postId);
    }
}