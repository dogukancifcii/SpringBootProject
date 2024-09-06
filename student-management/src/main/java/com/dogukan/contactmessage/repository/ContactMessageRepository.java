package com.dogukan.contactmessage.repository;

import com.dogukan.contactmessage.dto.ContactMessageResponse;
import com.dogukan.contactmessage.entity.ContactMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {
    Page<ContactMessage> findByEmailEquals(String email, Pageable pageable);

    Page<ContactMessage> findBySubjectEquals(String subject, Pageable pageable);


    /*Page<ContactMessage> findByDateTimeBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);*/

    //burda FUNCTION kismi localDateTime dan LocalDate almamizi sagladi
    @Query("select c from ContactMessage c where FUNCTION('DATE', c.dateTime) between ?1 and ?2")
    List<ContactMessage> findMessagesBetweenDates(LocalDate beginDate, LocalDate endDate);

    boolean existsByEmail(String email);


}
