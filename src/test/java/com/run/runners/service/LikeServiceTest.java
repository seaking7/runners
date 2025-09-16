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
    void 테스트_설정() {
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
    void 존재하지않는_게시물에_좋아요_토글시_예외발생() {
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            likeService.toggleLike(1L, userIdentifier);
        });

        verify(postRepository, times(1)).findById(1L);
        verify(likeRepository, never()).existsByPostAndUserIdentifier(any(), any());
    }

    @Test
    void 좋아요가_없을때_토글하면_좋아요_추가() {
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
    void 좋아요가_있을때_토글하면_좋아요_제거() {
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
    void 존재하지않는_게시물의_좋아요_확인시_false_반환() {
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        boolean result = likeService.isLikedByUser(1L, userIdentifier);

        assertFalse(result);
        verify(postRepository, times(1)).findById(1L);
        verify(likeRepository, never()).existsByPostAndUserIdentifier(any(), any());
    }

    @Test
    void 좋아요한_게시물의_좋아요_확인시_true_반환() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(testPost));
        when(likeRepository.existsByPostAndUserIdentifier(testPost, userIdentifier)).thenReturn(true);

        boolean result = likeService.isLikedByUser(1L, userIdentifier);

        assertTrue(result);
        verify(postRepository, times(1)).findById(1L);
        verify(likeRepository, times(1)).existsByPostAndUserIdentifier(testPost, userIdentifier);
    }

    @Test
    void 좋아요하지않은_게시물의_좋아요_확인시_false_반환() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(testPost));
        when(likeRepository.existsByPostAndUserIdentifier(testPost, userIdentifier)).thenReturn(false);

        boolean result = likeService.isLikedByUser(1L, userIdentifier);

        assertFalse(result);
        verify(postRepository, times(1)).findById(1L);
        verify(likeRepository, times(1)).existsByPostAndUserIdentifier(testPost, userIdentifier);
    }

    @Test
    void 좋아요_개수_조회시_정확한_개수_반환() {
        when(likeRepository.countByPostId(1L)).thenReturn(5L);

        long result = likeService.getLikeCount(1L);

        assertEquals(5L, result);
        verify(likeRepository, times(1)).countByPostId(1L);
    }

    @Test
    void 좋아요가_없을때_개수_조회시_0_반환() {
        when(likeRepository.countByPostId(1L)).thenReturn(0L);

        long result = likeService.getLikeCount(1L);

        assertEquals(0L, result);
        verify(likeRepository, times(1)).countByPostId(1L);
    }
}