package com.dogukan.controller.business;

import com.dogukan.payload.request.business.MeetRequest;
import com.dogukan.payload.response.ResponseMessage;
import com.dogukan.payload.response.business.MeetResponse;
import com.dogukan.service.business.MeetService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/meet")//restcontroller yada controller yazmazsak burayi kullanamayiz.
public class MeetController {

    private final MeetService meetService;

    @PostMapping("/save")
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    public ResponseMessage<MeetResponse> saveMeet(HttpServletRequest httpServletRequest, @RequestBody @Valid MeetRequest meetRequest) {
        return meetService.saveMeet(httpServletRequest, meetRequest);
    }
}
