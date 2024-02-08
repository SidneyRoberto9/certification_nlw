package com.rocketseat.certification_nlw.modules.students.useCases;


import com.rocketseat.certification_nlw.modules.questions.entities.AlternativeEntity;
import com.rocketseat.certification_nlw.modules.questions.entities.QuestionEntity;
import com.rocketseat.certification_nlw.modules.questions.repositories.QuestionRepository;
import com.rocketseat.certification_nlw.modules.students.dto.StudentCertificationAnswerDTO;
import com.rocketseat.certification_nlw.modules.students.dto.VerifyHasCertificationDTO;
import com.rocketseat.certification_nlw.modules.students.entities.AnswersCertificationEntity;
import com.rocketseat.certification_nlw.modules.students.entities.CertificationStudentEntity;
import com.rocketseat.certification_nlw.modules.students.entities.StudentEntity;
import com.rocketseat.certification_nlw.modules.students.repositories.CertificationStudentRepository;
import com.rocketseat.certification_nlw.modules.students.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class StudentCertificationAnswersUserCase {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private CertificationStudentRepository certificationStudentRepository;
    @Autowired
    private VerifyIfHasCertificationUseCase verifyIfHasCertificationUseCase;

    public CertificationStudentEntity execute(StudentCertificationAnswerDTO dto) throws Exception {

        var hasCertification = this.verifyIfHasCertificationUseCase.execute(new VerifyHasCertificationDTO(dto.getEmail(), dto.getTechnology()));

        if (hasCertification) {
            throw new Exception("Você já tirou a sua certificação!");
        }

        List<QuestionEntity> questionEntities = this.questionRepository.findByTechnology(dto.getTechnology());
        List<AnswersCertificationEntity> answersCertifications = new ArrayList<>();

        AtomicInteger correctAnswers = new AtomicInteger(0);

        dto.getQuestionsAnswers().forEach(questionAnswer -> {
            var question = questionEntities.stream().filter(q -> q.getId().equals(questionAnswer.getQuestionID()))
                    .findFirst().get();

            var findCorrectAlternative = question.getAlternatives().stream().filter(AlternativeEntity::isCorrect)
                    .findFirst().get();

            if (findCorrectAlternative.getId().equals(questionAnswer.getAlternativeID())) {
                questionAnswer.setCorrect(true);
                correctAnswers.incrementAndGet();
            } else {
                questionAnswer.setCorrect(false);
            }

            var answerCertificationEntity = AnswersCertificationEntity.builder()
                    .answerID(questionAnswer.getAlternativeID())
                    .questionID(questionAnswer.getQuestionID())
                    .isCorrect(questionAnswer.isCorrect())
                    .build();

            answersCertifications.add(answerCertificationEntity);
        });

        var student = this.studentRepository.findByEmail(dto.getEmail());

        UUID studentId = null;

        if (student.isEmpty()) {
            var studentCreated = StudentEntity.builder().email(dto.getEmail()).build();
            studentCreated = this.studentRepository.save(studentCreated);
            studentId = studentCreated.getId();
        } else {
            studentId = student.get().getId();
        }

        CertificationStudentEntity certificationStudentEntity = CertificationStudentEntity.builder()
                .answersCertificationEntities(answersCertifications)
                .studentID(studentId)
                .technology(dto.getTechnology())
                .grade(correctAnswers.get())
                .build();

        answersCertifications.forEach(answer -> {
            answer.setCertificationID(certificationStudentEntity.getId());
            answer.setCertificationStudentEntity(certificationStudentEntity);
        });

        certificationStudentEntity.setAnswersCertificationEntities(answersCertifications);

        certificationStudentRepository.save(certificationStudentEntity);

        return this.certificationStudentRepository.save(certificationStudentEntity);
    }
}
