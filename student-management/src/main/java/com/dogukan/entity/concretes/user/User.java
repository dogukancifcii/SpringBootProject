package com.dogukan.entity.concretes.user;

import com.dogukan.entity.concretes.business.StudentInfo;
import com.dogukan.entity.enums.Gender;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
//toBuilder = true parametresi, mevcut bir nesnenin bir kopyasını alarak, bu kopya üzerinde değişiklikler yapmanıza olanak tanır.


@Entity
@Table(name = "t_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username; //userName yazarsak repositoryde boyle yazmaliyiz

    @Column(unique = true)
    private String ssn; //tcno

    private String name;

    private String surname;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthDay;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) //kaydet ve client kismina geri dondurme demek
    //@JsonIgnore()  buda yukaridaki ile ayni isi yapar
    private String password;

    @Column(unique = true)
    private String phoneNumber;

    @Column(unique = true)
    private String email;

    private Boolean built_in; //dokunulamaz yapiyi olusturmayi sagliyor

    private String motherName;

    private String fatherName;

    private int studentNumber;

    private boolean isActive;

    private Long advisorTeacherId;

    private Boolean isAdvisor;

    @Enumerated(EnumType.STRING)
    //User tablosu gender kolonuna EnumTypei string olarak kaydediyor.Bunu yapmazsa dbye 0 1 olarak kaydeder.
    private Gender gender;

    @OneToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private UserRole userRole;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.REMOVE) //student da yazsak ayni seyi
    private List<StudentInfo> studentInfos;
}
