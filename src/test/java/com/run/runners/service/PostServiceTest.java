package com.run.runners.service;

import com.run.runners.entity.Post;
import com.run.runners.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    private Post testPost;

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
    }

    @Test
    void 게시물_저장시_저장된_게시물_반환() {
        when(postRepository.save(any(Post.class))).thenReturn(testPost);

        Post result = postService.savePost(testPost);

        assertNotNull(result);
        assertEquals(testPost.getId(), result.getId());
        assertEquals(testPost.getTitle(), result.getTitle());
        assertEquals(testPost.getContent(), result.getContent());
        verify(postRepository, times(1)).save(testPost);
    }

    @Test
    void 모든_게시물_조회시_게시물_목록_반환() {
        List<Post> posts = Arrays.asList(testPost);
        when(postRepository.findAllOrderByCreatedAtDesc()).thenReturn(posts);

        List<Post> result = postService.getAllPosts();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testPost.getId(), result.get(0).getId());
        verify(postRepository, times(1)).findAllOrderByCreatedAtDesc();
    }

    @Test
    void 존재하는_게시물_ID로_조회시_게시물_반환() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(testPost));

        Optional<Post> result = postService.getPostById(1L);

        assertTrue(result.isPresent());
        assertEquals(testPost.getId(), result.get().getId());
        verify(postRepository, times(1)).findById(1L);
    }

    @Test
    void 존재하지않는_게시물_ID로_조회시_빈값_반환() {
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Post> result = postService.getPostById(1L);

        assertFalse(result.isPresent());
        verify(postRepository, times(1)).findById(1L);
    }

    @Test
    void 존재하는_게시물_조회수_증가_조회시_조회수_증가하고_게시물_반환() {
        Post updatedPost = new Post();
        updatedPost.setId(1L);
        updatedPost.setTitle("테스트 제목");
        updatedPost.setContent("테스트 내용");
        updatedPost.setAuthor("테스트 작성자");
        updatedPost.setViewCount(1);
        updatedPost.setLikeCount(0);

        when(postRepository.findById(1L))
            .thenReturn(Optional.of(testPost))
            .thenReturn(Optional.of(updatedPost));

        Optional<Post> result = postService.getPostByIdAndIncrementView(1L);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getViewCount());
        verify(postRepository, times(1)).incrementViewCount(1L);
        verify(postRepository, times(2)).findById(1L);
    }

    @Test
    void 존재하지않는_게시물_조회수_증가_조회시_빈값_반환() {
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Post> result = postService.getPostByIdAndIncrementView(1L);

        assertFalse(result.isPresent());
        verify(postRepository, never()).incrementViewCount(anyLong());
        verify(postRepository, times(1)).findById(1L);
    }

    @Test
    void 제목으로_검색시_일치하는_게시물들_반환() {
        List<Post> posts = Arrays.asList(testPost);
        when(postRepository.findByTitleContainingIgnoreCaseOrderByCreatedAtDesc("테스트")).thenReturn(posts);

        List<Post> result = postService.searchByTitle("테스트");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testPost.getId(), result.get(0).getId());
        verify(postRepository, times(1)).findByTitleContainingIgnoreCaseOrderByCreatedAtDesc("테스트");
    }

    @Test
    void 작성자로_검색시_일치하는_게시물들_반환() {
        List<Post> posts = Arrays.asList(testPost);
        when(postRepository.findByAuthorContainingIgnoreCaseOrderByCreatedAtDesc("작성자")).thenReturn(posts);

        List<Post> result = postService.searchByAuthor("작성자");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testPost.getId(), result.get(0).getId());
        verify(postRepository, times(1)).findByAuthorContainingIgnoreCaseOrderByCreatedAtDesc("작성자");
    }

    @Test
    void 제목_또는_내용으로_검색시_일치하는_게시물들_반환() {
        List<Post> posts = Arrays.asList(testPost);
        when(postRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCaseOrderByCreatedAtDesc("키워드", "키워드"))
            .thenReturn(posts);

        List<Post> result = postService.searchByTitleOrContent("키워드");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testPost.getId(), result.get(0).getId());
        verify(postRepository, times(1))
            .findByTitleContainingIgnoreCaseOrContentContainingIgnoreCaseOrderByCreatedAtDesc("키워드", "키워드");
    }

    @Test
    void 게시물_삭제시_리포지토리_삭제_호출() {
        doNothing().when(postRepository).deleteById(1L);

        postService.deletePost(1L);

        verify(postRepository, times(1)).deleteById(1L);
    }

    @Test
    void 게시물_수정시_수정된_게시물_반환() {
        Post updatedPost = new Post();
        updatedPost.setId(1L);
        updatedPost.setTitle("수정된 제목");
        updatedPost.setContent("수정된 내용");
        updatedPost.setAuthor("테스트 작성자");

        when(postRepository.save(any(Post.class))).thenReturn(updatedPost);

        Post result = postService.updatePost(updatedPost);

        assertNotNull(result);
        assertEquals("수정된 제목", result.getTitle());
        assertEquals("수정된 내용", result.getContent());
        verify(postRepository, times(1)).save(updatedPost);
    }
}