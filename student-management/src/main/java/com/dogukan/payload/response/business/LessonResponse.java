package com.dogukan.payload.response.business;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class LessonResponse {

    private Long lessonId;
    private String lessonName;
    private int creditScore;
    private boolean isCompulsory;
}
