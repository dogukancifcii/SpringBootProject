package com.dogukan.repository.business;

import com.dogukan.entity.concretes.business.LessonProgram;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonProgramRepository extends JpaRepository<LessonProgram, Long> {
    List<LessonProgram> findByUsers_IdNull();
}
