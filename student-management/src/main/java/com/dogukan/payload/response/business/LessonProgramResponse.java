package com.dogukan.payload.response.business;


import com.dogukan.entity.concretes.business.EducationTerm;
import com.dogukan.entity.concretes.business.Lesson;
import com.dogukan.entity.enums.Day;
import com.dogukan.payload.response.user.StudentResponse;
import com.dogukan.payload.response.user.TeacherResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LessonProgramResponse {

    private Long lessonProgramId;
    private Day day;
    private LocalTime startTime;
    private LocalTime stopTime;
    private Set<Lesson> lessonName;
    private EducationTerm educationTerm;
    private Set<TeacherResponse> teachers;
    private Set<StudentResponse> students;
    //TODO student ve teacher eklenecek
}
