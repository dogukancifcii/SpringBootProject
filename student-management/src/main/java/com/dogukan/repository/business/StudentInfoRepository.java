package com.dogukan.repository.business;

import com.dogukan.entity.concretes.business.StudentInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentInfoRepository extends JpaRepository<StudentInfo, Long> {
}
