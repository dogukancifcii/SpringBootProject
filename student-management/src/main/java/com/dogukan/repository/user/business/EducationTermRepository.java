package com.dogukan.repository.user.business;


import com.dogukan.entity.concretes.business.EducationTerm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EducationTermRepository extends JpaRepository<EducationTerm, Long> {

}
