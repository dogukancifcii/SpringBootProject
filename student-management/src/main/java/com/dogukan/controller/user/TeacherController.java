package com.dogukan.controller.user;

import com.dogukan.payload.request.user.TeacherRequest;
import com.dogukan.payload.response.ResponseMessage;
import com.dogukan.payload.response.user.StudentResponse;
import com.dogukan.payload.response.user.TeacherResponse;
import com.dogukan.service.user.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @PostMapping("/save") //http://localhost:8080/teacher/save + POST + JSON
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<ResponseMessage<TeacherResponse>> saveTeacher(
            @RequestBody @Valid TeacherRequest teacherRequest
    ) {
        return ResponseEntity.ok(teacherService.saveTeacher(teacherRequest));
    }

    //bir rehber ogretmenin kendi ogrencilerinin tamamini getiren method
    @GetMapping("/getAllStudentByAdvisorUsername") //http://localhost:8080/teacher/getAllStudentByAdvisorUsername + GET
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    public List<StudentResponse> getAllStudentByAdvisorUsername(HttpServletRequest request) {
        String userName = request.getHeader("username");
        return teacherService.getAllStudentByAdvisorUsername(userName);

    }
}
