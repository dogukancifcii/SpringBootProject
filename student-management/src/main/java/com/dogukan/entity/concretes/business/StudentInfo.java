package com.dogukan.entity.concretes.business;


import com.dogukan.entity.concretes.user.User;
import com.dogukan.entity.enums.Note;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class StudentInfo { //ogrencilerin bilgilerini tutan entity

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer absentee;

    private Double midtermExam;

    private Double finalExam;

    private Double examAverage;

    private String infoNote;

    private Note letterGrade;

    @ManyToOne
    @JsonIgnore
    private User teacher;

    @ManyToOne
    @JsonIgnore
    private User student;

    @ManyToOne //her dersin kendine ozel birden fazla student infosu olabilir. ama ayni student info
    private Lesson lesson;

    @OneToOne
    //burasi one to one ve sadece burada yazdik bunun sebebi education term tarafindan sutdentInfoya erismek istemiyorum.
    private EducationTerm educationTerm;
}
