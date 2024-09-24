package com.dogukan.repository.business;


import com.dogukan.entity.concretes.business.Meet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
//yukaridaki anatasyonda repository yerine component yazarsak eger olacak olan su olur exception firladigi zaman standart olarak component exception ne ise onu dondurur ama eger repository diye tanimlarsak repository yani database exceptionlari firlar.
public interface MeetRepository extends JpaRepository<Meet, Long> {
    List<Meet> getByAdvisoryTeacher_IdEquals(Long advisoryTeacherId);

    List<Meet> findByStudentList_IdEquals(Long studentId);
}
