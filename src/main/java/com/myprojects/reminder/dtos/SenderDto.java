package com.myprojects.reminder.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SenderDto {
    private String email;
    private String password;
}
