package com.dogukan.service.user;

import com.dogukan.entity.concretes.user.User;
import com.dogukan.entity.enums.RoleType;
import com.dogukan.payload.mappers.UserMapper;
import com.dogukan.payload.messages.SuccessMessages;
import com.dogukan.payload.request.user.TeacherRequest;
import com.dogukan.payload.response.ResponseMessage;
import com.dogukan.payload.response.user.TeacherResponse;
import com.dogukan.repository.user.UserRepository;
import com.dogukan.service.validator.UniquePropertyValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final UserRepository userRepository;
    private final UniquePropertyValidator uniquePropertyValidator;
    private final UserMapper userMapper;
    private final UserRoleService userRoleService;
    private final PasswordEncoder passwordEncoder;

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


}
