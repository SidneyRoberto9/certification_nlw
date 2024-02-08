package com.rocketseat.certification_nlw.modules.questions.mappers;

import com.rocketseat.certification_nlw.modules.questions.dto.AlternativesResultDTO;
import com.rocketseat.certification_nlw.modules.questions.entities.AlternativeEntity;
import org.springframework.stereotype.Service;

@Service
public class AlternativesToDTO {
    public AlternativesResultDTO execute(AlternativeEntity alternative) {
        return AlternativesResultDTO.builder()
                .id(alternative.getId())
                .description(alternative.getDescription())
                .build();
    }
}
