package ru.academits.orlov.phonebookhibernate.service;

import ru.academits.orlov.phonebookhibernate.dto.GeneralResponse;
import ru.academits.orlov.phonebookhibernate.entity.Contact;

import java.util.List;

public interface ContactsService {
    List<Contact> getContacts(String term);

    GeneralResponse createOrUpdateContact(Contact contact);

    GeneralResponse deleteContact(Long id);
}
