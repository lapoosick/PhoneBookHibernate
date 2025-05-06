package ru.academits.orlov.phonebookhibernate.controller;

import org.springframework.web.bind.annotation.*;
import ru.academits.orlov.phonebookhibernate.converter.ContactDtoToContactConverter;
import ru.academits.orlov.phonebookhibernate.converter.ContactToContactDtoConverter;
import ru.academits.orlov.phonebookhibernate.dto.ContactDto;
import ru.academits.orlov.phonebookhibernate.dto.GeneralResponse;
import ru.academits.orlov.phonebookhibernate.service.ContactsService;

import java.util.List;

@RestController
@RequestMapping("/api/contacts")
public class ContactsController {
    private final ContactsService contactsService;
    private final ContactToContactDtoConverter contactToContactDtoConverter;
    private final ContactDtoToContactConverter contactDtoToContactConverter;

    public ContactsController(ContactsService contactsService,
                              ContactToContactDtoConverter contactToContactDtoConverter,
                              ContactDtoToContactConverter contactDtoToContactConverter) {
        this.contactsService = contactsService;
        this.contactToContactDtoConverter = contactToContactDtoConverter;
        this.contactDtoToContactConverter = contactDtoToContactConverter;
    }

    @PostMapping
    public GeneralResponse saveContact(@RequestBody ContactDto contactDto) {
        return contactsService.createOrUpdateContact(contactDtoToContactConverter.convert(contactDto));
    }

    @GetMapping
    public List<ContactDto> getContacts(@RequestParam(required = false) String term) {
        return contactToContactDtoConverter.convert(contactsService.getContacts(term));
    }

    @DeleteMapping("/{id}")
    public GeneralResponse deleteContact(@PathVariable Long id) {
        return contactsService.deleteContact(id);
    }
}
