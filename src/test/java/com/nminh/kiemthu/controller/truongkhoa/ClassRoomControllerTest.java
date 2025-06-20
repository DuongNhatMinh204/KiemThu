package com.nminh.kiemthu.controller.truongkhoa;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nminh.kiemthu.model.request.ClassRoomCreateDTO;
import com.nminh.kiemthu.repository.ClassRoomRepository;
import com.nminh.kiemthu.service.ClassroomService;
import com.nminh.kiemthu.entity.ClassRoom;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.nminh.kiemthu.repository.SemesterRepository;
import com.nminh.kiemthu.repository.SubjectRepository;
import com.nminh.kiemthu.entity.Semester;
import com.nminh.kiemthu.entity.Subject;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ClassRoomControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SemesterRepository semesterRepository;
    @MockBean
    private SubjectRepository subjectRepository;
    @MockBean
    private ClassRoomRepository classRoomRepository;

    @Test
    void testCreateClassRoom_Success() throws Exception {
        // Arrange: tạo dữ liệu mẫu
        ClassRoomCreateDTO dto = new ClassRoomCreateDTO();
        dto.setNumberOfClasses(2);
        dto.setSemesterId(1L);
        dto.setSubjectId(4L);
        dto.setNumberOfStudents(55);

        // Mock dữ liệu repository
        Semester semester = new Semester();
        semester.setId(1L);
        Subject subject = new Subject();
        subject.setId(4L);
        Mockito.when(semesterRepository.findById(1L)).thenReturn(Optional.of(semester));
        Mockito.when(subjectRepository.findById(4L)).thenReturn(Optional.of(subject));
        Mockito.when(classRoomRepository.save(any(ClassRoom.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act & Assert
        mockMvc.perform(post("/admin/classroom/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andDo(result -> {
                    System.out.println("Status: " + result.getResponse().getStatus());
                    System.out.println("Message: " + result.getResponse().getContentAsString());
                });
    }

    @Test
    void testCreateClassRoom_NumberOfClassesLessThanZero() throws Exception {
        ClassRoomCreateDTO dto = new ClassRoomCreateDTO();
        dto.setNumberOfClasses(-1); // Số lượng lớp nhỏ hơn 0
        dto.setSemesterId(1L);
        dto.setSubjectId(4L);
        dto.setNumberOfStudents(55);

        // Mock dữ liệu repository để không lỗi semester/subject
        Semester semester = new Semester();
        semester.setId(1L);
        Subject subject = new Subject();
        subject.setId(4L);
        Mockito.when(semesterRepository.findById(1L)).thenReturn(Optional.of(semester));
        Mockito.when(subjectRepository.findById(4L)).thenReturn(Optional.of(subject));

        mockMvc.perform(post("/admin/classroom/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("classes must be > 0"))
                .andDo(result -> {
                    System.out.println("Status: " + result.getResponse().getStatus());
                    System.out.println("Message: " + result.getResponse().getContentAsString());
                });
    }
}
