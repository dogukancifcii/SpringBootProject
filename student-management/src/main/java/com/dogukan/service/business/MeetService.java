package com.dogukan.service.business;

import com.dogukan.repository.business.MeetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MeetService {

    private final MeetRepository meetRepository;
}
