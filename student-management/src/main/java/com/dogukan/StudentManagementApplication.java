package com.dogukan;

import com.dogukan.entity.concretes.user.UserRole;
import com.dogukan.entity.enums.Gender;
import com.dogukan.entity.enums.RoleType;
import com.dogukan.payload.request.user.UserRequest;
import com.dogukan.repository.user.UserRoleRepository;
import com.dogukan.service.user.UserRoleService;
import com.dogukan.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;

@SpringBootApplication
//burada implement etme sebebi assagidaki run methodunu eklemek icin
//run methodunun amaci ise spring calismadan once calisan kodlardir.
public class StudentManagementApplication implements CommandLineRunner {


    private final UserRoleService userRoleService;
    private final UserRoleRepository userRoleRepository;
    private final UserService userService;

    public StudentManagementApplication(UserRoleService userRoleService, UserRoleRepository userRoleRepository, UserService userService) {
        this.userRoleService = userRoleService;
        this.userRoleRepository = userRoleRepository;
        this.userService = userService;
    }


    public static void main(String[] args) {
        SpringApplication.run(StudentManagementApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        //role tablomu dolduruyorum ama bosmu kontorl ediyorum
        if(userRoleService.getAllUserRole().isEmpty()){
            UserRole admin = new UserRole();

            admin.setRoleType(RoleType.ADMIN);
            admin.setRoleName("Admin");
            userRoleRepository.save(admin);

            UserRole dean = new UserRole();
            dean.setRoleType(RoleType.MANAGER);
            dean.setRoleName("Dean");
            userRoleRepository.save(dean);

            UserRole viceDean = new UserRole();
            viceDean.setRoleType(RoleType.ASSISTANT_MANAGER);
            viceDean.setRoleName("ViceDean");
            userRoleRepository.save(viceDean);

            UserRole student = new UserRole();
            student.setRoleType(RoleType.STUDENT);
            student.setRoleName("Student");
            userRoleRepository.save(student);

            UserRole teacher = new UserRole();
            teacher.setRoleType(RoleType.TEACHER);
            teacher.setRoleName("Teacher");
            userRoleRepository.save(teacher);
        }

        //Admin olusturulacak built_in ama
        if(userService.countALlAdmins()==0){
            UserRequest adminRequest = new UserRequest();
            adminRequest.setUsername("Admin");
            adminRequest.setEmail("admin@admin.com");
            adminRequest.setSsn("111-11-1111");
            adminRequest.setPassword("12345678");
            adminRequest.setName("X");
            adminRequest.setSurname("Y");
            adminRequest.setPhoneNumber("111-111-1111");
            adminRequest.setGender(Gender.FEMALE);
            adminRequest.setBirthDay(LocalDate.of(1980,2,2));
            adminRequest.setBirthPlace("Istanbul");

            userService.saveUser(adminRequest, "Admin");
        }
    }
}
