package com.run.runners.service;

import com.run.runners.entity.RunningMate;
import com.run.runners.entity.RunningMateComment;
import com.run.runners.repository.RunningMateCommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RunningMateCommentServiceTest {

    @Mock
    private RunningMateCommentRepository runningMateCommentRepository;

    @InjectMocks
    private RunningMateCommentService runningMateCommentService;

    private RunningMateComment testComment;
    private RunningMate testRunningMate;

    @BeforeEach
    void 테스트_설정() {
        testRunningMate = new RunningMate();
        testRunningMate.setId(1L);
        testRunningMate.setTitle("한강 러닝메이트 모집");

        testComment = new RunningMateComment();
        testComment.setId(1L);
        testComment.setRunningMate(testRunningMate);
        testComment.setAuthor("참여자김");
        testComment.setContent("참여하고 싶습니다!");
        testComment.setPhoneNumber("010-1234-5678");
        testComment.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void 러닝메이트_댓글_저장시_저장된_댓글_반환() {
        when(runningMateCommentRepository.save(any(RunningMateComment.class))).thenReturn(testComment);

        RunningMateComment result = runningMateCommentService.saveComment(testComment);

        assertNotNull(result);
        assertEquals(testComment.getId(), result.getId());
        assertEquals(testComment.getAuthor(), result.getAuthor());
        assertEquals(testComment.getContent(), result.getContent());
        assertEquals(testComment.getPhoneNumber(), result.getPhoneNumber());
        verify(runningMateCommentRepository, times(1)).save(testComment);
    }

    @Test
    void 러닝메이트_ID로_댓글_조회시_댓글_목록_반환() {
        List<RunningMateComment> comments = Arrays.asList(testComment);
        when(runningMateCommentRepository.findByRunningMateIdOrderByCreatedAtAsc(1L)).thenReturn(comments);

        List<RunningMateComment> result = runningMateCommentService.getCommentsByRunningMateId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testComment.getId(), result.get(0).getId());
        verify(runningMateCommentRepository, times(1)).findByRunningMateIdOrderByCreatedAtAsc(1L);
    }

    @Test
    void 댓글_삭제시_리포지토리_삭제_호출() {
        doNothing().when(runningMateCommentRepository).deleteById(1L);

        runningMateCommentService.deleteComment(1L);

        verify(runningMateCommentRepository, times(1)).deleteById(1L);
    }

    @Test
    void 러닝메이트_댓글_개수_조회시_정확한_개수_반환() {
        when(runningMateCommentRepository.countByRunningMateId(1L)).thenReturn(3L);

        long result = runningMateCommentService.getCommentCount(1L);

        assertEquals(3L, result);
        verify(runningMateCommentRepository, times(1)).countByRunningMateId(1L);
    }

    @Test
    void 댓글이_없을때_개수_조회시_0_반환() {
        when(runningMateCommentRepository.countByRunningMateId(1L)).thenReturn(0L);

        long result = runningMateCommentService.getCommentCount(1L);

        assertEquals(0L, result);
        verify(runningMateCommentRepository, times(1)).countByRunningMateId(1L);
    }
}