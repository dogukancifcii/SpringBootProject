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

    @GetMapping("/searchByEmail")// http://localhost:8080/contactMessages/searchByEmail?email
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

    // Not: ODEVVV    searchByDateBetween ***************************************

    // Not: *********************************** deleteByIdParam ***************************************

    // Not: ***************************************** deleteById ***************************************

    // Not: *********************************** getByIdWithParam ***************************************

    // Not: ************************************ getByIdWithPath ***************************************

}
