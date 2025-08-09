package ru.academits.orlov.phonebookhibernate.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.academits.orlov.phonebookhibernate.entity.Contact;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ContactsRepositoryTest {
    @Autowired
    private ContactsRepository contactsRepository;

    private Contact testContact1;
    private Contact testContact2;

    @BeforeEach
    public void setUp() {
        testContact1 = new Contact();
        testContact1.setSurname("Orlov");
        testContact1.setName("Evgenii");
        testContact1.setPhoneNumber("1234");

        testContact2 = new Contact();
        testContact2.setSurname("Oleg");
        testContact2.setName("John");
        testContact2.setPhoneNumber("4321");

        Contact testContact3 = new Contact();
        testContact3.setSurname("Smith");
        testContact3.setName("Alex");
        testContact3.setPhoneNumber("1111");

        contactsRepository.save(testContact1);
        contactsRepository.save(testContact2);
        contactsRepository.save(testContact3);
    }

    @Test
    public void findByPhoneNumber_phoneNumberExists_contactFound() {
        Contact foundedContact = contactsRepository.findByPhoneNumber(testContact1.getPhoneNumber());

        assertNotNull(foundedContact);
        assertEquals(testContact1.getId(), foundedContact.getId());
        assertEquals(testContact1.getSurname(), foundedContact.getSurname());
        assertEquals(testContact1.getName(), foundedContact.getName());
        assertEquals(testContact1.getPhoneNumber(), foundedContact.getPhoneNumber());
    }

    @Test
    public void findByPhoneNumber_phoneNumberNotExist_contactNotFound() {
        String notExistingPhoneNumber = "000";
        Contact foundedContact = contactsRepository.findByPhoneNumber(notExistingPhoneNumber);

        assertNull(foundedContact);
    }

    @Test
    public void findAllByTerm_nameMatches_contactFound() {
        String term = "oh";
        List<Contact> foundedContacts = contactsRepository.findAllBySurnameContainsIgnoreCaseOrNameContainsIgnoreCaseOrPhoneNumberContainsIgnoreCase(term, term, term);

        assertNotNull(foundedContacts);
        assertEquals(1, foundedContacts.size());
        assertEquals(testContact2.getId(), foundedContacts.getFirst().getId());
        assertEquals(testContact2.getSurname(), foundedContacts.getFirst().getSurname());
        assertEquals(testContact2.getName(), foundedContacts.getFirst().getName());
    }

    @Test
    public void findAllByTerm_surnameMatches_contactFound() {
        String term = "ol";
        List<Contact> foundedContacts = contactsRepository.findAllBySurnameContainsIgnoreCaseOrNameContainsIgnoreCaseOrPhoneNumberContainsIgnoreCase(term, term, term);

        assertNotNull(foundedContacts);
        assertEquals(1, foundedContacts.size());
        assertEquals(testContact2.getId(), foundedContacts.getFirst().getId());
        assertEquals(testContact2.getSurname(), foundedContacts.getFirst().getSurname());
        assertEquals(testContact2.getName(), foundedContacts.getFirst().getName());
    }

    @Test
    public void findAllByTerm_phoneNumberMatches_contactFound() {
        String term = "32";
        List<Contact> foundedContacts = contactsRepository.findAllBySurnameContainsIgnoreCaseOrNameContainsIgnoreCaseOrPhoneNumberContainsIgnoreCase(term, term, term);

        assertNotNull(foundedContacts);
        assertEquals(1, foundedContacts.size());
        assertEquals(testContact2.getId(), foundedContacts.getFirst().getId());
        assertEquals(testContact2.getSurname(), foundedContacts.getFirst().getSurname());
        assertEquals(testContact2.getName(), foundedContacts.getFirst().getName());
    }

    @Test
    public void findAllByTerm_nameAndSurnameMatch_contactsFound() {
        String term = "le";
        List<Contact> foundedContacts = contactsRepository.findAllBySurnameContainsIgnoreCaseOrNameContainsIgnoreCaseOrPhoneNumberContainsIgnoreCase(term, term, term);

        assertNotNull(foundedContacts);
        assertEquals(2, foundedContacts.size());
    }

    @Test
    public void findAllByTerm_noMatches_contactsNotFound() {
        String term = "ton";
        List<Contact> foundedContacts = contactsRepository.findAllBySurnameContainsIgnoreCaseOrNameContainsIgnoreCaseOrPhoneNumberContainsIgnoreCase(term, term, term);

        assertEquals(0, foundedContacts.size());
    }
}
