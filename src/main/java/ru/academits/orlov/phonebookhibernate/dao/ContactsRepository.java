package ru.academits.orlov.phonebookhibernate.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.academits.orlov.phonebookhibernate.entity.Contact;

import java.util.List;

public interface ContactsRepository extends JpaRepository<Contact, Long> {
    @Query("select c from Contact c where c.surname like %?1% or c.name like %?1% or c.phoneNumber like %?1%")
    List<Contact> findAllByTerm(String term);

    Contact findByPhoneNumber(String phoneNumber);
}
