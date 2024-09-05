package com.dogukan.contactmessage.controller;


import com.dogukan.contactmessage.dto.ContactMessageRequest;
import com.dogukan.contactmessage.dto.ContactMessageResponse;
import com.dogukan.contactmessage.entity.ContactMessage;
import com.dogukan.contactmessage.service.ContactMessageService;
import com.dogukan.payload.response.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/contactMessages")
@RequiredArgsConstructor
public class ContactMessageController {


    private final ContactMessageService contactMessageService;

    @PostMapping("/save") // http://localhost:8080/contactMessages/save  + POST
    public ResponseMessage<ContactMessageResponse> saveContact(@RequestBody @Valid ContactMessageRequest contactMessageRequest) {
        return contactMessageService.save(contactMessageRequest);
    }

    @GetMapping("/getAll") // http://localhost:8080/contactMessages/getAll + GET
    public Page<ContactMessageResponse> getAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "dateTime") String sort,
            @RequestParam(value = "type", defaultValue = "desc") String type
    ) {
        return contactMessageService.getAll(page, size, sort, type);
    }

    @GetMapping("/searchByEmail")// http://localhost:8080/contactMessages/searchByEmail?email=aaa@bbb.com
    public Page<ContactMessageResponse> searchByEmail(
            @RequestParam(value = "email") String email,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "dateTime") String sort,
            @RequestParam(value = "type", defaultValue = "desc") String type
    ) {
        return contactMessageService.searchByEmail(email, page, size, sort, type);
    }

    // Not: *************************************** searchBySubject ***************************************

    @GetMapping("/searchBySubject") //http://localhost:8080/contactMessages/searchByEmail?subject=
    public Page<ContactMessageResponse> searchBySubject(
            @RequestParam(value = "subject") String subject,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "dateTime") String sort,
            @RequestParam(value = "type", defaultValue = "desc") String type
    ) {
        return contactMessageService.searchBySubject(subject, page, size, sort, type);
    }


    // Not: ODEVVV    searchByDateBetween ***************************************


    // Not: *********************************** deleteByIdParam ***************************************

    @DeleteMapping("/deleteByIdParam")//http://localhost:8080/contactMessages/deleteByIdParam?id=1
    public ResponseMessage<ContactMessageResponse> deleteByIdParam(@RequestParam(value = "id") Long id) {
        return contactMessageService.deleteByIdParam(id);
    }


    // Not: ***************************************** deleteById ***************************************



    // Not: *********************************** getByIdWithParam ***************************************

    // Not: ************************************ getByIdWithPath ***************************************

}
