package ru.academits.orlov.phonebookhibernate.service;

import org.springframework.stereotype.Service;
import ru.academits.orlov.phonebookhibernate.dao.ContactsRepository;
import ru.academits.orlov.phonebookhibernate.dto.GeneralResponse;
import ru.academits.orlov.phonebookhibernate.entity.Contact;

import java.util.List;

@Service
public class ContactsServiceImpl implements ContactsService {
    private final ContactsRepository contactsRepository;

    public ContactsServiceImpl(ContactsRepository contactsRepository) {
        this.contactsRepository = contactsRepository;
    }

    @Override
    public List<Contact> getContacts(String term) {
        if (term == null || term.isEmpty()) {
            return contactsRepository.findAll();
        }

        return contactsRepository.findAllByTerm(term);
    }

    @Override
    public GeneralResponse createOrUpdateContact(Contact contact) {
        try {
            contact.setSurname(validateAndTrimString(contact.getSurname()));
            contact.setName(validateAndTrimString(contact.getName()));
            contact.setPhoneNumber(validateAndTrimString(contact.getPhoneNumber()));
        } catch (IllegalArgumentException e) {
            return GeneralResponse.getErrorResponse(e.getMessage());
        }

        if (contact.getId() == null) {
            return create(contact);
        }

        return update(contact);
    }

    @Override
    public GeneralResponse deleteContact(Long id) {
        if (id == null || contactsRepository.findById(id).isEmpty()) {
            return GeneralResponse.getErrorResponse("Контакт с id = " + id + " не найден.");
        }

        contactsRepository.deleteById(id);

        return GeneralResponse.getSuccessResponse();
    }

    private GeneralResponse create(Contact contact) {
        if (contactsRepository.findByPhoneNumber(contact.getPhoneNumber()) != null) {
            return GeneralResponse.getErrorResponse("Контакт с номером телефона " + contact.getPhoneNumber() + " уже существует.");
        }

        contactsRepository.save(contact);

        return GeneralResponse.getSuccessResponse();
    }

    private GeneralResponse update(Contact contact) {
        Contact repositoryContact = contactsRepository.findById(contact.getId()).orElse(null);

        if (repositoryContact == null) {
            return GeneralResponse.getErrorResponse("Пользователь не найден.");
        }

        String contactPhoneNumber = contact.getPhoneNumber();

        if (!repositoryContact.getPhoneNumber().equalsIgnoreCase(contactPhoneNumber)
                && contactsRepository.findByPhoneNumber(contactPhoneNumber) != null) {
            return GeneralResponse.getErrorResponse("Контакт с номером телефона " + contactPhoneNumber + " уже существует.");
        }

        repositoryContact.setSurname(contact.getSurname());
        repositoryContact.setName(contact.getName());
        repositoryContact.setPhoneNumber(contactPhoneNumber);

        contactsRepository.save(repositoryContact);

        return GeneralResponse.getSuccessResponse();
    }

    private static String validateAndTrimString(String string) {
        if (string == null || string.isBlank()) {
            throw new IllegalArgumentException("Не заполнено обязательное поле.");
        }

        return string.trim();
    }
}
