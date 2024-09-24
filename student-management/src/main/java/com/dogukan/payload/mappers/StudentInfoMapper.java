package com.dogukan.payload.mappers;

import com.dogukan.entity.concretes.business.EducationTerm;
import com.dogukan.entity.concretes.business.Lesson;
import com.dogukan.entity.concretes.business.StudentInfo;
import com.dogukan.entity.enums.Note;
import com.dogukan.payload.request.business.StudentInfoRequest;
import com.dogukan.payload.request.business.UpdateStudentInfoRequest;
import com.dogukan.payload.response.business.StudentInfoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StudentInfoMapper {

    @Autowired
    private UserMapper studentMapper;


    public StudentInfo mapStudentInfoRequestToStudentInfo(
            StudentInfoRequest studentInfoRequest,
            Note note,
            Double average) {
        return StudentInfo.builder()
                .infoNote(studentInfoRequest.getInfoNote())
                .absentee(studentInfoRequest.getAbsentee())
                .midtermExam(studentInfoRequest.getMidtermExam())
                .finalExam(studentInfoRequest.getFinalExam())
                .examAverage(average)
                .letterGrade(note)
                .build();
    }

    public StudentInfoResponse mapStudenInfoToStudentInfoRespons(
            StudentInfo studentInfo
    ) {
        return StudentInfoResponse.builder()
                .lessonName(studentInfo.getLesson().getLessonName())
                .creditScore(studentInfo.getLesson().getCreditScore())
                .isCompulsory(studentInfo.getLesson().getIsCompulsory())
                .educationTerm(studentInfo.getEducationTerm().getTerm())
                .id(studentInfo.getId())
                .absentee(studentInfo.getAbsentee())
                .midtermExam(studentInfo.getMidtermExam())
                .finalExam(studentInfo.getFinalExam())
                .infoNote(studentInfo.getInfoNote())
                .note(studentInfo.getLetterGrade())
                .average(studentInfo.getExamAverage())
                .studentResponse(studentMapper.mapUserToStudentResponse(studentInfo.getStudent()))
                .build();
    }

    public StudentInfo mapUpdateStudentInfoRequestToStudentInfo(UpdateStudentInfoRequest studentInfoRequest, Long studentInfoRequestId, Lesson lesson, EducationTerm educationTerm, Note note, Double average) {
        return StudentInfo.builder()
                .id(studentInfoRequestId)
                .infoNote(studentInfoRequest.getInfoNote())
                .midtermExam(studentInfoRequest.getMidtermExam())
                .finalExam(studentInfoRequest.getFinalExam())
                .absentee(studentInfoRequest.getAbsentee())
                .lesson(lesson)
                .educationTerm(educationTerm)
                .examAverage(average)
                .letterGrade(note)
                .build();

    }
}
