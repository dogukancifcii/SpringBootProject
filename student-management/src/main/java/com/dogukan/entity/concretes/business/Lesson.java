package com.dogukan.entity.concretes.business;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lessonId;

    private String lessonName;

    private Integer creditScore;

    private Boolean isCompulsory; //zorunlu bir ders mi?

    @JsonIgnore //bunu sonsuz donguye girmesin diye yazdik.
    @ManyToMany(mappedBy = "lessons", cascade = CascadeType.REMOVE)
    //cascadeType yazma sebebimiz bir lesson siliniyorsa o lessonun lesson programida silinsin diyoruz.
    private Set<LessonProgram> lessonPrograms;
}
