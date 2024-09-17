package com.dogukan.service.business;

import com.dogukan.entity.concretes.business.Lesson;
import com.dogukan.exception.ConflictException;
import com.dogukan.payload.mappers.LessonMapper;
import com.dogukan.payload.messages.ErrorMessages;
import com.dogukan.payload.messages.SuccessMessages;
import com.dogukan.payload.request.business.LessonRequest;
import com.dogukan.payload.response.ResponseMessage;
import com.dogukan.payload.response.business.LessonResponse;
import com.dogukan.repository.business.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final LessonMapper lessonMapper;

    public ResponseMessage<LessonResponse> saveLesson(LessonRequest lessonRequest) {
        isLessonExistByLessonName(lessonRequest.getLessonName());

        Lesson savedLesson = lessonRepository.save(lessonMapper.mapLessonRequestToLesson(lessonRequest));
        return ResponseMessage.<LessonResponse>builder()
                .object(lessonMapper.mapLessonToLessonResponse(savedLesson))
                .message(SuccessMessages.LESSON_SAVE)
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

    private boolean isLessonExistByLessonName(String lessonName) {
        boolean lessonExist = lessonRepository.existsLessonByLessonNameEqualsIgnoreCase(lessonName);

        if (lessonExist) {
            throw new ConflictException(String.format(ErrorMessages.LESSON_ALREADY_EXIST_WITH_LESSON_NAME, lessonName));
        } else {
            return false;
        }

    }
}
