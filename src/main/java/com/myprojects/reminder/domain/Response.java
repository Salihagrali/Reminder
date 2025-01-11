package com.myprojects.reminder.domain;

import org.springframework.http.HttpStatus;

public record Response(String time, int code, String path, HttpStatus status, String message) {
}
