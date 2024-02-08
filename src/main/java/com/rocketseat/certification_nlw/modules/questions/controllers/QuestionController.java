package com.rocketseat.certification_nlw.modules.questions.controllers;

import com.rocketseat.certification_nlw.modules.questions.entities.QuestionEntity;
import com.rocketseat.certification_nlw.modules.questions.mappers.AlternativesToDTO;
import com.rocketseat.certification_nlw.modules.questions.mappers.QuestionToDTO;
import com.rocketseat.certification_nlw.modules.questions.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/questions")
public class QuestionController {

    @Autowired
    private QuestionToDTO questionToDTO;
    @Autowired
    private AlternativesToDTO alternativesToDTO;
    @Autowired
    private QuestionRepository questionRepository;
    
    @GetMapping("/technology/{technology}")
    public List<QuestionEntity> findByTechnology(@PathVariable String technology) {
        return this.questionRepository.findByTechnology(technology);
    }
}
