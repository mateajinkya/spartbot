package com.spartbot.spartbotbackend.controller;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.spartbot.spartbotbackend.model.FacultyModel;
import com.spartbot.spartbotbackend.repository.FacultyRepository;

@WebMvcTest(FacultyController.class)
public class FacultyControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyRepository facultyRepository;

    @InjectMocks
    private FacultyController facultyController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllFaculty() throws Exception {
        // Mock faculty object
        // Given
        FacultyModel facultyModel = new FacultyModel("1", "Deborah Abbott", "https://www.sjsu.edu/people/deborah.abbott/index.html", "deborah.abbott@sjsu.edu", "123-456-7890", "Tuesday, Wednesday 16:45-17:30 and by appointment");

        // When
        List<FacultyModel> allFaculty = Collections.singletonList(facultyModel);
        given(facultyRepository.findAll()).willReturn(allFaculty);

        // Then
        mockMvc.perform(get("/faculty")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json("[{'id':'1','name':'Deborah Abbott','url':'https://www.sjsu.edu/people/deborah.abbott/index.html','email':'deborah.abbott@sjsu.edu','phone':'123-456-7890','office_hours':'Tuesday, Wednesday 16:45-17:30 and by appointment'}]"));
    }

    @Test
    public void testGetOfficeHours() throws Exception {
        // Given
        FacultyModel facultyModel = new FacultyModel("1", "Deborah Abbott", "https://www.sjsu.edu/people/deborah.abbott/index.html", "deborah.abbott@sjsu.edu", "123-456-7890", "Tuesday, Wednesday 16:45-17:30 and by appointment");

        // When
        given(facultyRepository.findByName(anyString())).willReturn(facultyModel);

        // Then
        mockMvc.perform(get("/office_hours")
            .param("name", "Deborah Abbott")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string("Tuesday, Wednesday 16:45-17:30 and by appointment"));
    }

    @Test
    public void testGetOfficeHoursFacultyNotFound() throws Exception {
        given(facultyRepository.findByName(anyString())).willReturn(null);
        mockMvc.perform(get("/office_hours")
            .param("name", "Nonexistent Faculty")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(content().string("No faculty members found with name Nonexistent Faculty"));
    }

    @Test
    public void testGetGeneralInfo() throws Exception {
        // Given
        FacultyModel facultyModel = new FacultyModel("1", "Jerry Gao", "https://www.sjsu.edu/people/jerry.gao/", "jerry.gao@sjsu.edu", "(XXX) XXX-XXXX", "Some office hours");

        // When
        given(facultyRepository.findByName(anyString())).willReturn(facultyModel);

        // Then
        mockMvc.perform(get("/info")
            .param("name", "Jerry Gao")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json("{'id':'1','name':'Jerry Gao','url':'https://www.sjsu.edu/people/jerry.gao/','email':'jerry.gao@sjsu.edu','phone':'(XXX) XXX-XXXX','office_hours':'Some office hours'}"));
    }

    @Test
    public void testGetGeneralInfoFacultyNotFound() throws Exception {
        given(facultyRepository.findByName(anyString())).willReturn(null);
        mockMvc.perform(get("/info")
            .param("name", "Nonexistent Faculty")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(content().string("No faculty members found with name Nonexistent Faculty"));
    }

    @Test
    public void testGetContactInfo() throws Exception {
        // Given
        FacultyModel facultyModel = new FacultyModel("1", "Jerry Gao", "https://www.sjsu.edu/people/jerry.gao/", "jerry.gao@sjsu.edu", "(123) 456-7890", "Something");

        // When
        given(facultyRepository.findByName(anyString())).willReturn(facultyModel);

        // Then
        mockMvc.perform(get("/contact")
            .param("name", "Jerry Gao")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json("{'phone':'(123) 456-7890','email':'jerry.gao@sjsu.edu'}"));
    }

    @Test
    public void testGetContactInfoFacultyNotFound() throws Exception {
        given(facultyRepository.findByName(anyString())).willReturn(null);
        mockMvc.perform(get("/contact")
            .param("name", "Nonexistent Faculty")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(content().string("No faculty members found with name Nonexistent Faculty"));
    }

    @Test
    public void testSearchByLastName() throws Exception {
        // Given
        FacultyModel facultyModel = new FacultyModel("1", "Deborah Abbott", "https://www.sjsu.edu/people/deborah.abbott/index.html", "deborah.abbott@sjsu.edu", "123-456-7890", "Tuesday, Wednesday 16:45-17:30 and by appointment");

        // When
        List<FacultyModel> facultyList = Collections.singletonList(facultyModel);
        given(facultyRepository.findByNameRegexIgnoreCase(anyString())).willReturn(facultyList);

        // Then
        mockMvc.perform(get("/lastname")
            .param("lastname", "Abbott")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json("[{'id':'1','name':'Deborah Abbott','url':'https://www.sjsu.edu/people/deborah.abbott/index.html','email':'deborah.abbott@sjsu.edu','phone':'123-456-7890','office_hours':'Tuesday, Wednesday 16:45-17:30 and by appointment'}]"));
    }

    @Test
    public void testSearchByLastNameFacultyNotFound() throws Exception {
        given(facultyRepository.findByNameRegexIgnoreCase(anyString())).willReturn(Collections.emptyList());
        mockMvc.perform(get("/lastname")
            .param("lastname", "Nonexistent")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(content().string("No faculty members found with name Nonexistent"));
    }
}
