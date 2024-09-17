package com.dogukan.service.business;

import com.dogukan.repository.business.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LessonProgramService {


    private final LessonRepository lessonRepository;
}
