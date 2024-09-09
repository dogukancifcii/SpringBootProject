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

    //bir dersin birden fazla hocasi olabilir.bir program farkli dersler icinde olabilir.O yüzden many to many.
    @ManyToMany
    @JoinTable(
            name = "lesson_program_lesson",
            joinColumns = @JoinColumn(name = "lessonprogram_id"),
            inverseJoinColumns = @JoinColumn(name = "lesson_id")
    )//3.table customize etmek icin yazdik.Normalde JoinTable yazsakta yazmasakta zaten tablo olusuyor.
    private Set<Lesson> lessons;

    @ManyToOne(cascade = CascadeType.PERSIST)
    //cascde persıst yapma sebebimiz eğer LessonProgram kaydedersem educatıonTerm repositoydeki save methoduyla oraya ekstradan kayıt etme ile uğraşmama gerek kalmıyor.Hibernate bizim yerimize bu olayı kendisi yopıyor.
    private EducationTerm educationTerm;

    @ManyToMany(mappedBy = "lessonProgramList", fetch = FetchType.EAGER)
    //many tarafinda oldugu için default olarak fetchType eager olur
    //Lesson program cekiyosak userlarda gelsin diye eagera cektik
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Set<User> users;

    @PreRemove
    private void removeLessonProgramFromUser() {
        users.forEach(user -> user.getLessonProgramList().remove(this));//buradaki this bu classdan bahsediyor.
    }
    //pre remove ile yaptigimiz bu olay LessonProgrami eger silmek istersek kayitli olan ogrencidende bu lessonprogrami sildirdik
}
