package com.run.runners.service;

import com.run.runners.entity.Post;
import com.run.runners.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    
    private final PostRepository postRepository;
    
    @Transactional
    public Post savePost(Post post) {
        return postRepository.save(post);
    }
    
    public List<Post> getAllPosts() {
        return postRepository.findAllOrderByCreatedAtDesc();
    }
    
    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }
    
    @Transactional
    public Optional<Post> getPostByIdAndIncrementView(Long id) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isPresent()) {
            postRepository.incrementViewCount(id);
            // 조회수 증가 후 다시 조회
            return postRepository.findById(id);
        }
        return post;
    }
    
    public List<Post> searchByTitle(String title) {
        return postRepository.findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(title);
    }
    
    public List<Post> searchByAuthor(String author) {
        return postRepository.findByAuthorContainingIgnoreCaseOrderByCreatedAtDesc(author);
    }
    
    public List<Post> searchByTitleOrContent(String keyword) {
        return postRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCaseOrderByCreatedAtDesc(keyword, keyword);
    }
    
    @Transactional
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
    
    @Transactional
    public Post updatePost(Post post) {
        return postRepository.save(post);
    }
}