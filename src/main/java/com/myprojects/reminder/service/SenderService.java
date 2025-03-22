package com.myprojects.reminder.service;

import com.myprojects.reminder.dtos.SenderDto;
import com.myprojects.reminder.model.Sender;
import com.myprojects.reminder.repository.SenderRepository;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.stereotype.Service;

import static com.myprojects.reminder.mapper.SenderMapper.senderDtotoSender;

@Service
public class SenderService {
    private final SenderRepository senderRepository;

    public SenderService(SenderRepository senderRepository) {
        this.senderRepository = senderRepository;
    }

    public Sender createSender(@NotEmpty(message = "Username cannot be empty or null") @Email(message = "You must enter email") String email,
                               @NotEmpty(message = "Password cannot be empty or null") String password) {
        SenderDto senderDto = new SenderDto(email, password);
        Sender sender = senderDtotoSender(senderDto);
        senderRepository.save(sender);
        return sender;
    }
}
