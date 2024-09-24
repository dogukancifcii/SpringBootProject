package com.dogukan.service.business;

import com.dogukan.entity.concretes.business.EducationTerm;
import com.dogukan.entity.concretes.business.Lesson;
import com.dogukan.entity.concretes.business.StudentInfo;
import com.dogukan.entity.concretes.user.User;
import com.dogukan.entity.enums.Note;
import com.dogukan.entity.enums.RoleType;
import com.dogukan.exception.ConflictException;
import com.dogukan.exception.ResourceNotFoundException;
import com.dogukan.payload.mappers.StudentInfoMapper;
import com.dogukan.payload.messages.ErrorMessages;
import com.dogukan.payload.messages.SuccessMessages;
import com.dogukan.payload.request.business.StudentInfoRequest;
import com.dogukan.payload.request.business.UpdateStudentInfoRequest;
import com.dogukan.payload.response.ResponseMessage;
import com.dogukan.payload.response.business.StudentInfoResponse;
import com.dogukan.repository.business.StudentInfoRepository;
import com.dogukan.service.helper.MethodHelper;
import com.dogukan.service.helper.PageableHelper;
import com.dogukan.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor

public class StudentInfoService {

    private final StudentInfoRepository studentInfoRepository;
    private final MethodHelper methodHelper;
    private final UserService userService;
    private final LessonService lessonService;
    private final EducationTermService educationTermService;
    private final StudentInfoMapper studentInfoMapper;
    private final PageableHelper pageableHelper;


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
                .object(studentInfoMapper.mapStudenInfoToStudentInfoResponse(savedStudentInfo))
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

    public ResponseMessage deleteById(Long studentInfoId) {
        isStudentInfoExistById(studentInfoId);
        //TODO: silen ogretmenin kendi dersine ait studentInfoyu mu siliyor bunun kontrilu yapabilirsin.Dikkat et admin butun student infolari silebilir.Bu yuzden if kontroluyle admin degilse ve teachera ait degilse kontrolu yapip exception firlatma yapilmali.
        studentInfoRepository.deleteById(studentInfoId);
        return ResponseMessage.builder()
                .message(SuccessMessages.STUDENT_INFO_DELETE)
                .httpStatus(HttpStatus.OK)
                .build();

    }

    public StudentInfo isStudentInfoExistById(Long id) {
        return studentInfoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessages.STUDENT_INFO_NOT_FOUND, id)));
    }

    public Page<StudentInfoResponse> getAllStudentInfoByPage(int page, int size, String sort, String type) {
        Pageable pageable = pageableHelper.getPageableWithProperties(page, size, sort, type);
        return studentInfoRepository.findAll(pageable).map(studentInfoMapper::mapStudenInfoToStudentInfoResponse);
    }

    public ResponseMessage<StudentInfoResponse> update(UpdateStudentInfoRequest studentInfoRequest, Long studentInfoId) {
        Lesson lesson = lessonService.isLessonExistById(studentInfoRequest.getLessonId());
        EducationTerm educationTerm = educationTermService.getEducationTermById(studentInfoRequest.getEducationTermId());
        StudentInfo studentInfo = isStudentInfoExistById(studentInfoId);
        Double notAverage = calculateExamAverage(studentInfoRequest.getMidtermExam(), studentInfoRequest.getFinalExam());
        Note note = checkLetterGrade(notAverage);

        StudentInfo studentInfoForUpdate = studentInfoMapper.mapUpdateStudentInfoRequestToStudentInfo(studentInfoRequest, studentInfoId, lesson, educationTerm, note, notAverage);

        studentInfoForUpdate.setStudent(studentInfo.getStudent());
        studentInfoForUpdate.setTeacher(studentInfo.getTeacher());
        StudentInfo updatedStudentInfo = studentInfoRepository.save(studentInfoForUpdate);


        return ResponseMessage.<StudentInfoResponse>builder()
                .message(SuccessMessages.STUDENT_INFO_UPDATE)
                .httpStatus(HttpStatus.OK)
                .object(studentInfoMapper.mapStudenInfoToStudentInfoResponse(updatedStudentInfo))
                .build();
    }

    public Page<StudentInfoResponse> getAllForTeacher(HttpServletRequest httpServletRequest, int page, int size) {
        Pageable pageable = pageableHelper.getPageableWithProperties(page, size);
        String username = (String) httpServletRequest.getAttribute("username");

        return studentInfoRepository
                .findByTeacherId_UsernameEquals(username, pageable)
                .map(studentInfoMapper::mapStudenInfoToStudentInfoResponse);
    }
}
