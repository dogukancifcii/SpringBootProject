package com.dogukan.contactmessage.service;


import com.dogukan.contactmessage.dto.ContactMessageRequest;
import com.dogukan.contactmessage.dto.ContactMessageResponse;
import com.dogukan.contactmessage.repository.ContactMessageRepository;
import com.dogukan.payload.response.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContactMessageService {

    private final ContactMessageRepository contactMessageRepository;


    public ResponseMessage<ContactMessageResponse> save(ContactMessageRequest contactMessageRequest) {
        return null;
    }
}
