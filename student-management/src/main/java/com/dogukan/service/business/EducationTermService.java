package com.dogukan.service.business;


import com.dogukan.entity.concretes.business.EducationTerm;
import com.dogukan.exception.ResourceNotFoundException;
import com.dogukan.payload.mappers.EducationTermMapper;
import com.dogukan.payload.messages.ErrorMessages;
import com.dogukan.payload.response.business.EducationTermResponse;
import com.dogukan.repository.user.business.EducationTermRepository;
import com.dogukan.service.helper.PageableHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EducationTermService {
    private final EducationTermRepository educationTermRepository;
    private final EducationTermMapper educationTermMapper;
    private final PageableHelper pageableHelper;

    public EducationTermResponse getEducationTermResponseById(Long id) {
        EducationTerm educationTerm = isEducationTermExist(id);
        return educationTermMapper.mapEducationTermToEducationTermResponse(educationTerm);
    }

    private EducationTerm isEducationTermExist(Long id) {
        return educationTermRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format(ErrorMessages.EDUCATION_TERM_NOT_FOUND_MESSAGE, id))
        );
    }

    public List<EducationTermResponse> getAllEducationTerms() {
        return educationTermRepository.findAll().stream()
                .map(educationTermMapper::mapEducationTermToEducationTermResponse)
                .collect(Collectors.toList());

    }

    public Page<EducationTermResponse> getAllEducationTermsByPage(int page, int size, String sort, String type) {
        Pageable pageable = pageableHelper.getPageableWithProperties(page, size, sort, type);


        return educationTermRepository.findAll(pageable)
                .map(educationTermMapper::mapEducationTermToEducationTermResponse);

    }
}
