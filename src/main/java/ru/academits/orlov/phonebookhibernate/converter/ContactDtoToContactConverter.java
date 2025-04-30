package ru.academits.orlov.phonebookhibernate.converter;

import org.springframework.stereotype.Service;
import ru.academits.orlov.phonebookhibernate.dto.ContactDto;
import ru.academits.orlov.phonebookhibernate.entity.Contact;

@Service
public class ContactDtoToContactConverter implements Converter<ContactDto, Contact> {
    @Override
    public Contact convert(ContactDto source) {
        Contact contact = new Contact();
        contact.setId(source.getId());
        contact.setName(source.getName());
        contact.setSurname(source.getSurname());
        contact.setPhoneNumber(source.getPhoneNumber());

        return contact;
    }
}
