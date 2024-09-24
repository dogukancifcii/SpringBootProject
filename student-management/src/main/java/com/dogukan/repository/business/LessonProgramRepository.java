package com.dogukan.repository.business;

import com.dogukan.entity.concretes.business.LessonProgram;
import com.dogukan.payload.response.business.LessonProgramResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public interface LessonProgramRepository extends JpaRepository<LessonProgram, Long> {
    List<LessonProgram> findByUsers_IdNull();

    @Query("select l from LessonProgram l inner join l.users users where users.username =?1")
    Set<LessonProgram> getLessonProgramByUsersUsername(String userName);

    List<LessonProgram> findByUsers_IdNotNull();

    //SQL -> SELECT * FROM lesson_program  WHERE lesson_program.id IN (2,3);
    @Query("SELECT l FROM LessonProgram  l WHERE l.id IN :myProperty")
    Set<LessonProgram> getLessonProgramByLessonProgramIdList(Set<Long> myProperty);
}
