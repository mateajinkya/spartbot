package com.spartbot.spartbotbackend.controller;

import com.spartbot.spartbotbackend.model.FacultyModel;
import com.spartbot.spartbotbackend.repository.FacultyRepository;
import com.spartbot.spartbotbackend.exception.FacultyNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

@RestController
public class FacultyController {

    @Autowired
    private FacultyRepository facultyRepository;

    @GetMapping("/faculty")
    public List<FacultyModel> getAllFaculty() {
        return facultyRepository.findAll();
    }

    @GetMapping("/faculty/office_hours")
    public String getOfficeHours(@RequestParam String name) {
        FacultyModel faculty = facultyRepository.findByName(name);
        if (faculty != null) {
            return faculty.getOffice_hours();
        } else {
            return "Faculty member not found";
        }
    }

    @GetMapping("/faculty/info")
    public ResponseEntity<?> getGeneralInfo(@RequestParam String name) {
        FacultyModel faculty = facultyRepository.findByName(name);
        if (faculty != null) {
            return ResponseEntity.ok(faculty);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Faculty member not found");
        }
    }

    @GetMapping("/faculty/contact")
    public Map<String, String> getContactInfo(@RequestParam String name) {
        FacultyModel faculty = facultyRepository.findByName(name);
        if (faculty != null) {
            Map<String, String> contactInfo = new HashMap<>();
            contactInfo.put("email", faculty.getEmail());
            contactInfo.put("phone", faculty.getPhone());
            return contactInfo;
        } else {
            return Collections.singletonMap("error", "Faculty member not found");
        }
    }

    @GetMapping("/faculty/lastname")
    public ResponseEntity<?> searchByLastName(@RequestParam String lastname) {
        List<FacultyModel> facultyList = facultyRepository.findByNameRegexIgnoreCase(".*" + lastname + ".*");
        if (!facultyList.isEmpty()) {
            return ResponseEntity.ok(facultyList);
        } else {
            throw new FacultyNotFoundException("No faculty members found with last name " + lastname);
        }
    }

    @GetMapping("/faculty/{id}")
    public FacultyModel getFacultyById(@PathVariable String id) {
        return facultyRepository.findById(id).orElse(null);
    }
}
