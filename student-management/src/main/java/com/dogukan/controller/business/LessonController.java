package com.dogukan.controller.business;


import com.dogukan.payload.request.business.LessonRequest;
import com.dogukan.payload.response.ResponseMessage;
import com.dogukan.payload.response.business.LessonResponse;
import com.dogukan.service.business.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;

    @PostMapping("/save") // http://localhost:8080/lessons/save + POST + JSON
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    private ResponseMessage<LessonResponse> saveLesson(@RequestBody @Valid LessonRequest lessonRequest) {
        return lessonService.saveLesson(lessonRequest);
    }

    @DeleteMapping("/delete/{id}")// http://localhost:8080/lessons/delete/2 + DELETE
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseMessage deleteLesson(@PathVariable Long id) {
        return lessonService.deleteLesson(id);
    }

    @GetMapping("/getLessonByName") //http://localhost:8080/getLessonByName?lessonName=java
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseMessage<LessonResponse> getLessonByLessonName(@RequestParam String lessonName) {
        return lessonService.getLessonByLessonName(lessonName);
    }
}
