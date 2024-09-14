package com.dogukan.service.business;


import com.dogukan.repository.user.business.EducationTermRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EducationTermService {
    private final EducationTermRepository educationTermRepository;
}
