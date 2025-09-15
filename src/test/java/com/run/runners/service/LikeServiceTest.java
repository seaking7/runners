package com.run.runners.service;

import com.run.runners.entity.Like;
import com.run.runners.entity.Post;
import com.run.runners.repository.LikeRepository;
import com.run.runners.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LikeServiceTest {

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private LikeService likeService;

    private Post testPost;
    private Like testLike;
    private String userIdentifier;

    @BeforeEach
    void setUp() {
        testPost = new Post();
        testPost.setId(1L);
        testPost.setTitle("테스트 제목");
        testPost.setContent("테스트 내용");
        testPost.setAuthor("테스트 작성자");
        testPost.setViewCount(0);
        testPost.setLikeCount(0);
        testPost.setCreatedAt(LocalDateTime.now());
        testPost.setUpdatedAt(LocalDateTime.now());

        userIdentifier = "192.168.1.1";

        testLike = new Like();
        testLike.setId(1L);
        testLike.setPost(testPost);
        testLike.setUserIdentifier(userIdentifier);
        testLike.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void toggleLike_WhenPostNotExists_ShouldThrowException() {
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            likeService.toggleLike(1L, userIdentifier);
        });

        verify(postRepository, times(1)).findById(1L);
        verify(likeRepository, never()).existsByPostAndUserIdentifier(any(), any());
    }

    @Test
    void toggleLike_WhenLikeNotExists_ShouldAddLike() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(testPost));
        when(likeRepository.existsByPostAndUserIdentifier(testPost, userIdentifier)).thenReturn(false);
        when(likeRepository.save(any(Like.class))).thenReturn(testLike);

        boolean result = likeService.toggleLike(1L, userIdentifier);

        assertTrue(result);
        assertEquals(1, testPost.getLikeCount());
        verify(postRepository, times(1)).findById(1L);
        verify(likeRepository, times(1)).existsByPostAndUserIdentifier(testPost, userIdentifier);
        verify(likeRepository, times(1)).save(any(Like.class));
        verify(postRepository, times(1)).save(testPost);
    }

    @Test
    void toggleLike_WhenLikeExists_ShouldRemoveLike() {
        testPost.setLikeCount(1);
        when(postRepository.findById(1L)).thenReturn(Optional.of(testPost));
        when(likeRepository.existsByPostAndUserIdentifier(testPost, userIdentifier)).thenReturn(true);

        boolean result = likeService.toggleLike(1L, userIdentifier);

        assertFalse(result);
        assertEquals(0, testPost.getLikeCount());
        verify(postRepository, times(1)).findById(1L);
        verify(likeRepository, times(1)).existsByPostAndUserIdentifier(testPost, userIdentifier);
        verify(likeRepository, times(1)).deleteByPostAndUserIdentifier(testPost, userIdentifier);
        verify(postRepository, times(1)).save(testPost);
    }

    @Test
    void isLikedByUser_WhenPostNotExists_ShouldReturnFalse() {
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        boolean result = likeService.isLikedByUser(1L, userIdentifier);

        assertFalse(result);
        verify(postRepository, times(1)).findById(1L);
        verify(likeRepository, never()).existsByPostAndUserIdentifier(any(), any());
    }

    @Test
    void isLikedByUser_WhenPostExistsAndLiked_ShouldReturnTrue() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(testPost));
        when(likeRepository.existsByPostAndUserIdentifier(testPost, userIdentifier)).thenReturn(true);

        boolean result = likeService.isLikedByUser(1L, userIdentifier);

        assertTrue(result);
        verify(postRepository, times(1)).findById(1L);
        verify(likeRepository, times(1)).existsByPostAndUserIdentifier(testPost, userIdentifier);
    }

    @Test
    void isLikedByUser_WhenPostExistsAndNotLiked_ShouldReturnFalse() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(testPost));
        when(likeRepository.existsByPostAndUserIdentifier(testPost, userIdentifier)).thenReturn(false);

        boolean result = likeService.isLikedByUser(1L, userIdentifier);

        assertFalse(result);
        verify(postRepository, times(1)).findById(1L);
        verify(likeRepository, times(1)).existsByPostAndUserIdentifier(testPost, userIdentifier);
    }

    @Test
    void getLikeCount_ShouldReturnCorrectCount() {
        when(likeRepository.countByPostId(1L)).thenReturn(5L);

        long result = likeService.getLikeCount(1L);

        assertEquals(5L, result);
        verify(likeRepository, times(1)).countByPostId(1L);
    }

    @Test
    void getLikeCount_WhenNoLikes_ShouldReturnZero() {
        when(likeRepository.countByPostId(1L)).thenReturn(0L);

        long result = likeService.getLikeCount(1L);

        assertEquals(0L, result);
        verify(likeRepository, times(1)).countByPostId(1L);
    }
}