package com.dogukan.service.business;

import com.dogukan.entity.concretes.business.Meet;
import com.dogukan.entity.concretes.user.User;
import com.dogukan.entity.enums.RoleType;
import com.dogukan.exception.BadRequestException;
import com.dogukan.exception.ConflictException;
import com.dogukan.exception.ResourceNotFoundException;
import com.dogukan.payload.mappers.MeetMaper;
import com.dogukan.payload.messages.ErrorMessages;
import com.dogukan.payload.messages.SuccessMessages;
import com.dogukan.payload.request.business.MeetRequest;
import com.dogukan.payload.response.ResponseMessage;
import com.dogukan.payload.response.business.MeetResponse;
import com.dogukan.repository.business.MeetRepository;
import com.dogukan.service.helper.MethodHelper;
import com.dogukan.service.user.UserService;
import com.dogukan.service.validator.DateTimeValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetService {

    private final MeetRepository meetRepository;
    private final UserService userService;
    private final MethodHelper methodHelper;
    private final DateTimeValidator dateTimeValidator;
    private final MeetMaper meetMaper;

    public ResponseMessage<MeetResponse> saveMeet(HttpServletRequest httpServletRequest, MeetRequest meetRequest) {
        String username = (String) httpServletRequest.getAttribute("username");

        User advisorTeacher = userService.getTeacherByUsername(username);
        methodHelper.checkAdvisor(advisorTeacher);
        dateTimeValidator.checkTimeWithExeption(meetRequest.getStartTime(), meetRequest.getStopTime());

        checkMeetConflict(advisorTeacher.getId(),
                meetRequest.getDate(),
                meetRequest.getStartTime(),
                meetRequest.getStopTime());

        for (Long studentId : meetRequest.getStudentIds()) {
            User student = methodHelper.isUserExist(studentId);
            methodHelper.checkRole(student, RoleType.STUDENT);

            checkMeetConflict(studentId,
                    meetRequest.getDate(),
                    meetRequest.getStartTime(),
                    meetRequest.getStopTime());
        }

        List<User> students = userService.getStudentById(meetRequest.getStudentIds());

        Meet meet = meetMaper.mapMeetRequestToMeet(meetRequest);

        meet.setStudentList(students);
        meet.setAdvisoryTeacher(advisorTeacher);

        Meet savedMeet = meetRepository.save(meet);

        return ResponseMessage.<MeetResponse>builder()
                .message(SuccessMessages.MEET_SAVE)
                .httpStatus(HttpStatus.CREATED)
                .object(meetMaper.mapMeetToMeetResponse(savedMeet))
                .build();


    }

    private void checkMeetConflict(Long userId, LocalDate date, LocalTime startTime, LocalTime stopTime) {
        List<Meet> meets;

        if (Boolean.TRUE.equals(userService.getUserByUserId(userId).getIsAdvisor())) {
            meets = meetRepository.getByAdvisoryTeacher_IdEquals(userId);
        } else meets = meetRepository.findByStudentList_IdEquals(userId);

        for (Meet meet : meets) {
            LocalTime existingStartTime = meet.getStartTime();
            LocalTime existingStopTime = meet.getStopTime();

            if (meet.getDate().equals(date) &&
                    (
                            (startTime.isAfter(existingStartTime) && startTime.isBefore(existingStopTime)) ||
                                    (stopTime.isAfter(existingStartTime) && stopTime.isBefore(existingStopTime) ||
                                            (startTime.isBefore(existingStartTime) && stopTime.isAfter(existingStopTime)) ||
                                            (startTime.equals(existingStartTime) || stopTime.equals(existingStopTime))
                                    )
                    )
            ) {
                throw new ConflictException(ErrorMessages.MEET_HOURS_CONFLICT);
            }
        }

    }

    public ResponseMessage delete(Long meetId, HttpServletRequest httpServletRequest) {
        Meet meet = isMeetExistById(meetId);
        isTeacherControl(meet, httpServletRequest);
        meetRepository.deleteById(meetId);

        return ResponseMessage.builder()
                .message(SuccessMessages.MEET_DELETE)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    private Meet isMeetExistById(Long meetId) {
        return meetRepository.findById(meetId).orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessages.MEET_NOT_FOUND_MESSAGE, meetId)));
    }

    private void isTeacherControl(Meet meet, HttpServletRequest httpServletRequest) {
        String userName = (String) httpServletRequest.getAttribute("username");

        User teacher = methodHelper.isUserExistByUsername(userName);
        if (
                (teacher.getUserRole().getRoleType().equals(RoleType.TEACHER)) && !(meet.getAdvisoryTeacher().getId().equals(teacher.getId()))
        ) {
            throw new BadRequestException(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE);
        }
    }
}
