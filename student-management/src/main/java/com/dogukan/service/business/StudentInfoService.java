package com.dogukan.service.business;

import com.dogukan.repository.business.StudentInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class StudentInfoService {

    private final StudentInfoRepository studentInfoRepository;
}
