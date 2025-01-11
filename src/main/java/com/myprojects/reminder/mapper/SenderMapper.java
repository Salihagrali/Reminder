package com.myprojects.reminder.mapper;

import com.myprojects.reminder.dtos.SenderDto;
import com.myprojects.reminder.model.Sender;

public class SenderMapper {
    public static Sender senderDtotoSender(SenderDto senderDto) {
        return Sender.builder()
                .email(senderDto.getEmail())
                .password(senderDto.getPassword())
                .build();
    }

    public static SenderDto senderToSenderDto(Sender sender) {
        return SenderDto.builder()
                .email(sender.getEmail())
                .password(sender.getPassword())
                .build();
    }
}
