//package com.team03.godchoice.controller;
//
//import com.team03.godchoice.service.NotificationService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestHeader;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
//
//@RestController
//@RequiredArgsConstructor
//public class NotificationController {
//
//    private final NotificationService notificationService;
//
//
//    @GetMapping(value = "/subscribe/{id}", produces = "text/event-stream")
//    public SseEmitter subscribe(@PathVariable Long id,
//                                @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
//        return notificationService.subscribe(id, lastEventId);
//    }
//}
