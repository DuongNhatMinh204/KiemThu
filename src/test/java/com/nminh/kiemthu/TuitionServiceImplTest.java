package com.nminh.kiemthu;

import com.nminh.kiemthu.entity.Tuition;
import com.nminh.kiemthu.enums.ErrorCode;
import com.nminh.kiemthu.exception.AppException;
import com.nminh.kiemthu.repository.TuitionRepository;
import com.nminh.kiemthu.service.TutionService;
import com.nminh.kiemthu.service.impl.TuitionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TuitionServiceImplTest {

    @Mock
    private TuitionRepository tuitionRepository;

    @InjectMocks
    private TuitionServiceImpl tuitionService;

    private Tuition tuition;

    @BeforeEach
    void setUp() {
        tuition = new Tuition();
        tuition.setId(1L);
        tuition.setMoney(1000L);
        tuition.setPre_money(500L);
    }

    @Test
    void createTuition_success() {
        when(tuitionRepository.save(any(Tuition.class))).thenReturn(tuition);

        Tuition result = tuitionService.createTuition(tuition);

        assertNotNull(result);
        assertEquals(tuition.getId(), result.getId());
        assertEquals(tuition.getMoney(), result.getMoney());
        verify(tuitionRepository, times(1)).save(tuition);
    }

    @Test
    void updateTuition_success() {
        Long newMoney = 2000L;
        when(tuitionRepository.findById(1L)).thenReturn(Optional.of(tuition));
        when(tuitionRepository.save(any(Tuition.class))).thenReturn(tuition);

        Tuition result = tuitionService.updateTuition(1L, newMoney);

        assertNotNull(result);
        assertEquals(newMoney, result.getMoney());
        assertEquals(1000L, result.getPre_money());
        verify(tuitionRepository, times(1)).findById(1L);
        verify(tuitionRepository, times(1)).save(tuition);
    }

    @Test
    void updateTuition_negativeMoney_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            tuitionService.updateTuition(1L, -100L);
        });
        verify(tuitionRepository, never()).save(any(Tuition.class));
    }

    @Test
    void updateTuition_notFound_throwsAppException() {
        when(tuitionRepository.findById(1L)).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> {
            tuitionService.updateTuition(1L, 2000L);
        });

        assertEquals(ErrorCode.TUITION_NOT_FOUND, exception.getErrorCode());
        verify(tuitionRepository, times(1)).findById(1L);
        verify(tuitionRepository, never()).save(any(Tuition.class));
    }

    @Test
    void getAll_success() {
        Tuition tuition2 = new Tuition();
        tuition2.setId(2L);
        tuition2.setMoney(1500L);
        List<Tuition> tuitionList = Arrays.asList(tuition, tuition2);
        when(tuitionRepository.findAll()).thenReturn(tuitionList);

        List<Tuition> result = tuitionService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(tuitionList, result);
        verify(tuitionRepository, times(1)).findAll();
    }
}