package com.dogukan.payload.response.business;


import com.dogukan.entity.enums.Term;
import lombok.*;

import java.time.LocalDate;


//@Value immuttable nesne olusturmak icin yani getter vardir ama setter yoktur bunda
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class EducationTermResponse {

    private Long id;
    private Term term;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate lastRegistrationDate;
}
