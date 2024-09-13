package com.dogukan.payload.response.user;


import com.dogukan.entity.concretes.business.LessonProgram;
import com.dogukan.payload.response.abstracts.BaseUserResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TeacherResponse extends BaseUserResponse {

    private Set<LessonProgram> lessonPrograms;
    private Boolean isAdvisorTeacher;
}
