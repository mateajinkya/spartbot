package com.spartbot.spartbotbackend.controller;

import com.spartbot.spartbotbackend.model.FacultyModel;
import com.spartbot.spartbotbackend.repository.FacultyRepository;
import com.spartbot.spartbotbackend.exception.FacultyNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
public class FacultyController {

    @Autowired
    private FacultyRepository facultyRepository;

    @GetMapping("/faculty")
    public List<FacultyModel> getAllFaculty() {
        return facultyRepository.findAll();
    }

    @GetMapping("/office_hours")
    public String getOfficeHours(@RequestParam String name) {
        FacultyModel faculty = facultyRepository.findByName(name);
        if (faculty != null) {
            return faculty.getOffice_hours();
        } else {
            throw new FacultyNotFoundException("No faculty members found with name " + name);
        }
    }

    @GetMapping("/info")
    public ResponseEntity<?> getGeneralInfo(@RequestParam String name) {
        FacultyModel faculty = facultyRepository.findByName(name);
        if (faculty == null) {
            throw new FacultyNotFoundException("No faculty members found with name " + name);
        }
        return ResponseEntity.ok(faculty);
    }

    @GetMapping("/contact")
    public Map<String, String> getContactInfo(@RequestParam String name) {
        FacultyModel faculty = facultyRepository.findByName(name);
        if (faculty == null) {
            throw new FacultyNotFoundException("No faculty members found with name " + name);
        }
        Map<String, String> contactInfo = new HashMap<>();
        contactInfo.put("email", faculty.getEmail());
        contactInfo.put("phone", faculty.getPhone());
        return contactInfo;
    }

    @GetMapping("/lastname")
    public ResponseEntity<?> searchByLastName(@RequestParam String lastname) {
        List<FacultyModel> facultyList = facultyRepository.findByNameRegexIgnoreCase(".*" + lastname + ".*");
        if (!facultyList.isEmpty()) {
            return ResponseEntity.ok(facultyList);
        } else {
            throw new FacultyNotFoundException("No faculty members found with name " + lastname);
        }
    }
}
