package ru.academits.orlov.phonebookhibernate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GeneralResponse {
    private boolean success;
    private String message;

    public static GeneralResponse getSuccessResponse() {
        return new GeneralResponse(true, null);
    }

    public static GeneralResponse getErrorResponse(String message) {
        return new GeneralResponse(false, message);
    }
}
