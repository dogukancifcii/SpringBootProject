package com.dogukan.service.business;

import com.dogukan.entity.concretes.business.EducationTerm;
import com.dogukan.entity.concretes.business.Lesson;
import com.dogukan.entity.concretes.business.StudentInfo;
import com.dogukan.entity.concretes.user.User;
import com.dogukan.entity.enums.Note;
import com.dogukan.entity.enums.RoleType;
import com.dogukan.exception.ConflictException;
import com.dogukan.payload.mappers.StudentInfoMapper;
import com.dogukan.payload.messages.ErrorMessages;
import com.dogukan.payload.messages.SuccessMessages;
import com.dogukan.payload.request.business.StudentInfoRequest;
import com.dogukan.payload.response.ResponseMessage;
import com.dogukan.payload.response.business.StudentInfoResponse;
import com.dogukan.repository.business.StudentInfoRepository;
import com.dogukan.service.helper.MethodHelper;
import com.dogukan.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Service
@RequiredArgsConstructor

public class StudentInfoService {

    private final StudentInfoRepository studentInfoRepository;
    private final MethodHelper methodHelper;
    private final UserService userService;
    private final LessonService lessonService;
    private final EducationTermService educationTermService;
    private final StudentInfoMapper studentInfoMapper;


    @Value("${midterm.exam.impact.percentage}")
    private Double midtermExamPercentage;

    @Value("${final.exam.impact.percentage}")
    private Double finalExamPercentage;

    public ResponseMessage<StudentInfoResponse> saveStudentInfo(HttpServletRequest httpServletRequest, StudentInfoRequest studentInfoRequest) {
        String teacherUsername = (String) httpServletRequest.getAttribute("username");
        User student = methodHelper.isUserExist(studentInfoRequest.getStudentId());
        methodHelper.checkRole(student, RoleType.STUDENT);
        User teacher = userService.getTeacherByUsername(teacherUsername);
        Lesson lesson = lessonService.isLessonExistById(studentInfoRequest.getLessonId());
        EducationTerm educationTerm = educationTermService.getEducationTermById(studentInfoRequest.getEducationTermId());
        checkSameLesson(studentInfoRequest.getStudentId(), lesson.getLessonName());

        Note note = checkLetterGrade(calculateExamAverage(studentInfoRequest.getMidtermExam(), studentInfoRequest.getFinalExam()));

        StudentInfo studentInfo = studentInfoMapper.mapStudentInfoRequestToStudentInfo(
                studentInfoRequest,
                note,
                calculateExamAverage(studentInfoRequest.getMidtermExam(), studentInfoRequest.getFinalExam()));

        studentInfo.setStudent(student);
        studentInfo.setTeacher(teacher);
        studentInfo.setEducationTerm(educationTerm);
        studentInfo.setLesson(lesson);

        StudentInfo savedStudentInfo = studentInfoRepository.save(studentInfo);

        return ResponseMessage.<StudentInfoResponse>builder()
                .message(SuccessMessages.STUDENT_INFO_SAVE)
                .httpStatus(HttpStatus.CREATED)
                .object(studentInfoMapper.mapStudenInfoToStudentInfoRespons(savedStudentInfo))
                .build();

    }

    private void checkSameLesson(Long studentId, String lessonName) {
        boolean isLessonDuplicationExist = studentInfoRepository.getAllByStudentId_Id(studentId)
                .stream()
                .anyMatch(s -> s.getLesson().getLessonName().equalsIgnoreCase(lessonName));
        if (isLessonDuplicationExist) {
            throw new ConflictException(String.format(ErrorMessages.LESSON_ALREADY_EXIST_WITH_LESSON_NAME, lessonName));
        }
    }

    private Double calculateExamAverage(Double midtermExam, Double finalExam) {
        return ((midtermExamPercentage * midtermExam) + (finalExamPercentage * finalExam));
    }

    private Note checkLetterGrade(Double average) {
        if (average < 50.00) {
            return Note.FF;
        } else if (average < 60) {
            return Note.DD;
        } else if (average < 65) {
            return Note.CC;
        } else if (average < 70) {
            return Note.CB;
        } else if (average < 75) {
            return Note.BB;
        } else if (average < 80) {
            return Note.BA;
        } else {
            return Note.AA;
        }
    }
}
