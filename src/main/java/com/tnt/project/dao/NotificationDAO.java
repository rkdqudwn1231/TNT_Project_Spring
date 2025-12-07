package com.tnt.project.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tnt.project.dto.NotificationDTO;

@Repository
public class NotificationDAO {

    @Autowired
    private SqlSession mybatis;

    /**
     * 알림 저장
     */
    public void insert(NotificationDTO dto) {
        mybatis.insert("Notification.insert", dto);
    }

    /**
     * 내가 받은 알림 목록 조회
     */
    public List<NotificationDTO> findByMemberId(String memberId) {
        return mybatis.selectList("Notification.findByMemberId", memberId);
    }

    /**
     * 특정 알림 한 건 읽음 처리
     */
    public int markAsRead(long seq) {
        return mybatis.update("Notification.markAsRead", seq);
    }

    /**
     * 특정 유저의 모든 알림 읽음 처리
     */
    public int markAllAsRead(String memberId) {
        return mybatis.update("Notification.markAllAsRead", memberId);
    }

    /**
     * 안 읽은 알림 개수 (헤더 알림 아이콘 뱃지용)
     */
    public int countUnread(String memberId) {
        return mybatis.selectOne("Notification.countUnread", memberId);
    }
}
