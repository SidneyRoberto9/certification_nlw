package com.rocketseat.certification_nlw.modules.questions.mappers;

import com.rocketseat.certification_nlw.modules.questions.dto.AlternativesResultDTO;
import com.rocketseat.certification_nlw.modules.questions.dto.QuestionResultDTO;
import com.rocketseat.certification_nlw.modules.questions.entities.QuestionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionToDTO {
    @Autowired
    private AlternativesToDTO alternativesToDTO;

    public QuestionResultDTO execute(QuestionEntity question) {
        var questionResultDTO = QuestionResultDTO.builder()
                .id(question.getId())
                .technology(question.getTechnology())
                .description(question.getDescription())
                .build();

        List<AlternativesResultDTO> alternativesResultDTO = question.getAlternatives()
                .stream()
                .map(alternative -> alternativesToDTO.execute(alternative))
                .collect(Collectors.toList());

        questionResultDTO.setAlternatives(alternativesResultDTO);

        return questionResultDTO;
    }
}
