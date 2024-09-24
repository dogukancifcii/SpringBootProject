package com.dogukan.service.user;

import com.dogukan.entity.concretes.business.LessonProgram;
import com.dogukan.entity.concretes.user.User;
import com.dogukan.entity.enums.RoleType;
import com.dogukan.payload.mappers.UserMapper;
import com.dogukan.payload.messages.SuccessMessages;
import com.dogukan.payload.request.business.ChooseLessonProgramWithId;
import com.dogukan.payload.request.user.StudentRequest;
import com.dogukan.payload.request.user.StudentRequestWithoutPassword;
import com.dogukan.payload.response.ResponseMessage;
import com.dogukan.payload.response.user.StudentResponse;
import com.dogukan.repository.user.UserRepository;
import com.dogukan.service.business.LessonProgramService;
import com.dogukan.service.helper.MethodHelper;
import com.dogukan.service.validator.DateTimeValidator;
import com.dogukan.service.validator.UniquePropertyValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final UserRepository userRepository;
    private final MethodHelper methodHelper;
    private final UniquePropertyValidator uniquePropertyValidator;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleService userRoleService;
    private final LessonProgramService lessonProgramService;
    private final DateTimeValidator dateTimeValidator;

    public ResponseMessage<StudentResponse> saveStudent(StudentRequest studentRequest) {

        User advisorTeacher = methodHelper.isUserExist(studentRequest.getAdvisorTeacherId());
        methodHelper.checkAdvisor(advisorTeacher);

        uniquePropertyValidator.checkDuplicate(
                studentRequest.getUsername(),
                studentRequest.getSsn(),
                studentRequest.getPhoneNumber(),
                studentRequest.getEmail()
        );
        User student = userMapper.mapStudentRequestToUser(studentRequest);
        student.setAdvisorTeacherId(advisorTeacher.getId());
        student.setPassword(passwordEncoder.encode(studentRequest.getPassword()));
        student.setUserRole(userRoleService.getUserRole(RoleType.STUDENT));
        student.setActive(true);
        student.setIsAdvisor(Boolean.FALSE);
        student.setStudentNumber(getLastNumber());

        return ResponseMessage.<StudentResponse>builder()
                .object(userMapper.mapUserToStudentResponse(userRepository.save(student)))
                .message(SuccessMessages.STUDENT_SAVE)
                .build();


    }

    private int getLastNumber() {
        //ogrenci var mi yokmu kontrolu yoksa 1000 numarasini donruduyor varsa asagisi calisiytor
        if (!userRepository.findStudent(RoleType.STUDENT)) {
            return 1000;
        }
        return userRepository.getMaxStudentNumber() + 1;
    }

    public ResponseMessage changeStatusOfStudent(Long studentId, boolean status) {
        User student = methodHelper.isUserExist(studentId);
        //role kontrol
        methodHelper.checkRole(student, RoleType.STUDENT);
        student.setActive(status);
        userRepository.save(student);

        return ResponseMessage.builder()
                .message("Student is " + (status ? "active" : "passive"))
                .httpStatus(HttpStatus.OK)
                .build();
    }

    // Not: updateStudentForStudents() **********************************************************
    public ResponseEntity<String> updateStudent(StudentRequestWithoutPassword studentRequest,
                                                HttpServletRequest request) {

        String userName = (String) request.getAttribute("username");
        User student = userRepository.findByUsername(userName);

        // !!! unique kontrolu
        uniquePropertyValidator.checkUniqueProperties(student, studentRequest);

        student.setMotherName(studentRequest.getMotherName());
        student.setFatherName(studentRequest.getFatherName());
        student.setBirthDay(studentRequest.getBirthDay());
        student.setEmail(studentRequest.getEmail());
        student.setPhoneNumber(studentRequest.getPhoneNumber());
        student.setBirthPlace(studentRequest.getBirthPlace());
        student.setGender(studentRequest.getGender());
        student.setName(studentRequest.getName());
        student.setSurname(studentRequest.getSurname());
        student.setSsn(studentRequest.getSsn());

        userRepository.save(student);

        String message = SuccessMessages.USER_UPDATE;

        return ResponseEntity.ok(message);
    }

    // Not: updateStudent() **********************************************************
    public ResponseMessage<StudentResponse> updateStudentForManagers(Long userId,
                                                                     StudentRequest studentRequest) {
        User user = methodHelper.isUserExist(userId);
        // !!! Parametrede gelen id bir student'a ait degilse exception firlatiliyor
        methodHelper.checkRole(user,RoleType.STUDENT);
        // !!! unique kontrolu
        uniquePropertyValidator.checkUniqueProperties(user, studentRequest);

        user.setName(studentRequest.getName());
        user.setSurname(studentRequest.getSurname());
        user.setBirthDay(studentRequest.getBirthDay());
        user.setBirthPlace(studentRequest.getBirthPlace());
        user.setSsn(studentRequest.getSsn());
        user.setEmail(studentRequest.getEmail());
        user.setPhoneNumber(studentRequest.getPhoneNumber());
        user.setGender(studentRequest.getGender());
        user.setMotherName(studentRequest.getMotherName());
        user.setFatherName(studentRequest.getFatherName());
        user.setPassword(passwordEncoder.encode(studentRequest.getPassword()));
        user.setAdvisorTeacherId(studentRequest.getAdvisorTeacherId());

        return ResponseMessage.<StudentResponse>builder()
                .object(userMapper.mapUserToStudentResponse(userRepository.save(user)))
                .message(SuccessMessages.STUDENT_UPDATE)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    // Not: addLessonProgramToStudentLessonsProgram() *************************
    public ResponseMessage<StudentResponse> addLessonProgramToStudent(String userName,
                                                                      ChooseLessonProgramWithId chooseLessonProgramWithId) {
        // !!! username kontrolu
        User student = methodHelper.isUserExistByUsername(userName);
        //Not: LessonProgram sonrasi eklenecek
        // !!! talep edilen lessonProgramlar getiriliyor
        Set<LessonProgram> lessonProgramSet =
                lessonProgramService.getLessonProgramById(chooseLessonProgramWithId.getLessonProgramId());
        // !!! mevcuttaki lessonProgramlar getiriliyor
        Set<LessonProgram> studentCurrentLessonProgram = student.getLessonProgramList();
        // !!! talep edilen ile mevcutta bir cakisma var mi kontrolu
        dateTimeValidator.checkLessonPrograms(studentCurrentLessonProgram, lessonProgramSet);
        studentCurrentLessonProgram.addAll(lessonProgramSet);
        //we are updating the lesson program of the student
        student.setLessonProgramList(studentCurrentLessonProgram);
        User savedStudent = userRepository.save(student);
        return ResponseMessage.<StudentResponse>builder()
                .message(SuccessMessages.LESSON_PROGRAM_ADD_TO_STUDENT)
                .object(userMapper.mapUserToStudentResponse(savedStudent))
                .httpStatus(HttpStatus.OK)
                .build();
    }
}
