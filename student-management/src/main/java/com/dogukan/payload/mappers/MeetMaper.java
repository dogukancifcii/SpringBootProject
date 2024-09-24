package com.dogukan.payload.mappers;

import com.dogukan.entity.concretes.business.Meet;
import com.dogukan.payload.request.business.MeetRequest;
import com.dogukan.payload.response.business.MeetResponse;
import org.springframework.stereotype.Component;

@Component
public class MeetMaper {

    public Meet mapMeetRequestToMeet(MeetRequest meetRequest) {
        return Meet.builder()
                .date(meetRequest.getDate())
                .startTime(meetRequest.getStartTime())
                .stopTime(meetRequest.getStopTime())
                .description(meetRequest.getDescription())
                .build();
    }

    public Meet mapMeetUpdateRequestToMeet(MeetRequest meetRequest, Long meetId) {
        return Meet.builder()
                .id(meetId)
                .stopTime(meetRequest.getStopTime())
                .startTime(meetRequest.getStartTime())
                .description(meetRequest.getDescription())
                .date(meetRequest.getDate())
                .build();
    }

    public MeetResponse mapMeetToMeetResponse(Meet meet) {
        return MeetResponse.builder()
                .id(meet.getId())
                .date(meet.getDate())
                .startTime(meet.getStartTime())
                .stopTime(meet.getStopTime())
                .description(meet.getDescription())
                .advisorTeacherId(meet.getAdvisoryTeacher().getId())
                .teacherSsn(meet.getAdvisoryTeacher().getSsn())
                .teacherName(meet.getAdvisoryTeacher().getName())
                .students(meet.getStudentList())
                .build();
    }
}
