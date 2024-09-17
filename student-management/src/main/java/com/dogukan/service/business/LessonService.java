package com.dogukan.service.business;

import com.dogukan.entity.concretes.business.Lesson;
import com.dogukan.exception.ConflictException;
import com.dogukan.exception.ResourceNotFoundException;
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

    public ResponseMessage deleteLesson(Long id) {

        isLessonExistById(id);
        lessonRepository.deleteById(id);
        return ResponseMessage.builder()
                .message(SuccessMessages.LESSON_DELETE)
                .httpStatus(HttpStatus.OK)
                .build();

    }

    private Lesson isLessonExistById(Long id) {
        return lessonRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_LESSON_MESSAGE, id)));
    }

    public ResponseMessage<LessonResponse> getLessonByLessonName(String lessonName) {
        if (lessonRepository.getLessonByLessonName(lessonName).isPresent()) {
            return ResponseMessage.<LessonResponse>builder()
                    .message(SuccessMessages.LESSON_FOUND)
                    .object(lessonMapper.mapLessonToLessonResponse(lessonRepository.getLessonByLessonName(lessonName).get()))
                    .build();
        } else {
            return ResponseMessage.<LessonResponse>builder()
                    .message(String.format(ErrorMessages.NOT_FOUND_LESSON_MESSAGE, lessonName))
                    .build();
        }
    }
}
