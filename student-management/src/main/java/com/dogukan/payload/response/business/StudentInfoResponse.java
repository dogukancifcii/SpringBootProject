package com.dogukan.payload.response.business;

import com.dogukan.entity.enums.Note;
import com.dogukan.entity.enums.Term;
import com.dogukan.payload.response.user.StudentResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentInfoResponse {

    private Long id;
    private Double midtermExam;
    private Double finalExam;
    private Integer absentee;
    private String infoNote;
    private String lessonName;
    private int creditScore;
    private boolean isCompulsory;
    private Term educationTerm;
    private Double average;
    private Note note;
    private StudentResponse studentResponse;
}
