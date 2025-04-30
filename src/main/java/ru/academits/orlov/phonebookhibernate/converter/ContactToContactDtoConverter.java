package ru.academits.orlov.phonebookhibernate.converter;

import org.springframework.stereotype.Service;
import ru.academits.orlov.phonebookhibernate.dto.ContactDto;
import ru.academits.orlov.phonebookhibernate.entity.Contact;

@Service
public class ContactToContactDtoConverter implements Converter<Contact, ContactDto> {
    @Override
    public ContactDto convert(Contact source) {
        ContactDto contactDto = new ContactDto();
        contactDto.setId(source.getId());
        contactDto.setName(source.getName());
        contactDto.setSurname(source.getSurname());
        contactDto.setPhoneNumber(source.getPhoneNumber());

        return contactDto;
    }
}
