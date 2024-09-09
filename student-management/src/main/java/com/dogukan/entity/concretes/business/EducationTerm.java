package com.dogukan.entity.concretes.business;

import com.dogukan.entity.enums.Term;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Entity

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class EducationTerm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Education term must not be empty")
    @Enumerated(EnumType.STRING)
    private Term term;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "start_date")
    @NotNull(message = "Start date must not be empty")
    private LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "end_date")
    @NotNull(message = "End date must not be empty")
    private LocalDate endDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "last_registration_date")
    @NotNull(message = "Last registration date must not be empty")
    private LocalDate lastRegistrationDate;

    @OneToMany(mappedBy = "educationTerm", cascade = CascadeType.ALL)
    //cascade amaci mesela bir educatıon term silersem lessonProgramda silinsin.
    //yani educatıon termde yaptıgım butun değişiklileri lessonProgrdamdada yansıt demiş oluyoruz.Silme çok
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    //yukaridaki kodun yazilmas sebebi ise infinite recursion olmasin diye.Yazabilme yetkisi var fakat geri clientten istek gönderince gozukmesin oluyor.
    private List<LessonProgram> lessonProgram;
}
