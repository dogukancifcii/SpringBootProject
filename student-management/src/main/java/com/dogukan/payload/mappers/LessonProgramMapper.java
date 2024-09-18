package com.dogukan.payload.mappers;

import com.dogukan.entity.concretes.business.EducationTerm;
import com.dogukan.entity.concretes.business.Lesson;
import com.dogukan.entity.concretes.business.LessonProgram;
import com.dogukan.payload.request.business.LessonProgramRequest;
import com.dogukan.payload.response.business.LessonProgramResponse;
import com.dogukan.service.business.LessonProgramService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class LessonProgramMapper {


    public LessonProgram mapLessonProgramRequestToLessonProgram(LessonProgramRequest lessonProgramRequest, Set<Lesson> lessonSet, EducationTerm educationTerm) {
        return LessonProgram.builder()
                .startTime(lessonProgramRequest.getStartTime())
                .stopTime(lessonProgramRequest.getStopTime())
                .day(lessonProgramRequest.getDay())
                .lessons(lessonSet)
                .educationTerm(educationTerm)
                .build();
    }

    public LessonProgramResponse mapLessonProgramToLessonProgramResponse(LessonProgram lessonProgram){
        return LessonProgramResponse.builder()
                .day(lessonProgram.getDay())
                .startTime(lessonProgram.getStartTime())
                .stopTime(lessonProgram.getStopTime())
                .lessonProgramId(lessonProgram.getId())
                .lessonName(lessonProgram.getLessons())
                .build();
    }
}
