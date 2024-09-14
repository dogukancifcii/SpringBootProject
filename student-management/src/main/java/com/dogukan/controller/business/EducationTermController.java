package com.dogukan.controller.business;


import com.dogukan.payload.response.business.EducationTermResponse;
import com.dogukan.service.business.EducationTermService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/educationTerm")
@RequiredArgsConstructor
public class EducationTermController {

    private final EducationTermService educationTermService;

    // Not: ODEVV save() *********************************************************

    @GetMapping("/{id}") // http://localhost:8080/educationTerm/1 + GET
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER','TEACHER')")
    public EducationTermResponse getEducationTermById(@PathVariable Long id){
        return educationTermService.getEducationTermResponseById(id);
    }
}
