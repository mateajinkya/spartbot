package com.spartbot.spartbotbackend.repository;

import com.spartbot.spartbotbackend.model.FacultyModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacultyRepository extends MongoRepository<FacultyModel, String> {
    FacultyModel findByName(String name);
    List<FacultyModel> findByNameRegexIgnoreCase(String regex);
}
