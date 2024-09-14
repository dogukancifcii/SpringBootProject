package com.dogukan.service.user;

import com.dogukan.contactmessage.messages.Messages;
import com.dogukan.entity.concretes.user.User;
import com.dogukan.entity.enums.RoleType;
import com.dogukan.exception.ResourceNotFoundException;
import com.dogukan.payload.mappers.UserMapper;
import com.dogukan.payload.messages.ErrorMessages;
import com.dogukan.payload.messages.SuccessMessages;
import com.dogukan.payload.request.user.TeacherRequest;
import com.dogukan.payload.response.ResponseMessage;
import com.dogukan.payload.response.user.StudentResponse;
import com.dogukan.payload.response.user.TeacherResponse;
import com.dogukan.payload.response.user.UserResponse;
import com.dogukan.repository.user.UserRepository;
import com.dogukan.service.helper.MethodHelper;
import com.dogukan.service.validator.UniquePropertyValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final UserRepository userRepository;
    private final UniquePropertyValidator uniquePropertyValidator;
    private final UserMapper userMapper;
    private final UserRoleService userRoleService;
    private final PasswordEncoder passwordEncoder;
    private final MethodHelper methodHelper;

    public ResponseMessage<TeacherResponse> saveTeacher(TeacherRequest teacherRequest) {
        //TODO : LessonProgram eklenecek
        uniquePropertyValidator.checkDuplicate(
                teacherRequest.getUsername(),
                teacherRequest.getSsn(),
                teacherRequest.getPhoneNumber(),
                teacherRequest.getEmail()
        );

        User teacher = userMapper.mapTeacherRequestToUser(teacherRequest);
        teacher.setUserRole(userRoleService.getUserRole(RoleType.TEACHER));

        //TODO : Lesson program setlenecek
        teacher.setPassword(passwordEncoder.encode(teacherRequest.getPassword()));
        if (teacherRequest.getIsAdvisorTeacher()) {
            teacher.setIsAdvisor(Boolean.TRUE);
        } else teacher.setIsAdvisor(Boolean.FALSE);

        User savedTeacher = userRepository.save(teacher);

        return ResponseMessage.<TeacherResponse>builder()
                .message(SuccessMessages.TEACHER_SAVE)
                .object(userMapper.mapUserToTeacherResponse(savedTeacher))
                .build();

    }


    public List<StudentResponse> getAllStudentByAdvisorUsername(String userName) {
        User teacher = methodHelper.isUserExistByUsername(userName);
        methodHelper.checkAdvisor(teacher);

        return userRepository.findByAdvisorTeacherId(teacher.getId())
                .stream()
                .map(userMapper::mapUserToStudentResponse).collect(Collectors.toList());
    }

    //TeacherUpdate:
    //TODO: Verilen id teachera ait mi onu kontrol edilmesi gerekiyor mu???
    public ResponseMessage<TeacherResponse> updateTeacherById(TeacherRequest teacherRequest, Long teacherId) {


        //TODO:assagida tekrar method olusturdumu kontrol et...
        //dbden bu idli teacher var mi kontrolü
        User teacher = methodHelper.isUserExist(teacherId);

        //built_in kontrolü yoptik
        methodHelper.checkBuiltIn(teacher);
        //unique bilgiler degistimi kontrolü yapılıyor
        uniquePropertyValidator.checkUniqueProperties(teacher, teacherRequest);

        //DTO to POJO donusumu
        User updatedTeacher = userMapper.mapTeacherRequestToUpdatedTeacher(teacherRequest, teacherId);

        //password Encode
        updatedTeacher.setPassword(passwordEncoder.encode(teacherRequest.getPassword()));
        //burada role setleme sebebimiz dbde veriyi almadigimiz icin kendimiz yukarida donusum yaptigimiz icin eger role setlemezsek null olarak kayit eder o yuzden setlememiz gerek.Ama dbden cekmis ve updateyi ona gore yapmis olsaydik zaten dbden setlenmis rol gelirdi.
        updatedTeacher.setUserRole(teacher.getUserRole());

        if (teacherRequest.getIsAdvisorTeacher()) {
            updatedTeacher.setIsAdvisor(Boolean.TRUE);
        } else updatedTeacher.setIsAdvisor(Boolean.FALSE);

        User savedTeacher = userRepository.save(updatedTeacher);


        return ResponseMessage.<TeacherResponse>builder()
                .message(SuccessMessages.TEACHER_UPDATE)
                .httpStatus(HttpStatus.OK)
                .object(userMapper.mapUserToTeacherResponse(savedTeacher))
                .build();

    }

    public ResponseMessage<TeacherResponse> saveAdvisorTeacherByTeacherId(Long teacherId) {
        User teacher = methodHelper.isUserExist(teacherId);

        methodHelper.checkBuiltIn(teacher);

        teacher.setIsAdvisor(Boolean.TRUE);

        User savedTeacher = userRepository.save(teacher);

        return ResponseMessage.<TeacherResponse>builder()
                .message(SuccessMessages.TEACHER_ADVISOR_UPDATED)
                .httpStatus(HttpStatus.OK)
                .object(userMapper.mapUserToTeacherResponse(savedTeacher))
                .build();

    }

    public ResponseMessage<TeacherResponse> deleteAdvisorTeacherById(Long teacherId) {

        User teacher = methodHelper.isUserExist(teacherId);

        methodHelper.checkBuiltIn(teacher);

        teacher.setIsAdvisor(Boolean.FALSE);

        User savedTeacher = userRepository.save(teacher);

        return ResponseMessage.<TeacherResponse>builder()
                .message(SuccessMessages.TEACHER_ADVISOR_UPDATED)
                .httpStatus(HttpStatus.OK)
                .object(userMapper.mapUserToTeacherResponse(savedTeacher))
                .build();
    }

    // Not : getAllAdvisorTeacher() **************************************************************
    public List<UserResponse> getAllAdvisorTeacher() {
        return userRepository.findAllByAdvisor(Boolean.TRUE) // JPQL
                .stream()
                .map(userMapper::mapUserToUserResponse)
                .collect(Collectors.toList());
    }
}
