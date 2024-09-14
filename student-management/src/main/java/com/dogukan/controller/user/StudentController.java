package com.dogukan.controller.user;

import com.dogukan.payload.request.user.StudentRequest;
import com.dogukan.payload.response.ResponseMessage;
import com.dogukan.payload.response.user.StudentResponse;
import com.dogukan.service.user.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping("/save") //http://localhost:8080/student/save + POST + JSON
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<ResponseMessage<StudentResponse>> saveStudent(@RequestBody @Valid StudentRequest studentRequest) {
        return ResponseEntity.ok(studentService.saveStudent(studentRequest));
    }

    // Not: updateStudentForStudents() ***********************************************
    // !!! ogrencinin kendisini update etme islemi
    // Not: updateStudent() **********************************************************

    //TODO: LessonProgram ekleem metodu yazilacak


    //normalde degisiklik yaptigimiz icin patch yada put yapariz fakat getlede olur ama okunabilirlik icin patch puth daha onemli
    @GetMapping("/changeStatus")//http://localhost:8080/student/changeStatus?id=1&status=true  +  GET
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGE','ASSISTANT_MANAGER')")
    public ResponseMessage changeStatusOfStudent(@RequestParam Long id, @RequestParam boolean status) {
        return studentService.changeStatusOfStudent(id, status);
    }
}
