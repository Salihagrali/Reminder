package com.myprojects.reminder.mapper;

import com.myprojects.reminder.dtos.NoticeDto;
import com.myprojects.reminder.model.Notice;

public class NoticeMapper {
    public static Notice noticeDtoToNotice(NoticeDto noticeDto) {
        return Notice.builder()
                .title(noticeDto.getTitle())
                .content(noticeDto.getContent())
                .build();
    }

    public static NoticeDto noticeToNoticeDto(Notice notice) {
        return NoticeDto.builder()
                .title(notice.getTitle())
                .content(notice.getContent())
                .build();
    }
}
