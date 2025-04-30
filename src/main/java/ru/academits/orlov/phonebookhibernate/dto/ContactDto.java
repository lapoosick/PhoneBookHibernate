package ru.academits.orlov.phonebookhibernate.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactDto {
    private Long id;
    private String surname;
    private String name;
    private String phoneNumber;
}
