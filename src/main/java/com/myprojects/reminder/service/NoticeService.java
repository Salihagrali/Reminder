package com.myprojects.reminder.service;

import com.myprojects.reminder.dtos.NoticeDto;
import com.myprojects.reminder.model.Notice;
import com.myprojects.reminder.model.UserEntity;
import com.myprojects.reminder.repository.NoticeRepository;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.stereotype.Service;

import static com.myprojects.reminder.mapper.NoticeMapper.noticeDtoToNotice;

@Service
public class NoticeService {
    private final NoticeRepository noticeRepository;

    public NoticeService(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    public Notice createNotice(@NotEmpty(message = "Title cannot be empty or null") String title,
                               @NotEmpty(message = "Content cannot be empty or null") String content,
                               UserEntity user) {
        NoticeDto noticeDto = new NoticeDto(title, content);
        Notice notice = noticeDtoToNotice(noticeDto);
        notice.setAuthor(user);
        noticeRepository.save(notice);
        return notice;
    }
}
