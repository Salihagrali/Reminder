package com.myprojects.reminder.dtorequest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailRequest {
    //for Sender entity
    @NotEmpty(message = "Username cannot be empty or null")
    @Email(message = "You must enter email")
    private String email;
    @NotEmpty(message = "Password cannot be empty or null")
    private String password;

    //for Notice entity
    @NotEmpty(message = "Title cannot be empty or null")
    private String title;
    @NotEmpty(message = "Content cannot be empty or null")
    private String content;

}