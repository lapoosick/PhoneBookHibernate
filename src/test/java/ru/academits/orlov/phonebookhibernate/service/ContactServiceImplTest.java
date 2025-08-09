package ru.academits.orlov.phonebookhibernate.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.academits.orlov.phonebookhibernate.dao.ContactsRepository;
import ru.academits.orlov.phonebookhibernate.entity.Contact;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ContactServiceImplTest {
    private final ContactsRepository mockContactsRepository = Mockito.mock(ContactsRepository.class);
    private final ContactsService contactsService = new ContactsServiceImpl(mockContactsRepository);

    @Test
    public void createContact_positiveCase() {
        Contact newContact = new Contact();
        newContact.setSurname("Smith");
        newContact.setName("Oleg");
        newContact.setPhoneNumber("111");

        contactsService.createOrUpdateContact(newContact);

        verify(mockContactsRepository, times(1)).save(newContact);
    }

    @Test
    public void createContact_negativeCase_surnameIsNull() {
        Contact newContact = new Contact();
        newContact.setName("Oleg");
        newContact.setPhoneNumber("111");

        String errorMessage = contactsService.createOrUpdateContact(newContact).getMessage();

        assertEquals(errorMessage, "Не заполнено обязательное поле.");

        verify(mockContactsRepository, times(0)).save(newContact);
    }

    @Test
    public void createContact_negativeCase_nameIsNull() {
        Contact newContact = new Contact();
        newContact.setSurname("Smith");
        newContact.setPhoneNumber("111");

        String errorMessage = contactsService.createOrUpdateContact(newContact).getMessage();

        assertEquals(errorMessage, "Не заполнено обязательное поле.");

        verify(mockContactsRepository, times(0)).save(newContact);
    }

    @Test
    public void createContact_negativeCase_phoneNumberIsNull() {
        Contact newContact = new Contact();
        newContact.setSurname("Smith");
        newContact.setName("Oleg");

        String errorMessage = contactsService.createOrUpdateContact(newContact).getMessage();

        assertEquals(errorMessage, "Не заполнено обязательное поле.");

        verify(mockContactsRepository, times(0)).save(newContact);
    }

    @Test
    public void createContact_negativeCase_phoneNumberExists() {
        Contact newContact = new Contact();
        newContact.setSurname("Smith");
        newContact.setName("Oleg");
        newContact.setPhoneNumber("111");

        when(mockContactsRepository.findByPhoneNumber("111")).thenReturn(newContact);

        contactsService.createOrUpdateContact(newContact);

        verify(mockContactsRepository, times(0)).save(newContact);
    }

    @Test
    public void updateContact_positiveCase() {
        Contact repositoryContact = new Contact();
        repositoryContact.setId(1L);
        repositoryContact.setSurname("Smith");
        repositoryContact.setName("Oleg");
        repositoryContact.setPhoneNumber("111");

        Contact updateContact = new Contact();
        updateContact.setId(1L);
        updateContact.setSurname("Born");
        updateContact.setName("Alex");
        updateContact.setPhoneNumber("111");

        when(mockContactsRepository.findById(updateContact.getId())).thenReturn(Optional.of(repositoryContact));
        when(mockContactsRepository.findByPhoneNumber(updateContact.getPhoneNumber())).thenReturn(repositoryContact);

        contactsService.createOrUpdateContact(updateContact);

        verify(mockContactsRepository, times(1)).save(repositoryContact);
    }

    @Test
    public void updateContact_negativeCase_contactNotFoundById() {
        Contact updateContact = new Contact();
        updateContact.setId(1L);
        updateContact.setSurname("Smith");
        updateContact.setName("Oleg");
        updateContact.setPhoneNumber("111");

        when(mockContactsRepository.findById(updateContact.getId())).thenReturn(Optional.empty());

        String errorMessage = contactsService.createOrUpdateContact(updateContact).getMessage();

        assertEquals(errorMessage, "Пользователь не найден.");

        verify(mockContactsRepository, times(0)).save(updateContact);
    }

    @Test
    public void updateContact_negativeCase_phoneNumberExists() {
        Contact repositoryContact1 = new Contact();
        repositoryContact1.setId(1L);
        repositoryContact1.setSurname("Smith");
        repositoryContact1.setName("Oleg");
        repositoryContact1.setPhoneNumber("111");

        Contact repositoryContact2 = new Contact();
        repositoryContact2.setId(2L);
        repositoryContact2.setSurname("Doe");
        repositoryContact2.setName("Alex");
        repositoryContact2.setPhoneNumber("222");

        Contact updateContact = new Contact();
        updateContact.setId(1L);
        updateContact.setSurname("Born");
        updateContact.setName("John");
        updateContact.setPhoneNumber("222");

        when(mockContactsRepository.findById(updateContact.getId())).thenReturn(Optional.of(repositoryContact1));
        when(mockContactsRepository.findByPhoneNumber(updateContact.getPhoneNumber())).thenReturn(repositoryContact2);

        String errorMessage = contactsService.createOrUpdateContact(updateContact).getMessage();

        assertEquals(errorMessage, "Контакт с номером телефона " + updateContact.getPhoneNumber() + " уже существует.");

        verify(mockContactsRepository, times(0)).save(updateContact);
    }

    @Test
    public void deleteContact_positiveCase() {
        Contact repositoryContact = new Contact();
        repositoryContact.setId(1L);
        repositoryContact.setSurname("Smith");
        repositoryContact.setName("Oleg");
        repositoryContact.setPhoneNumber("111");

        Long contactId = repositoryContact.getId();

        when(mockContactsRepository.findById(contactId)).thenReturn(Optional.of(repositoryContact));

        contactsService.deleteContact(contactId);

        verify(mockContactsRepository, times(1)).deleteById(contactId);
    }

    @Test
    public void deleteContact_negativeCase_idIsNull() {
        String errorMessage = contactsService.deleteContact(null).getMessage();

        assertEquals(errorMessage, "Контакт с id = " + null + " не найден.");

        verify(mockContactsRepository, times(0)).deleteById(null);
    }

    @Test
    public void deleteContact_negativeCase_contactNotPresent() {
        Long contactId = 1L;

        when(mockContactsRepository.findById(contactId)).thenReturn(Optional.empty());

        String errorMessage = contactsService.deleteContact(contactId).getMessage();

        assertEquals(errorMessage, "Контакт с id = " + contactId + " не найден.");

        verify(mockContactsRepository, times(0)).deleteById(contactId);
    }

    @Test
    public void getContacts_termIsNull() {
        contactsService.getContacts(null);

        verify(mockContactsRepository, times(1)).findAll();
    }

    @Test
    public void getContacts_termIsEmpty() {
        contactsService.getContacts("");

        verify(mockContactsRepository, times(1)).findAll();
    }

    @Test
    public void getContacts_findByTerm() {
        String term = "Born";

        contactsService.getContacts(term);

        verify(mockContactsRepository, times(1)).findAllBySurnameContainsIgnoreCaseOrNameContainsIgnoreCaseOrPhoneNumberContainsIgnoreCase(term, term, term);
    }
}
