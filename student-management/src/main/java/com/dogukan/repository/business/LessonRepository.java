package com.dogukan.repository.business;

import com.dogukan.entity.concretes.business.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;



public interface LessonRepository extends JpaRepository<Lesson, Long> {

    boolean existsLessonByLessonNameEqualsIgnoreCase(String lessonName);
}
