package com.cg.service.answer;

import com.cg.model.Answer;
import com.cg.repository.AnswerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;

    public List<Answer> findAllAnswer(Long id){
        return answerRepository.findByQuestionId(id);
    }

}
