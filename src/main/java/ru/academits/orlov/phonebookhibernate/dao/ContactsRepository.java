package ru.academits.orlov.phonebookhibernate.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.academits.orlov.phonebookhibernate.entity.Contact;

import java.util.List;

public interface ContactsRepository extends JpaRepository<Contact, Long> {
    @Query("from Contact c where lower(c.surname) like lower(concat('%', ?1, '%')) " +
            "or lower(c.name) like lower(concat('%', ?1, '%')) " +
            "or lower(c.phoneNumber) like lower(concat('%', ?1, '%'))")
    List<Contact> findAllByTerm(String term);

    // Это просто другой вариант первого запроса.
    // Тут пришлось бы три раза передавать параметр term.
    // Кажется, вариант с HQL выглядит получше.
    List<Contact> findAllBySurnameContainsIgnoreCaseOrNameContainsIgnoreCaseOrPhoneNumberContainsIgnoreCase(String surname, String name, String phoneNumber);

    Contact findByPhoneNumber(String phoneNumber);
}
