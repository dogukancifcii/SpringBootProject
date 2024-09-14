package com.dogukan.service.business;


import com.dogukan.entity.concretes.business.EducationTerm;
import com.dogukan.exception.ResourceNotFoundException;
import com.dogukan.payload.mappers.EducationTermMapper;
import com.dogukan.payload.messages.ErrorMessages;
import com.dogukan.payload.response.business.EducationTermResponse;
import com.dogukan.repository.user.business.EducationTermRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EducationTermService {
    private final EducationTermRepository educationTermRepository;
    private final EducationTermMapper educationTermMapper;

    public EducationTermResponse getEducationTermResponseById(Long id) {
        EducationTerm educationTerm = isEducationTermExist(id);
        return educationTermMapper.mapEducationTermToEducationTermResponse(educationTerm);
    }

    private EducationTerm isEducationTermExist(Long id) {
        return educationTermRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format(ErrorMessages.EDUCATION_TERM_NOT_FOUND_MESSAGE, id))
        );
    }
}
