package com.cg.repository;

import com.cg.model.UserQuiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserQuizRepository extends JpaRepository<UserQuiz, Long> {
}
