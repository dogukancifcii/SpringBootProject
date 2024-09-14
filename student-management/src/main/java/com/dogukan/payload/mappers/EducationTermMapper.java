package com.dogukan.payload.mappers;

import com.dogukan.entity.concretes.business.EducationTerm;
import com.dogukan.payload.response.business.EducationTermResponse;
import com.dogukan.service.business.EducationTermService;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class EducationTermMapper {

    public EducationTermResponse mapEducationTermToEducationTermResponse(EducationTerm educationTerm) {
        return EducationTermResponse.builder()
                .id(educationTerm.getId())
                .term(educationTerm.getTerm())
                .startDate(educationTerm.getStartDate())
                .endDate(educationTerm.getEndDate())
                .lastRegistrationDate(educationTerm.getLastRegistrationDate())
                .build();
    }
}
