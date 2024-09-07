package com.dogukan.entity.concretes.business;

import com.dogukan.entity.concretes.user.User;
import com.dogukan.entity.enums.Day;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class LessonProgram {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Day day;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "US")
    private LocalTime startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "US")
    private LocalTime stopTime;

    @ManyToMany
    @JoinTable(
            name = "lesson_program_lesson",
            joinColumns = @JoinColumn(name = "lessonprogram_id"),
            inverseJoinColumns = @JoinColumn(name = "lesson_id")
    )
    private Set<Lesson> lessons;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private EducationTerm educationTerm;

    @ManyToMany(mappedBy = "lessonProgramList", fetch = FetchType.EAGER)
//Lesson program cekiyosak userlarda gelsin diye eagera cektik
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Set<User> users;

    @PreRemove
    private void removeLessonProgramFromUser() {
        users.forEach(user -> user.getLessonProgramList().remove(this));
    }
    //pre remove ile yaptigimiz bu olay LessonProgrami eger silmek istersek kayitli olan ogrencidende bu lessonprogrami sildirdik
}
