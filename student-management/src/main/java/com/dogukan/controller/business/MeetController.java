package com.dogukan.controller.business;

import com.dogukan.payload.request.business.MeetRequest;
import com.dogukan.payload.response.ResponseMessage;
import com.dogukan.payload.response.business.MeetResponse;
import com.dogukan.service.business.MeetService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

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

    @GetMapping("/getAll") //http://localhost:8080/meet/getAll  + GET
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public List<MeetResponse> getAll() {
        return meetService.getAll();
    }


    // Not: getByMeetId ********************************************************

    @GetMapping("/getByMeetId/{meetId}")//http://localhost:8080/meet/getByMeetId/2
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseMessage<MeetResponse> getByMeetId(@PathVariable Long meetId) {
        return meetService.getByMeetId(meetId);
    }

    // Not: getAllWithPage *****************************************************

    @GetMapping("/getAllWithPage")//http://localhost:8080/meet/getAllWithPage
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public Page<MeetResponse> getAllWithPage(
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size
    ) {
        return meetService.getAllWithPage(page, size);
    }

    // Not: gettAllByAdvTeacherByPage() ****************************************

    @GetMapping("/gettAllByAdvTeacherByPage")
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    public ResponseEntity<Page<MeetResponse>> getAllMeetByTeacher(HttpServletRequest httpServletRequest,
                                                                  @RequestParam(value = "page") int page,
                                                                  @RequestParam(value = "size") int size) {
        return meetService.getAllMeetByTeacher(httpServletRequest, page, size);
    }


    @DeleteMapping("/delete/{meetId}")//http://localhost:8080/meet/delete/2 + DELETE
    @PreAuthorize("hasAnyAuthority('TEACHER','ADMIN')")
    public ResponseMessage delete(@PathVariable Long meetId, HttpServletRequest httpServletRequest) {
        return meetService.delete(meetId, httpServletRequest);
    }

    @GetMapping("/getAllMeetByStudent")
    @PreAuthorize("hasAnyAuthority('STUDENT')")
    public List<MeetResponse> getAllMeetByStudent(HttpServletRequest httpServletRequest) {
        return meetService.getAllMeetByStudent(httpServletRequest);
    }
}
