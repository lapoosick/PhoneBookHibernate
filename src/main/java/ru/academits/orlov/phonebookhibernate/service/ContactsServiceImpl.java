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
    public GeneralResponse createContact(Contact contact) {
        if (contact.getSurname() == null || contact.getSurname().isEmpty()) {
            return GeneralResponse.getErrorResponse("Не указана фамилия.");
        }

        if (contact.getName() == null || contact.getName().isEmpty()) {
            return GeneralResponse.getErrorResponse("Не указано имя.");
        }

        if (contact.getPhoneNumber() == null || contact.getPhoneNumber().isEmpty()) {
            return GeneralResponse.getErrorResponse("Не указан телефон.");
        }

        if (contact.getId() == null) {
            return create(contact);
        }

        return update(contact);
    }

    @Override
    public GeneralResponse deleteContact(Long id) {
        if (id == null || contactsRepository.findById(id).isEmpty()) {
            return GeneralResponse.getErrorResponse("Контакт не найден.");
        }

        long contactOrdinalNumber = contactsRepository.findById(id).get().getOrdinalNumber();
        long lastContactId = contactsRepository.findByOrdinalNumber(contactsRepository.count()).getId();

        contactsRepository.deleteById(id);

        long updateOrdinalNumbersCount = lastContactId - id;

        if (contactsRepository.count() != 0) {
            for (long i = 1; i <= updateOrdinalNumbersCount; i++) {
                Contact contact = contactsRepository.findById(id + i).orElse(null);

                if (contact != null) {
                    contact.setOrdinalNumber(contactOrdinalNumber++);

                    contactsRepository.save(contact);
                }
            }
        }

        return GeneralResponse.getSuccessResponse();
    }

    private GeneralResponse create(Contact contact) {
        if (contactsRepository.findByPhoneNumber(contact.getPhoneNumber()) != null) {
            return GeneralResponse.getErrorResponse("Контакт с таким номером телефона уже существует.");
        }

        contact.setOrdinalNumber(contactsRepository.count() + 1);

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
            return GeneralResponse.getErrorResponse("Контакт с таким номером телефона уже существует.");
        }

        repositoryContact.setSurname(contact.getSurname());
        repositoryContact.setName(contact.getName());
        repositoryContact.setPhoneNumber(contactPhoneNumber);

        contactsRepository.save(repositoryContact);

        return GeneralResponse.getSuccessResponse();
    }
}
