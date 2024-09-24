package com.dogukan.controller.business;

import com.dogukan.payload.request.business.MeetRequest;
import com.dogukan.payload.response.ResponseMessage;
import com.dogukan.payload.response.business.MeetResponse;
import com.dogukan.service.business.MeetService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/meet")//restcontroller yada controller yazmazsak burayi kullanamayiz.
public class MeetController {

    private final MeetService meetService;

    @PostMapping("/save") //http://localhost:8080/meet/save + POST +JSON
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    public ResponseMessage<MeetResponse> saveMeet(HttpServletRequest httpServletRequest, @RequestBody @Valid MeetRequest meetRequest) {
        return meetService.saveMeet(httpServletRequest, meetRequest);
    }

    // Not: getAll *************************************************************
    // Not: getByMeetId ********************************************************

    // Not: getAllWithPage *****************************************************
    // Not: gettAllByAdvTeacherByPage() ****************************************

    @DeleteMapping("/delete/{meetId}")//http://localhost:8080/meet/delete/2 + DELETE
    @PreAuthorize("hasAnyAuthority('TEACHER','ADMIN')")
    public ResponseMessage delete(@PathVariable Long meetId, HttpServletRequest httpServletRequest) {
        return meetService.delete(meetId, httpServletRequest);
    }
}
