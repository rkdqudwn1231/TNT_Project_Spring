package com.tnt.project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tnt.project.dto.NotificationDTO;
import com.tnt.project.services.NotificationService;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    /**
     * 1) 내가 받은 알림 목록 조회
     * GET /notification/list/{memberId}
     */
    @GetMapping("/list/{memberId}")
    public ResponseEntity<?> getNotifications(@PathVariable("memberId") String memberId) {
        List<NotificationDTO> list = notificationService.findByMemberId(memberId);
        return ResponseEntity.ok(list);
    }

    /**
     * 2) 특정 알림 읽음 처리
     * PUT /notification/read/{seq}
     */
    @PutMapping("/read/{seq}")
    public ResponseEntity<?> readNotification(@PathVariable("seq") long seq) {
        notificationService.markAsRead(seq);
        return ResponseEntity.ok().build();
    }

    /**
     * 3) 해당 유저의 전체 알림을 읽음 처리
     * PUT /notification/read-all/{memberId}
     */
    @PutMapping("/read-all/{memberId}")
    public ResponseEntity<?> readAll(@PathVariable("memberId") String memberId) {
        notificationService.markAllAsRead(memberId);
        return ResponseEntity.ok().build();
    }
}
