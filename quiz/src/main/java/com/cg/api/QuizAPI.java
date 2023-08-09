package com.cg.api;

import com.cg.model.Quiz;
import com.cg.service.quiz.QuizService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/quiz")
@AllArgsConstructor
public class QuizAPI {

    private final QuizService quizService;

    @GetMapping
    public List<Quiz> getAllQuiz(){
        return quizService.findAllQuiz();
    }
}
