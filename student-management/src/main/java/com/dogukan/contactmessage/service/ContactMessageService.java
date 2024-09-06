package com.dogukan.contactmessage.service;


import com.dogukan.contactmessage.dto.ContactMessageRequest;
import com.dogukan.contactmessage.dto.ContactMessageResponse;
import com.dogukan.contactmessage.entity.ContactMessage;
import com.dogukan.contactmessage.mapper.ContactMessageMapper;
import com.dogukan.contactmessage.messages.Messages;
import com.dogukan.contactmessage.repository.ContactMessageRepository;
import com.dogukan.exception.ConflictException;
import com.dogukan.exception.ResourceNotFoundException;
import com.dogukan.payload.response.ResponseMessage;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ContactMessageService {

    private final ContactMessageRepository contactMessageRepository;
    private final ContactMessageMapper createContactMessage;


    public ResponseMessage<ContactMessageResponse> save(ContactMessageRequest contactMessageRequest) {
        ContactMessage contactMessage = createContactMessage.requestToContactMessage(contactMessageRequest); //DTO to POJO
        if (contactMessageRepository.existsByEmail(contactMessage.getEmail())) { //burada email kontrolu yaptik
            throw new IllegalArgumentException("This email adress already in use");
        }
        ContactMessage savedData = contactMessageRepository.save(contactMessage);

        return ResponseMessage.<ContactMessageResponse>builder() //burada builder ile nesne olusturmus olduk.yani new yerine kullandık
                .message("Contact Message Created Successfully")
                .httpStatus(HttpStatus.CREATED)//201
                .object(createContactMessage.contactMessageToResponse(savedData)).build(); //POJO to DTO
    }

    public Page<ContactMessageResponse> getAll(int page, int size, String sort, String type) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());

        if (Objects.equals(type, "desc")) { //Önemli !!! type.equals("desc") --> null safe degildir  NulllPointerException verebilir
            //Objects.equals otomatik olarak null kontrolunu kendisi yapar.Eğer ilk argüman (type) null ise, ikinci argümanla ("desc") karşılaştırma yapılmaz, doğrudan false döner
            //Eğer iki değer de null ise, bu durumda true döner.
            pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        }
        return contactMessageRepository.findAll(pageable).map(createContactMessage::contactMessageToResponse); //burada olusan nesne icindeki ContackMessage donusumunu yapmis olduk
    }

    public Page<ContactMessageResponse> searchByEmail(String email, int page, int size, String sort, String type) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());

        if (Objects.equals(type, "desc")) {
            pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        }
        return contactMessageRepository.findByEmailEquals(email, pageable).map(createContactMessage::contactMessageToResponse);
    }

    public Page<ContactMessageResponse> searchBySubject(String subject, int page, int size, String sort, String type) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());

        if (Objects.equals(type, "desc")) {
            pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        }
        return contactMessageRepository.findBySubjectEquals(subject, pageable).map(createContactMessage::contactMessageToResponse);
    }

    /*public ResponseMessage<ContactMessageResponse> deleteById(Long id) {
        ContactMessage contactMessage = contactMessageRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Contact Message is not found by id " + id));

        contactMessageRepository.delete(contactMessage);
        return ResponseMessage.<ContactMessageResponse>builder()
                .message("Contact Message Deleted Successfully")
                .httpStatus(HttpStatus.OK)
                .object(createContactMessage.contactMessageToResponse(contactMessage))
                .build();
    }*/

    public String deleteById(Long id) {
        getContactMessageById(id);
        contactMessageRepository.deleteById(id);
        return Messages.CONTACT_MESSAGE_DELETED_SUCCESSFULLY;
    }


    /*public ResponseMessage<ContactMessageResponse> getById(Long id) {

        ContactMessage contactMessage = contactMessageRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Contact Message is not found by id" + id));

        return ResponseMessage.<ContactMessageResponse>builder()
                .message("Contact Message brought successfully..!")
                .httpStatus(HttpStatus.OK)
                .object(createContactMessage.contactMessageToResponse(contactMessage))
                .build();

    }*/


    public ContactMessage getContactMessageById(Long id) {
        return contactMessageRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(Messages.NOT_FOUND_MESSAGE));
    }



    /*public Page<ContactMessageResponse> searchByDateBetween(LocalDateTime startDate, LocalDateTime endDate, int page, int size, String sort, String type) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        if (Objects.equals(type, "desc")) {
            pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        }

        return contactMessageRepository.findByDateTimeBetween(startDate, endDate, pageable).map(createContactMessage::contactMessageToResponse);

    }*/

    public List<ContactMessage> searchByDateBetween(String beginDateString, String endDateString) {
        try {
            LocalDate beginDate = LocalDate.parse(beginDateString);
            LocalDate endDate = LocalDate.parse(endDateString);
            return contactMessageRepository.findMessagesBetweenDates(beginDate, endDate);
        } catch (DateTimeParseException e) {
            throw new ConflictException(Messages.WRONG_DATE_FORMAT);
        }
    }

}
