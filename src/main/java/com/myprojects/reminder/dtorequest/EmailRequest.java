package com.myprojects.reminder.dtorequest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailRequest {
    @NotEmpty(message = "Title cannot be empty or null")
    private String title;
    @NotEmpty(message = "Content cannot be empty or null")
    private String content;

    private long delay;

}