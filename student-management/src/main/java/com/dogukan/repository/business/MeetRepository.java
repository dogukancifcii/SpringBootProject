package com.dogukan.repository.business;


import com.dogukan.entity.concretes.business.Meet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
//yukaridaki anatasyonda repository yerine component yazarsak eger olacak olan su olur exception firladigi zaman standart olarak component exception ne ise onu dondurur ama eger repository diye tanimlarsak repository yani database exceptionlari firlar.
public interface MeetRepository extends JpaRepository<Meet, Long> {
    List<Meet> getByAdvisoryTeacher_IdEquals(Long advisoryTeacherId);

    List<Meet> findByStudentList_IdEquals(Long studentId);

    @Query("select m from Meet m where m.advisoryTeacher.username = ?1")
    Page<Meet> findByAdvisoryTeacher_IdEquals(Long advisoryTeacherId, Pageable pageable);

}
