package com.run.runners.service;

import com.run.runners.entity.RunningMate;
import com.run.runners.repository.RunningMateRepository;
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
class RunningMateServiceTest {

    @Mock
    private RunningMateRepository runningMateRepository;

    @InjectMocks
    private RunningMateService runningMateService;

    private RunningMate testRunningMate;

    @BeforeEach
    void 테스트_설정() {
        testRunningMate = new RunningMate();
        testRunningMate.setId(1L);
        testRunningMate.setTitle("한강 러닝메이트 모집");
        testRunningMate.setContent("함께 달릴 러닝메이트를 찾습니다.");
        testRunningMate.setAuthor("러너김");
        testRunningMate.setLocation("한강공원");
        testRunningMate.setMaxParticipants(5);
        testRunningMate.setRunningDateTime(LocalDateTime.now().plusDays(1));
        testRunningMate.setViewCount(0);
        testRunningMate.setCreatedAt(LocalDateTime.now());
        testRunningMate.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void 러닝메이트_저장시_저장된_러닝메이트_반환() {
        when(runningMateRepository.save(any(RunningMate.class))).thenReturn(testRunningMate);

        RunningMate result = runningMateService.saveRunningMate(testRunningMate);

        assertNotNull(result);
        assertEquals(testRunningMate.getId(), result.getId());
        assertEquals(testRunningMate.getTitle(), result.getTitle());
        assertEquals(testRunningMate.getLocation(), result.getLocation());
        verify(runningMateRepository, times(1)).save(testRunningMate);
    }

    @Test
    void 모든_러닝메이트_조회시_러닝메이트_목록_반환() {
        List<RunningMate> runningMates = Arrays.asList(testRunningMate);
        when(runningMateRepository.findAllOrderByCreatedAtDesc()).thenReturn(runningMates);

        List<RunningMate> result = runningMateService.getAllRunningMates();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testRunningMate.getId(), result.get(0).getId());
        verify(runningMateRepository, times(1)).findAllOrderByCreatedAtDesc();
    }

    @Test
    void 존재하는_러닝메이트_ID로_조회시_러닝메이트_반환() {
        when(runningMateRepository.findById(1L)).thenReturn(Optional.of(testRunningMate));

        Optional<RunningMate> result = runningMateService.getRunningMateById(1L);

        assertTrue(result.isPresent());
        assertEquals(testRunningMate.getId(), result.get().getId());
        verify(runningMateRepository, times(1)).findById(1L);
    }

    @Test
    void 존재하지않는_러닝메이트_ID로_조회시_빈값_반환() {
        when(runningMateRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<RunningMate> result = runningMateService.getRunningMateById(1L);

        assertFalse(result.isPresent());
        verify(runningMateRepository, times(1)).findById(1L);
    }

    @Test
    void 존재하는_러닝메이트_조회수_증가_조회시_조회수_증가하고_러닝메이트_반환() {
        RunningMate updatedRunningMate = new RunningMate();
        updatedRunningMate.setId(1L);
        updatedRunningMate.setTitle("한강 러닝메이트 모집");
        updatedRunningMate.setViewCount(1);

        when(runningMateRepository.findById(1L))
            .thenReturn(Optional.of(testRunningMate))
            .thenReturn(Optional.of(updatedRunningMate));

        Optional<RunningMate> result = runningMateService.getRunningMateByIdAndIncrementView(1L);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getViewCount());
        verify(runningMateRepository, times(1)).incrementViewCount(1L);
        verify(runningMateRepository, times(2)).findById(1L);
    }

    @Test
    void 존재하지않는_러닝메이트_조회수_증가_조회시_빈값_반환() {
        when(runningMateRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<RunningMate> result = runningMateService.getRunningMateByIdAndIncrementView(1L);

        assertFalse(result.isPresent());
        verify(runningMateRepository, never()).incrementViewCount(anyLong());
        verify(runningMateRepository, times(1)).findById(1L);
    }

    @Test
    void 제목으로_검색시_일치하는_러닝메이트들_반환() {
        List<RunningMate> runningMates = Arrays.asList(testRunningMate);
        when(runningMateRepository.findByTitleContainingIgnoreCaseOrderByCreatedAtDesc("한강")).thenReturn(runningMates);

        List<RunningMate> result = runningMateService.searchByTitle("한강");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testRunningMate.getId(), result.get(0).getId());
        verify(runningMateRepository, times(1)).findByTitleContainingIgnoreCaseOrderByCreatedAtDesc("한강");
    }

    @Test
    void 작성자로_검색시_일치하는_러닝메이트들_반환() {
        List<RunningMate> runningMates = Arrays.asList(testRunningMate);
        when(runningMateRepository.findByAuthorContainingIgnoreCaseOrderByCreatedAtDesc("러너김")).thenReturn(runningMates);

        List<RunningMate> result = runningMateService.searchByAuthor("러너김");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testRunningMate.getId(), result.get(0).getId());
        verify(runningMateRepository, times(1)).findByAuthorContainingIgnoreCaseOrderByCreatedAtDesc("러너김");
    }

    @Test
    void 제목_또는_내용으로_검색시_일치하는_러닝메이트들_반환() {
        List<RunningMate> runningMates = Arrays.asList(testRunningMate);
        when(runningMateRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCaseOrderByCreatedAtDesc("러닝", "러닝"))
            .thenReturn(runningMates);

        List<RunningMate> result = runningMateService.searchByTitleOrContent("러닝");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testRunningMate.getId(), result.get(0).getId());
        verify(runningMateRepository, times(1))
            .findByTitleContainingIgnoreCaseOrContentContainingIgnoreCaseOrderByCreatedAtDesc("러닝", "러닝");
    }

    @Test
    void 장소로_검색시_일치하는_러닝메이트들_반환() {
        List<RunningMate> runningMates = Arrays.asList(testRunningMate);
        when(runningMateRepository.findByLocationContainingIgnoreCaseOrderByCreatedAtDesc("한강")).thenReturn(runningMates);

        List<RunningMate> result = runningMateService.searchByLocation("한강");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testRunningMate.getId(), result.get(0).getId());
        verify(runningMateRepository, times(1)).findByLocationContainingIgnoreCaseOrderByCreatedAtDesc("한강");
    }

    @Test
    void 러닝메이트_삭제시_리포지토리_삭제_호출() {
        doNothing().when(runningMateRepository).deleteById(1L);

        runningMateService.deleteRunningMate(1L);

        verify(runningMateRepository, times(1)).deleteById(1L);
    }

    @Test
    void 러닝메이트_수정시_수정된_러닝메이트_반환() {
        RunningMate updatedRunningMate = new RunningMate();
        updatedRunningMate.setId(1L);
        updatedRunningMate.setTitle("수정된 제목");
        updatedRunningMate.setLocation("수정된 장소");

        when(runningMateRepository.save(any(RunningMate.class))).thenReturn(updatedRunningMate);

        RunningMate result = runningMateService.updateRunningMate(updatedRunningMate);

        assertNotNull(result);
        assertEquals("수정된 제목", result.getTitle());
        assertEquals("수정된 장소", result.getLocation());
        verify(runningMateRepository, times(1)).save(updatedRunningMate);
    }
}