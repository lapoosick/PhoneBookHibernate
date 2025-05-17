package ru.academits.orlov.phonebookhibernate.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.academits.orlov.phonebookhibernate.entity.Contact;

import java.util.List;

public interface ContactsRepository extends JpaRepository<Contact, Long> {
//    @Query("from Contact c where lower(c.surname) like concat('%', lower(:term), '%') " +
//            "or lower(c.name) like concat('%', lower(:term), '%') " +
//            "or lower(c.phoneNumber) like concat('%', lower(:term), '%')")
//    List<Contact> findAllByTerm(String term);

    List<Contact> findAllBySurnameContainsIgnoreCaseOrNameContainsIgnoreCaseOrPhoneNumberContainsIgnoreCase(String surname, String name, String phoneNumber);

    Contact findByPhoneNumber(String phoneNumber);
}
