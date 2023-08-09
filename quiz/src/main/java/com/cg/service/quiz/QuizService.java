package com.cg.service.quiz;

import com.cg.model.Quiz;
import com.cg.repository.QuizRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class QuizService {
    private final QuizRepository quizRepository;

    public List<Quiz> findAllQuiz(){
        return quizRepository.findAll();
    }

}
