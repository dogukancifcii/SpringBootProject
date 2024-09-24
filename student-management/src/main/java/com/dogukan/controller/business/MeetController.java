package com.dogukan.controller.business;

import com.dogukan.service.business.MeetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/meet")//restcontroller yada controller yazmazsak burayi kullanamayiz.
public class MeetController {

    private final MeetService meetService;


}
