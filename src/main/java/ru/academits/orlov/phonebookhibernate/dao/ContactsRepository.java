package ru.academits.orlov.phonebookhibernate.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.academits.orlov.phonebookhibernate.entity.Contact;

import java.util.List;

public interface ContactsRepository extends JpaRepository<Contact, Long> {
    List<Contact> findAllBySurnameContainsIgnoreCaseOrNameContainsIgnoreCaseOrPhoneNumberContainsIgnoreCase(String surname, String name, String phoneNumber);

    Contact findByPhoneNumber(String phoneNumber);
}
