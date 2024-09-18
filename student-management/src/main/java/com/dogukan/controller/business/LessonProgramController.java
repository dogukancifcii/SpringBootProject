package com.dogukan.controller.business;


import com.dogukan.payload.request.business.LessonProgramRequest;
import com.dogukan.payload.response.ResponseMessage;
import com.dogukan.payload.response.business.LessonProgramResponse;
import com.dogukan.service.business.LessonProgramService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/lessonPrograms")
@RequiredArgsConstructor
public class LessonProgramController {

    private final LessonProgramService lessonProgramService;


    @PostMapping("/save") // http://localhost:8080/lessonPrograms/save
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseMessage<LessonProgramResponse> saveLessonProgram(@RequestBody @Valid LessonProgramRequest lessonProgramRequest) {
        return lessonProgramService.saveLessonProgram(lessonProgramRequest);
    }

    @GetMapping("/getAll") //http://localhost:8080/lessonPrograms/getAll
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public List<LessonProgramResponse> getAllLessonPrograms() {
        return lessonProgramService.getAllLessonPrograms();
    }

    @GetMapping("/getById/{id}")//http://localhost:8080/lessonPrograms/getById/1
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public LessonProgramResponse getLessonProgramById(@PathVariable Long id) {
        return lessonProgramService.getLessonProgramById(id);
    }

    @GetMapping("/getAllUnassigned") //http://localhost:8080/lessonPrograms/getAllUnassigned
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public List<LessonProgramResponse> getAllUnassigned() {
        return lessonProgramService.getAllUnassigned();
    }


    @DeleteMapping("/delete/{id}")//http://localhost:8080/lessonPrograms/delete/2
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseMessage deleteById(@PathVariable Long id) {
        return lessonProgramService.deleteById(id);
    }

    @GetMapping("/getAllLessonProgramByTeacher")
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    public Set<LessonProgramResponse> getAllByTeacher(HttpServletRequest httpServletRequest){
        return lessonProgramService.getAllLessonProgramByUser(httpServletRequest)
    }
}
